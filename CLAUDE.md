## Project Overview

CheatShqip is an Android app for English → Albanian word translation and grammatical lookup.
It features a custom design system (:tosk) and a demo app (:toskdemo).

## Module Structure

| Module | Purpose |
|---|---|
| `:app` | Main application (`com.cheatshqip`) |
| `:tosk` | Custom design system (Compose components + token system) |
| `:toskdemo` | Demo app for Tosk components |
| `:build-logic` | Custom Gradle convention plugins |

## Architecture

Hexagonal (Clean) Architecture, strictly enforced:

- **Domain** — immutable value objects (`Word`, `Translation`, `AlbanianWordDetail`, declensions)
- **Application** — use cases as interfaces (input ports), output ports for external deps
- **Adapters** — REST (Retrofit + OkHttp), ML Kit translation, Koin DI wiring
- **Presentation** — MVVM with Compose; sealed `HomeScreenUIState`, `HomeScreenViewModel`

Key package layout under `com.cheatshqip`:
- `domain/` — business entities and value objects
- `application/port/input/` — use case interfaces
- `application/port/output/` — output port interfaces
- `adapter/output/` — REST adapter, ML Kit adapter
- `di/` — Koin module (`ApplicationModule.kt`)

## Tech Stack

- **Language**: Kotlin 2.3.10
- **UI**: Jetpack Compose (BOM 2026.02.01), Material 3
- **DI**: Koin 4.1.1
- **Networking**: Retrofit 3.0.0, OkHttp 5.3.2, Kotlinx Serialization JSON
- **Translation**: ML Kit Translate 17.0.3 (English → Albanian)
- **Coroutines**: kotlinx-coroutines 1.10.2
- **Testing**: JUnit 5 (Jupiter) 6.0.3, MockWebServer, Koin Test, Compose UI Test
- **Lint**: Detekt 1.23.8
- **Min SDK**: 24 (app), 21 (tosk) — note: toskdemo was bumped to 23 for navigationevent compat
- **Target/Compile SDK**: 36

## Testing

Always run tests in a subagent. Report only pass/fail counts and failure details back to the main conversation.

Test results (XML) are located at:
- `<module>/build/test-results/testDebugUnitTest/TEST-*.xml`

### Test Conventions

- Unit tests use JUnit 5 with backtick descriptive names: `` `given X, should Y` ``
- Fakes over mocks: `FakeAlbanianTranslationOutputAdapter`, `FakeWordSuggestionsOutputAdapter`
- Integration tests live in `src/test/java/.../integration/` and use MockWebServer
- UI tests in `src/androidTest/` use `createAndroidComposeRule<MainActivity>()`
- Async tests use `runTest` with up to 15s timeout

## API

- Backend: AWS API Gateway (eu-central-1)
- Endpoint: `GET /define/{word}` — returns exact and fuzzy Albanian word matches
- Serialization: Kotlinx Serialization JSON via Retrofit converter

## Design System (Tosk)

Token-based design system with primitive and semantic tokens.
Components: Button, Card, TextField, Badge, Chip, TopAppBar.
Supports light/dark theme via Compose MaterialTheme wrapper.
