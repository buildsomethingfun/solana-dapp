# common-navigation

Type-safe navigation framework built on top of Jetpack Navigation Compose. Handles screens, bottom sheets, dialogs, overlays, and toasts through a single `Navigator` interface.

## Architecture

```
┌────────────────────────────────────────────────────────────────┐
│                        AppNavigation                           │
│                                                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  ToastOverlay     (z: top)     ← auto-dismiss queue     │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │  DialogOverlay    (z: 3)       ← modal content          │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │  ScreenOverlay    (z: 2)       ← slide-up full screen   │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │  SheetOverlay     (z: 1)       ← drag-to-dismiss sheet  │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │  NavHost          (z: base)    ← screen back stack      │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                │
│  AppNavigator routes destination type to the correct layer     │
└────────────────────────────────────────────────────────────────┘
```

## Destination Type Hierarchy

All navigation targets implement `Destination` (`core/Destination.kt`):

```kotlin
interface Destination {
    interface Screen : Destination    // Full-screen, managed by NavController
    interface Sheet : Destination     // Bottom sheet with drag-to-dismiss + scrim
    interface Dialog : Destination    // Modal dialog
    interface Toast : Destination     // Transient notification, auto-dismiss
}
```

Concrete destinations in `AppDestination.kt` — a `@Serializable sealed interface` enabling type-safe routing:

```kotlin
@Serializable
sealed interface AppDestination : Destination {
    data object Splash : AppDestination, Destination.Screen
    data object Auth : AppDestination, Destination.Screen
    data object Home : AppDestination, Destination.Screen
    data object Apps : AppDestination, Destination.Screen
    data class AppDetail(val id: String) : AppDestination, Destination.Screen
    data object NewApp : AppDestination, Destination.Screen
    data object Profile : AppDestination, Destination.Screen
    data class ErrorToast(val text: String) : AppDestination, Destination.Toast
    data class SuccessToast(val text: String) : AppDestination, Destination.Toast
}
```

To add a new destination: add an entry here implementing the appropriate `Destination.*` sub-interface.

## Navigator

### Interface (`core/Navigator.kt`)

```kotlin
interface Navigator {
    val activity: AppCompatActivity
    fun open(destination: Destination, options: NavigationBuilder.() -> Unit = {})
    fun back()
}
```

### Navigation Options

```kotlin
sealed interface NavigationOption {
    data object SingleTop : NavigationOption       // launchSingleTop = true
    data object ClearStack : NavigationOption       // Pop entire back stack from root
    data object OverCurrentContent : NavigationOption  // Show Screen as overlay instead of pushing
}
```

### Builder DSL (`core/NavigationBuilder.kt`)

```kotlin
navigator.open(AppDestination.Home) {
    +NavigationOption.ClearStack      // unaryPlus adds option
    +NavigationOption.SingleTop
    popUpTo AppDestination.Splash     // infix function to pop back to destination
}
```

### AppNavigator dispatch logic (`core/AppNavigator.kt`)

`open()` routes by destination type:

| Destination type | Target |
|---|---|
| `Toast` | `toastState.show()` |
| `Dialog` | `dialogState.show()` |
| `Sheet` | `sheetState.show()` |
| `Screen` | `navController.navigate()` or `overlayState.show()` if `OverCurrentContent` |

Before navigating, processes `popUpTo` and `ClearStack` options — pops the back stack and hides any active overlays/sheets/dialogs.

`back()` dismisses layers in priority order:

```
Overlay visible?  → hide overlay
Dialog visible?   → hide dialog
Sheet visible?    → hide sheet
Can pop back?     → popBackStack()
Otherwise         → activity.finish()
```

Uses `runBlocking` to synchronously read overlay states for back decisions.

## Composable Destination Registration (`core/ComposableDestination.kt`)

Extension function for registering screen routes with optional animations:

```kotlin
inline fun <reified T : Destination.Screen> NavGraphBuilder.destination(
    animate: NavAnimation = None,
    content: @Composable (T?) -> Unit
)

enum class NavAnimation { None, SlideUp, SlideRight, Fade }
```

Usage in `AppNavigation`'s `destinations` lambda:

```kotlin
destination<AppDestination.Splash> { SplashScreen(navigator, storeProvider) }
destination<AppDestination.AppDetail>(animate = SlideRight) { args ->
    AppDetailScreen(id = args?.id.orEmpty(), navigator)
}
```

