# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**buildsomething.fun (actions.FUN)** — Native Android app for the Solana dApp Store. Users can vibecode apps and publish them. Built for a hackathon.

- Package: `dapp.buildsomething`
- Min SDK 31 / Target SDK 35 / Compile SDK 35
- JDK 17, Kotlin 2.1.20, AGP 8.6.0
- Compose-first UI with Material 3


## Module Structure

All source modules live under `sources/`:

```
sources/
├── app/                          # Application entry point, Koin DI setup
├── common/
│   ├── common-arch/              # TEA architecture framework
│   ├── common-navigation/        # AppDestination definitions + navigator
│   ├── common-network/           # Retrofit, OkHttp, backend URL config
│   ├── common-ui/                # Theme, shared composables, ExoPlayer, Coil
│   └── common-util/              # Extension functions, lifecycle utils
├── feature/
│   ├── feature-splash/           # Splash screen (checks auth state)
│   ├── feature-auth/             # Wallet connection
│   ├── feature-main/             # Bottom nav container (Apps, NewApp, Profile tabs)
│   ├── feature-apps/             # App listing + detail screens
│   ├── feature-newapp/           # New dApp submission
│   └── feature-profile/         # User profile
└── repository/
    ├── repository-preferences/   # DataStore-based local preferences
    ├── repository-user/          # User data management
    └── repository-solana/        # Solana Mobile Wallet Adapter, RPC, transactions
```

Build conventions live in `build-conventions/` (android, kotlin, environment, util).

## Architecture: TEA (The Elm Architecture)

Every feature follows a strict TEA/MVI pattern via `TeaViewModel` in `common-arch`:

- **State** → internal presentation state
- **UiState** → mapped state exposed to Compose UI
- **UiEvent** → user interactions from UI (extends Event)
- **Event** → triggers Reducer
- **Command** → triggers Actor (side effects like network calls)
- **Effect** → one-shot UI events (navigation, toasts)
- **Reducer** → pure function: `(State, Event) → StateUpdate(state, commands, effects)`
- **Actor** → side effect handler: `Flow<Command> → Flow<Event>`

Each feature module has a `StoreProvider` (Koin factory) that wires the Actor, Reducer, and UiStateMapper into a concrete `TeaViewModel`.

## Navigation

Defined in `common-navigation/AppDestination.kt` as a `@Serializable sealed interface`. Destinations: `Splash`, `Auth`, `Home`, `Apps`, `AppDetail(id)`, `NewApp`, `Profile`, `ErrorToast(text)`, `SuccessToast(text)`.

Flow: `Splash` → checks user state → `Auth` (wallet connect) or `Home` (bottom nav with tabs).

`AppNavigator` handles navigation commands; supports screens, sheets, dialogs, overlays, and toasts.

## Key Dependencies

- **DI**: Koin 4.0.2 (module declarations in each module's `di/` package, wired in `AppModule`)
- **Networking**: Retrofit 2.11 + OkHttp 4.12 with KotlinX Serialization
- **Solana**: Mobile Wallet Adapter 2.0.3, sol4k 0.5.14, Solana RPC 0.2.8
- **Images**: Coil 2.6
- **Media**: ExoPlayer (Media3 1.7.1)
- **Logging**: Timber

## Network Configuration

Backend URLs in `common-network/.../BackendUrl.kt`:

## Conventions

- Gradle version catalog: `gradle/libs.versions.toml`
- Module naming: `common-*`, `feature-*`, `repository-*`
- Package naming: `dapp.buildsomething.{common|feature|repository}.{module}`
- All Compose libraries applied via `convention.android-library-compose` plugin
- Kotlin language version 1.9, JVM target 17
- Signing config in `build-conventions/environment/.../Signing.kt`
- Version code/name configurable via `versionCode`/`versionName` env vars for CI
