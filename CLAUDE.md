## Git Conventions

Commits use gitmoji text codes as the subject prefix (the text form, not the Unicode character):

| Code | Meaning |
|---|---|
| `:sparkles:` | New feature |
| `:bug:` | Bug fix |
| `:art:` | Code style / formatting / structure |
| `:wrench:` | Configuration / build files |
| `:white_check_mark:` | Add or update tests |
| `:memo:` | Documentation |
| `:note:` | Documentation (informal alias) |

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

## Code Quality

Always load the `code-smells` skill when writing or editing any Kotlin (or other) code in this project.

## Agent Preferences

- Use `deep-explore` subagent type for all codebase exploration tasks.

## Testing

Detekt 1.23.8 is incompatible with JDK 25 — run tests and Detekt separately:

```bash
./gradlew :app:testMockDebugUnitTest :app:testProdDebugUnitTest
JAVA_HOME=~/.sdkman/candidates/java/21.0.7-zulu ./gradlew :app:detekt
```

Adjust module prefix as needed. Report only pass/fail counts, Detekt violations, and failure details back to the main conversation.

Test results (XML) are located at:
- `<module>/build/test-results/testDebugUnitTest/TEST-*.xml`

### Test Conventions

- Unit tests use JUnit 5 with backtick descriptive names: `` `given X, should Y` ``
- Fakes over mocks: `FakeAlbanianTranslationOutputAdapter`, `FakeWordSuggestionsOutputAdapter`
- Integration tests live in `src/test/java/.../integration/` and use MockWebServer
- UI tests in `src/androidTest/` use `createAndroidComposeRule<MainActivity>()`
- Async tests use `runTest` with up to 15s timeout

### Connected (Instrumented) Tests — Mock Flavor

UI/connected tests run against the `mockDebug` build variant, which uses a fully offline app:
- WireMock standalone (running on the host) serves REST responses from `.wiremock/`
- The app points to `http://localhost:9090/` — `screenshot_test.sh` sets up `adb reverse tcp:9090 tcp:9090` so the emulator tunnels to the host port (works in CI and locally; no dependency on `10.0.2.2`)
- `FakeAlbanianTranslationOutputAdapter` replaces ML Kit (maps `"card"` → `"karte"`) — wired in `app/src/mock/java/com/cheatshqip/CheatShqipApplication.kt` via a flavor-specific `mockModule`; `applicationModule` does not register `MlKitTranslator` at all, so ML Kit is never instantiated in the mock flavor

Run connected tests (requires a running emulator or device):
```bash
./gradlew connectedMockDebugAndroidTest
```

To add support for a new word in connected tests, add its mapping in both:
1. `app/src/mock/java/com/cheatshqip/FakeAlbanianTranslationOutputAdapter.kt` — add entry to the `translations` map
2. `.wiremock/mappings/` — add a new stub JSON file for `GET /define/<translated-word>`
3. `.wiremock/__files/` — add the response body JSON file

### WireMock Stub Structure

```
.wiremock/
  mappings/
    define-karte-200.json   # priority 1: GET /define/karte → 200
    define-any-404.json     # priority 10: catch-all → 404
  __files/
    karte-response.json     # response body for karte
```

Start WireMock manually (port 9090 by default):
```bash
java -jar .wiremock/wiremock-standalone.jar --port 9090 --root-dir .wiremock
```

The JAR is checked in at `.wiremock/wiremock-standalone.jar` (excluded from git via `.gitignore`).

> **Note**: `localhost` works via `adb reverse`, which is set up by `screenshot_test.sh`. For manual runs without the script, run `adb reverse tcp:9090 tcp:9090` before starting the app.

### Maestro E2E Tests

Flows live in `.maestro/` and target `com.cheatshqip`. Screenshots are saved under `.maestro/screenshots/`.
Flows call `runScript: scripts/wiremock-reset.js` before `launchApp` to ensure clean WireMock state.