Route arguments (like `AppDetail.id`) are automatically serialized/deserialized via `backstack.toRoute<T>()`.

## AppNavigation Composable (`core/AppNavigation.kt`)

Top-level composable wiring everything together:

```kotlin
@Composable
fun AppNavigation(
    activity: AppCompatActivity,
    startDestination: Destination.Screen,
    destinations: NavGraphBuilder.(Navigator) -> Unit,   // Register screen routes
    sheets: @Composable (Destination.Sheet, Navigator) -> Unit = ...,
    dialogs: @Composable (Destination.Dialog, Navigator) -> Unit = ...,
    overlayScreens: @Composable (Destination.Screen, Navigator) -> Unit = ...,
    toasts: @Composable (Destination.Toast, Navigator) -> Unit = ...,
)
```

Creates `NavController`, all overlay state objects, and `AppNavigator`. Renders `NavHost` + all overlay layers.

## Overlay Systems

### Toast (`core/toast/`)

**Queue-based** system — multiple rapid toasts display sequentially, not simultaneously.

`ToastState` manages:
- `queue: StateFlow<List<ToastItem>>` — pending toasts
- `activeToast: StateFlow<ToastItem?>` — currently displayed toast

Flow: `show()` → add to queue → if nothing active, promote first item → `ToastOverlay` displays for 2s → `dismiss()` → show next.

`ToastOverlay` composable:
- Positioned at top center with status bar padding
- Slides in from top, fades out
- Auto-dismisses after 2000ms via `LaunchedEffect`
- Transitions between consecutive toasts with 150ms delay

### Sheet (`core/sheet/`)

`SheetState` holds a single `Destination.Sheet?`.

`SheetOverlay` composable:
- Slides up from 1000dp below with custom cubic bezier easing
- Semi-transparent scrim (max 0.6 alpha) fades with drag progress
- Drag-to-dismiss with `VelocityTracker`:
  - Fast flick (> 800 velocity) → dismiss
  - Drag past threshold (> 150dp) → dismiss
  - Otherwise → snap back with spring animation
- Scrim tap and back button both dismiss
- Calls `navigator.back()` after dismiss animation completes

### Screen Overlay (`core/overlay/`)

`OverlayScreenState` holds a single `Destination.Screen?`.

`ScreenOverlay` composable:
- Full-screen overlay that slides in/out vertically
- Triggered by `OverCurrentContent` navigation option
- 300ms exit animation delay before clearing state

### Dialog (`core/dialog/`)

`DialogState` holds a single `Destination.Dialog?`.

`DialogOverlay` composable:
- Simplest overlay — renders content directly when state is non-null
- No built-in animations; the content composable handles dialog styling

## Tab Navigation

Tab navigation is handled externally in `feature-main` via `TabNavigator` — a `Navigator` implementation that:
- Routes `Destination.Screen` to a tab-local `NavController`
- Delegates everything else (toasts, sheets, dialogs) to the global `AppNavigator`
- Each tab has its own `NavHost` for isolated back stacks

## File Index

| File | Purpose |
|---|---|
| `AppDestination.kt` | All app destinations as `@Serializable sealed interface` |
| `core/Destination.kt` | Base destination type hierarchy (Screen, Sheet, Dialog, Toast) |
| `core/Navigator.kt` | `Navigator` interface + `NavigationOption` sealed interface |
| `core/AppNavigator.kt` | Concrete navigator routing destinations to layers |
| `core/NavigationBuilder.kt` | DSL builder for navigation options + `NavigationConfig` |
| `core/ComposableDestination.kt` | `NavGraphBuilder.destination()` extension + `NavAnimation` |
| `core/AppNavigation.kt` | Top-level composable wiring NavHost + all overlays |
| `core/toast/ToastState.kt` | Queue-based toast state management |
| `core/toast/ToastItem.kt` | Toast data (id, destination, timestamp) |
| `core/toast/ToastOverlay.kt` | Auto-dismiss toast composable with slide+fade animations |
| `core/sheet/SheetState.kt` | Single-sheet state holder |
| `core/sheet/SheetOverlay.kt` | Drag-to-dismiss bottom sheet with scrim + velocity tracking |
| `core/overlay/OverlayScreenState.kt` | Single-overlay state holder |
| `core/overlay/ScreenOverlay.kt` | Slide-in/out full-screen overlay |
| `core/dialog/DialogState.kt` | Single-dialog state holder |
| `core/dialog/DialogOverlay.kt` | Simple dialog container |
