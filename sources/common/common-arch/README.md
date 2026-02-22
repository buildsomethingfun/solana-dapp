# common-arch

TEA (The Elm Architecture) framework for unidirectional state management. Every feature with non-trivial state should use this module.

## Data Flow

```
  UI                         TeaViewModel                        Side Effects
  ──                         ────────────                        ────────────

  accept(UiEvent)
       │
       ▼
  eventsFlow ──► Reducer.reduce(state, event) ──► Update
                      │            │           │
                      ▼            ▼           ▼
                  stateFlow   commandsFlow  effectsChannel
                      │            │           │
                      ▼            ▼           ▼
                  UiStateMapper   Actor     effects Flow
                      │            │
                      ▼            │
                  uiStateFlow      └──► Events back to eventsFlow
                      │                       (cycle continues)
                      ▼
                state: StateFlow<UiState>
```

## Components

### Store (`Store.kt`)

Abstract base class extending `ViewModel`. Public contract for the UI layer:

```kotlin
abstract class Store<Effect, UiEvent, UiState> : ViewModel() {
    abstract val effects: Flow<Effect>
    abstract val state: StateFlow<UiState>
    abstract fun accept(event: UiEvent)
}
```

### TeaViewModel (`TeaViewModel.kt`)

Core implementation wiring all TEA components together. Accepts 6 type parameters:

| Type param | Role |
|---|---|
| `Command` | Triggers Actor side effects |
| `Effect` | One-shot events to UI (navigation, toasts) |
| `Event` | Base event type flowing through the system |
| `UiEvent : Event` | Subset of events originating from UI |
| `State` | Internal presentation state |
| `UiState` | State exposed to UI (optionally mapped from State) |

Two constructors: single `Actor` or `Set<Actor>` (merged via `Flow.merge()`).

Initialization order in `init {}`:
1. `setupStateFlow()` — State -> UiState pipeline with `distinctUntilChanged()`
2. `setupCommandsFlow()` — Commands -> Actor -> Events
3. `setupEventsFlow()` — Events -> Reducer -> Update (state + commands + effects)
4. `applyInitialEvents()` — Emits initial events asynchronously in `viewModelScope`

### Actor (`component/Actor.kt`)

Functional interface for side effects. Receives a `Flow<Command>`, returns `Flow<Event>`.

```kotlin
fun interface Actor<Command, Event> {
    fun act(commands: Flow<Command>): Flow<Event>
}
```

Typical implementation filters for specific command types, calls repositories, maps results to events, and catches errors:

```kotlin
class LoadUserActor(private val repo: UserRepository) : Actor<Command, Event> {
    override fun act(commands: Flow<Command>): Flow<Event> {
        return commands
            .filterIsInstance<Command.LoadUser>()
            .flatMapLatest { repo.user.mapLatest(Event::UserLoaded) }
    }
}
```

### Reducer (`component/Reducer.kt`)

Pure function: `(State, Event) -> Update`. Contains presentation logic.

```kotlin
interface Reducer<Command, Effect, Event, State> {
    fun reduce(currentState: State, event: Event): Update<Command, Effect, State>
}
```

### DslReducer (`dsl/DslReducer.kt`)

Ergonomic base class that eliminates `Update(...)` boilerplate. Provides `state {}`, `commands()`, `effects()` builder functions:

```kotlin
class MyReducer : DslReducer<Command, Effect, Event, State>() {
    override fun reduce(event: Event) {
        when (event) {
            is UiEvent.ButtonClicked -> {
                state { copy(isLoading = true) }
                commands(Command.LoadData)
            }
            is Event.DataLoaded -> {
                state { copy(isLoading = false, data = event.data) }
            }
            is Event.DataFailed -> {
                state { copy(isLoading = false) }
                effects(Effect.ShowError(event.message))
            }
        }
    }
}
```

Uses an internal `Updater` that accumulates state/commands/effects during `reduce()`, then collects into an `Update` object.