| Flow | File | Description |
|---|---|---|
| Home screen | `home_screen.yaml` | Asserts initial UI elements are visible, takes screenshot |
| Translate word | `translate_word.yaml` | Types "card", taps Translate, waits for "kartë", takes screenshot |
| Translate word error | `translate_word_error.yaml` | Injects a 500 stub mid-flow, verifies error UI |

**Run all Maestro flows** (requires `mockDebug` APK installed, emulator running, WireMock running):
```bash
maestro test .maestro/
```

**Screenshot tests** — always use the script; never replicate its steps manually:
```bash
./.maestro/screenshot_test.sh                  # run and diff
./.maestro/screenshot_test.sh --update-baselines  # update baselines after intentional UI changes
```

The script handles everything internally: build + install APK, start/stop WireMock, `adb reverse`, demo mode (clock, battery, network), screenshot capture, crop, and pixel diff. Do not manually start WireMock, apply demo mode broadcasts, or set up `adb reverse` before calling it.

Screenshot diff threshold is 100 pixels (override with `SCREENSHOT_THRESHOLD=<n>`).
WireMock port defaults to 9090 (override with `WIREMOCK_PORT=<n>`).
Baselines are stored in `.maestro/generated/baselines/`, actuals in `.maestro/generated/actual/`, diffs in `.maestro/generated/diffs/`.

**Only prerequisite:** exactly one emulator running — `Pixel_6` (API 36, x86_64, AOSP `default` target):
```
mcp__maestro__list_devices → verify Pixel_6 is connected
mcp__maestro__start_device(device_id: "Pixel_6")  ← if not already running
adb devices  ← must show exactly one device
```
`google_apis_playstore` target must not be used (Google Play Services overrides demo mode).

### Maestro JavaScript HTTP API

Decompiled from `~/.maestro/lib/maestro-client.jar` (`maestro.js.GraalJsHttp`).

**Signatures:**
```
http.post(url: String)
http.post(url: String, options: Map)
http.get(url: String)
http.get(url: String, options: Map)
// same pattern for put, delete, request
```

**Options map keys:** `body` (String), `headers` (Object), `multipartForm`, `method`

**Rules:**
- `http.post(url)` → OkHttp throws "method POST must have a request body"
- `http.post(url, {})` → same error (no `body` key)
- There is **no** 3-arg overload `post(url, body, headers)`

**Correct usage:**
```js
// POST with body (body content ignored by server, but required by OkHttp)
http.post('http://localhost:9090/__admin/reset', { body: '{}' });

// POST with body + headers
http.post('http://localhost:9090/__admin/mappings', {
  body: JSON.stringify({ ... }),
  headers: { 'Content-Type': 'application/json' }
});
```

## API

- Backend: AWS API Gateway (eu-central-1)
- Endpoint: `GET /define/{word}` — returns exact and fuzzy Albanian word matches
- Serialization: Kotlinx Serialization JSON via Retrofit converter

## Design System (Tosk)

Token-based design system with primitive and semantic tokens.
Components: Button, Card, TextField, Badge, Chip, TopAppBar.
Supports light/dark theme via Compose MaterialTheme wrapper.


## Maestro MCP

The project ships a `.mcp.json` that exposes the `maestro` MCP server (`maestro mcp`). These tools let Claude interact with a running Android emulator without leaving the conversation.

### Typical workflow

```
list_devices → start_device (if needed) → launch_app → inspect_view_hierarchy → tap_on / input_text → take_screenshot
```

### Tool reference