### Update (`component/Update.kt`)

Result type from Reducer:

```kotlin
class Update<Command, Effect, State>(
    val state: State? = null,          // null = no state change
    val commands: List<Command> = emptyList(),
    val effects: List<Effect> = emptyList(),
)
```

### UiStateMapper (`component/UiStateMapper.kt`)

Optional mapping from internal `State` to `UiState`. If not provided, `State` is cast directly to `UiState` — meaning `State` and `UiState` must be the same type.

```kotlin
fun interface UiStateMapper<State, UiState> {
    fun map(state: State): UiState
}
```

### Renderer + bind() (`component/Renderer.kt`, `util/TeaStoreBoundary.kt`)

For Fragment-based UI (not Compose). `bind()` attaches lifecycle-aware collectors:
- State rendered at `STARTED`
- Effects rendered at `RESUMED`

In Compose screens, use `store.state.collectAsState()` and `LaunchedEffect` for effects instead.

## Threading Model

- All flow operations run on `viewModelScope` (Main dispatcher)
- State mutations are serialized through `eventsFlow` — no concurrent state races
- `accept()` is safe to call from any thread
- `SharedFlow(replay = 1)` on events and commands prevents losing the first emission before subscriptions are active
- `Channel(BUFFERED)` on effects ensures one-time delivery (not replayed on resubscription)

## How Features Use This

Each feature module follows this pattern:

```
feature-xxx/
├── presentation/
│   ├── XxxState.kt          # State + UiState (often the same type)
│   ├── XxxCommand.kt        # sealed interface of commands
│   ├── XxxEvent.kt          # sealed interface of events + UiEvent sub-interface
│   ├── XxxEffect.kt         # sealed interface of effects
│   ├── XxxReducer.kt        # extends DslReducer
│   ├── actors/
│   │   └── SomeActor.kt     # Actor implementation per side-effect concern
│   └── XxxStoreProvider.kt  # Abstract class with viewModelFactory(), DI wiring
├── ui/
│   └── XxxScreen.kt         # Compose screen consuming the Store
└── di/
    └── XxxModule.kt         # Koin module
```

Screen composable pattern:

```kotlin
@Composable
fun XxxScreen(navigator: Navigator, storeProvider: XxxStoreProvider) {
    val store = viewModel<XxxStore>(factory = storeProvider.viewModelFactory())
    val state by store.state.collectAsState()

    LaunchedEffect(Unit) {
        store.effects.onEach { effect ->
            when (effect) {
                is Effect.NavigateToHome -> navigator.open(AppDestination.Home) {
                    +NavigationOption.ClearStack
                }
                is Effect.ShowError -> navigator.open(AppDestination.ErrorToast(effect.message))
            }
        }.launchIn(this)
    }

    XxxScreenContent(state = state, onAction = { store.accept(it) })
}
```

## File Index

| File | Visibility | Purpose |
|---|---|---|
| `tea/Store.kt` | public | Abstract ViewModel base — the UI-facing contract |
| `tea/TeaViewModel.kt` | public | Full TEA wiring implementation |
| `tea/component/Actor.kt` | public | Side-effect handler interface |
| `tea/component/Reducer.kt` | public | Event->State reducer interface |
| `tea/component/Update.kt` | public | Reducer result type |
| `tea/component/UiStateMapper.kt` | public | Optional State->UiState mapping |
| `tea/component/Renderer.kt` | public | Render callback interface (Fragment usage) |
| `tea/dsl/DslReducer.kt` | public | DSL-style reducer base class |
| `tea/dsl/Updater.kt` | internal | Mutable accumulator for DslReducer |
| `tea/util/TeaUtils.kt` | internal | `combineActors()` via Flow.merge() |
| `tea/util/TeaStoreBoundary.kt` | public | `Store.bind()` extension for Fragment lifecycle |
| `lifecycle/LifecycleUtils.kt` | public | `launchRepeatOnCreated/Started/Resumed` extensions |