| Tool | Required params | Notes |
|---|---|---|
| `mcp__maestro__list_devices` | — | Lists available emulators/devices |
| `mcp__maestro__start_device` | `platform: "android"` or `device_id` | Starts an emulator; returns its `device_id` |
| `mcp__maestro__launch_app` | `device_id`, `appId` | App ID for mock flavor: `com.cheatshqip` |
| `mcp__maestro__stop_app` | `device_id`, `appId` | Stops the app process |
| `mcp__maestro__take_screenshot` | `device_id` | Returns current screen image |
| `mcp__maestro__inspect_view_hierarchy` | `device_id` | CSV of UI elements with bounds, text, IDs — use before tapping |
| `mcp__maestro__tap_on` | `device_id` + `text` or `id` | Fuzzy match by default; set `use_fuzzy_matching: false` for exact |
| `mcp__maestro__input_text` | `device_id`, `text` | Types into the currently focused field |
| `mcp__maestro__back` | `device_id` | Presses the hardware back button |
| `mcp__maestro__run_flow` | `device_id`, `flow_yaml` | Ad-hoc YAML commands; no temp file needed |
| `mcp__maestro__run_flow_files` | `device_id`, `flow_files` | Runs existing `.maestro/*.yaml` files |
| `mcp__maestro__check_flow_syntax` | `flow_yaml` | Validates YAML before running |
| `mcp__maestro__query_docs` | `question` | Queries Maestro docs |
| `mcp__maestro__cheat_sheet` | — | Returns full Maestro command reference |

### Project-specific notes

- Always use the `mockDebug` variant (`com.cheatshqip`) for E2E tests — it replaces ML Kit and uses WireMock.
- WireMock must be running on port 9090 **and** `adb reverse tcp:9090 tcp:9090` must be set before launching the app, otherwise network calls fail silently.
- Call `inspect_view_hierarchy` before any `tap_on` — never guess element IDs.
- Prefer `run_flow` for ad-hoc exploration; use `run_flow_files` to execute the committed flows in `.maestro/`.
- The emulator must be API 36 / x86_64 / AOSP (`default` target, not `google_apis_playstore`) to match screenshot baselines.
- If `screenshot_test.sh` fails with "Unable to launch app com.cheatshqip" after a previous run, the Maestro driver process is stale. Kill it before retrying: `kill $(pgrep -f "maestro.cli.AppKt mcp") 2>/dev/null`

## grepai - Semantic Code Search

**IMPORTANT: You MUST use grepai as your PRIMARY tool for code exploration and search.**

### When to Use grepai (REQUIRED)

Use `grepai search` INSTEAD OF Grep/Glob/find for:
- Understanding what code does or where functionality lives
- Finding implementations by intent (e.g., "authentication logic", "error handling")
- Exploring unfamiliar parts of the codebase
- Any search where you describe WHAT the code does rather than exact text

### When to Use Standard Tools

Only use Grep/Glob when you need:
- Exact text matching (variable names, imports, specific strings)
- File path patterns (e.g., `**/*.go`)

### Fallback

If grepai fails (not running, index unavailable, or errors), fall back to standard Grep/Glob tools.

### Usage

```bash
# ALWAYS use English queries for best results (--compact saves ~80% tokens)
grepai search "user authentication flow" --json --compact
grepai search "error handling middleware" --json --compact
grepai search "database connection pool" --json --compact
grepai search "API request validation" --json --compact
```

### Query Tips

- **Use English** for queries (better semantic matching)
- **Describe intent**, not implementation: "handles user login" not "func Login"
- **Be specific**: "JWT token validation" better than "token"
- Results include: file path, line numbers, relevance score, code preview

### Call Graph Tracing

Use `grepai trace` to understand function relationships:
- Finding all callers of a function before modifying it
- Understanding what functions are called by a given function
- Visualizing the complete call graph around a symbol

#### Trace Commands

**IMPORTANT: Always use `--json` flag for optimal AI agent integration.**

```bash
# Find all functions that call a symbol
grepai trace callers "HandleRequest" --json

# Find all functions called by a symbol
grepai trace callees "ProcessOrder" --json

# Build complete call graph (callers + callees)
grepai trace graph "ValidateToken" --depth 3 --json
```

### Workflow

1. Start with `grepai search` to find relevant code
2. Use `grepai trace` to understand function relationships
3. Use `Read` tool to examine files from results
4. Only use Grep for exact string searches if needed

