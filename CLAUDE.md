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
- **Adapters** — Room/SQLite against the bundled dictionary database, Koin DI wiring
- **Presentation** — MVVM with Compose; sealed `HomeScreenUIState`, `HomeScreenViewModel`

Key package layout under `com.cheatshqip`:
- `domain/` — business entities and value objects
- `application/port/input/` — use case interfaces
- `application/port/output/` — output port interfaces
- `adapter/output/` — Room/SQLite adapters (bundled dictionary)
- `di/` — Koin module (`ApplicationModule.kt`)

## Tech Stack

- **Language**: Kotlin 2.4.10
- **UI**: Jetpack Compose (BOM 2026.06.01), Material 3
- **DI**: Koin 4.2.2
- **Database**: Room 2.8.4 + `sqlite-bundled` (FTS5), pre-built SQLite asset
- **Coroutines**: kotlinx-coroutines 1.11.0
- **Testing**: JUnit 5 (Jupiter) 6.1.2, Koin Test, Compose UI Test
- **Lint**: Detekt 1.23.8
- **Min SDK**: 24 (app), 21 (tosk) — note: toskdemo was bumped to 23 for navigationevent compat
- **Target/Compile SDK**: 37

## Build Troubleshooting

### Plugin/dependency "not found" after a version bump

Gradle caches a negative lookup when a dependency or plugin version can't be found in a repository, so it won't hit the network again on the next build. If a version was just published (or there was a brief network blip), the stale "not found" result can persist even though the version genuinely exists upstream — e.g. `Plugin [id: 'com.android.application', version: '9.2.1'] was not found`.

**Fix, in order of cost:**

1. Re-run with `--refresh-dependencies` — forces re-resolution, bypassing the cached negative result:
   ```bash
   ./gradlew <task> --refresh-dependencies
   ```
2. If that doesn't clear it, delete the cached entry for just that module:
   ```bash
   rm -rf ~/.gradle/caches/modules-2/files-2.1/<group>/<artifact>
   rm -rf ~/.gradle/caches/modules-2/metadata-*/descriptors/<group>/<artifact>
   ```
   e.g. for the AGP plugin marker: `~/.gradle/caches/modules-2/files-2.1/com.android.application/com.android.application.gradle.plugin`
3. Nuclear option — stop the daemon (it can hold stale metadata in memory even past a cache clear) and wipe the whole dependency cache:
   ```bash
   ./gradlew --stop
   rm -rf ~/.gradle/caches/modules-2
   ```

Before assuming a version doesn't exist, verify it independently (e.g. check `https://dl.google.com/dl/android/maven2/<group-path>/maven-metadata.xml` for AGP/AndroidX artifacts) rather than trusting the first Gradle failure.

**This also triggers on a Gradle wrapper bump alone**, not just AGP/AndroidX: `build-logic/build.gradle.kts` applies `` `kotlin-dsl` `` with no explicit version, so Gradle auto-selects the `org.gradle.kotlin.kotlin-dsl` plugin version matching the new Gradle distribution's bundled Kotlin — e.g. bumping the wrapper to 9.5.1 demands `kotlin-dsl` 6.5.7. If that plugin marker hasn't synced to the Gradle Plugin Portal yet, you get the same `Plugin [...] was not found` error; same fix (`--refresh-dependencies` first).

**IDE sync vs CLI**: a plain Android Studio sync does *not* pass `--refresh-dependencies`, so fixing this via the CLI doesn't automatically un-stick a sync that already failed with the cached negative result — re-trigger **File → Sync Project with Gradle Files** afterwards. If the IDE sync still fails, it may be talking to a Gradle daemon that cached the failure in memory (check with `./gradlew --status`); run `./gradlew --stop` and sync again to force a fresh daemon.

### IDE sync warning: "Could not resolve Gradle distribution sources"

Benign, non-fatal — logged during `prepareKotlinBuildScriptModel` (Kotlin DSL script editing support) when the IDE separately tries to download `gradle-<version>-src.zip` for "go to source" into Gradle internals. It doesn't affect build/test correctness. The wrapper uses the `-all.zip` distribution (bundles sources with binaries) specifically to avoid this extra lookup — keep `distributionUrl` pointing at `-all`, not `-bin`, when bumping the Gradle version.

### AndroidX version bumps requiring a newer compileSdk

Newer `androidx.core`/`androidx.lifecycle` releases can require a higher `compileSdk` than the project currently targets, failing at `:app:checkDebugAarMetadata` with "requires libraries and applications that depend on it to compile against version N or later." Install the required platform before bumping `compileSdk`/`targetSdk`:
```bash
sdkmanager "platforms;android-<N>.0"
```

## Code Quality

Always load the `code-smells` skill when writing or editing any Kotlin (or other) code in this project.

## Testing

Detekt 1.23.8 is incompatible with JDK 25 — run tests and Detekt separately:

```bash
./gradlew :app:testDebugUnitTest
JAVA_HOME=~/.sdkman/candidates/java/21.0.7-zulu ./gradlew :app:detekt
```

Adjust module prefix as needed. Report only pass/fail counts, Detekt violations, and failure details back to the main conversation.

Test results (XML) are located at:
- `<module>/build/test-results/testDebugUnitTest/TEST-*.xml`

### Test Conventions

- Unit tests use JUnit 5 with backtick descriptive names: `` `given X, should Y` ``
- Fakes over mocks: `FakeEnglishToAlbanianOutputAdapter`, `FakeAlbanianWordDetailOutputAdapter`
- UI tests in `src/androidTest/` use `createAndroidComposeRule<MainActivity>()`
- Async tests use `runTest` with up to 15s timeout

### Connected (Instrumented) Tests

The app is fully offline (bundled dictionary), so connected tests run against the plain
`debug` build variant. `DictionaryDatabaseAssetTest` and
`SqliteEnglishToAlbanianOutputAdapterIntegrationTest` (in `src/androidTest/`) verify the
bundled dictionary asset and FTS5 search on a real device/emulator.

Run connected tests (requires a running emulator or device):
```bash
./gradlew connectedDebugAndroidTest
```

### Maestro E2E Tests

Flows live in `.maestro/` and target `com.cheatshqip`. Screenshots are saved under `.maestro/generated/screenshots/`.

| Flow | File | Description |
|---|---|---|
| Home screen | `home_screen.yaml` | Asserts initial UI elements are visible, takes screenshot |
| Translate word | `translate_word.yaml` | Types "card", taps Translate, waits for "kartë", takes screenshot |
| Word detail | `word_detail.yaml` | Translates "card", opens "kartë", asserts declensions, takes screenshot |

**Run all Maestro flows** (requires `debug` APK installed and a running emulator):
```bash
maestro test .maestro/
```

**Screenshot tests** — always use the script; never replicate its steps manually:
```bash
./.maestro/screenshot_test.sh                  # run and diff
./.maestro/screenshot_test.sh --update-baselines  # update baselines after intentional UI changes
```

The script handles everything internally: build + install APK, demo mode (clock, battery, network), screenshot capture, crop, and pixel diff. Do not manually apply demo mode broadcasts before calling it.

Screenshot diff threshold is 100 pixels (override with `SCREENSHOT_THRESHOLD=<n>`).
Baselines are stored in `.maestro/generated/baselines/`, actuals in `.maestro/generated/actual/`, diffs in `.maestro/generated/diffs/`.

**Only prerequisite:** exactly one emulator running — `Pixel_6` (API 36, x86_64, AOSP `default` target):
```
mcp__maestro__list_devices → verify Pixel_6 is connected
mcp__maestro__start_device(device_id: "Pixel_6")  ← if not already running
adb devices  ← must show exactly one device
```
`google_apis_playstore` target must not be used (Google Play Services overrides demo mode).

## API

The app is fully offline — all translation and word-detail lookups come from the bundled SQLite dictionary. No network service is used at runtime. (A dormant AWS API Gateway backend once served `GET /define/{word}`; it has been removed from the codebase.)

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
| `mcp__maestro__launch_app` | `device_id`, `appId` | App ID: `com.cheatshqip` |
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

- Always use the `debug` variant (`com.cheatshqip`) for E2E tests — the app is fully offline (bundled dictionary), so no network service is involved.
- Call `inspect_view_hierarchy` before any `tap_on` — never guess element IDs.
- Prefer `run_flow` for ad-hoc exploration; use `run_flow_files` to execute the committed flows in `.maestro/`.
- The emulator must be API 36 / x86_64 / AOSP (`default` target, not `google_apis_playstore`) to match screenshot baselines.
- If `screenshot_test.sh` fails with "Unable to launch app com.cheatshqip" after a previous run, the Maestro driver process is stale. Kill it before retrying: `kill $(pgrep -f "maestro.cli.AppKt mcp") 2>/dev/null`

## Android CLI — `android describe`

`android describe` analyzes the project and outputs JSON model files per module (namespace, variants, APK paths, SDK versions).

**Known issue**: fails with a configuration cache error because the CLI's injected `DumpModelTask` (`.gradle/init.gradle.kts`) accesses `project` at execution time, which Gradle bans when `org.gradle.configuration-cache=true`. This is a bug in the android CLI init script.

**Workaround** — run the underlying tasks directly:
```bash
./gradlew --no-configuration-cache \
  --init-script .gradle/init.gradle.kts \
  :app:dumpAndroidProjectModel \
  :tosk:dumpAndroidProjectModel \
  :toskdemo:dumpAndroidProjectModel
```

Output JSON files are written to `<module>/build/AndroidProject.json`.

## Bundled Dictionary Database

The app ships a pre-built SQLite database at `app/src/main/assets/dictionary.db`, loaded by Room with the bundled SQLite driver (FTS5). Because `createFromAsset` is incompatible with a custom driver, `DictionaryDatabaseFactory` copies the asset into the app's database directory on first launch. The database is generated by the `words/` Python pipeline.

### When to regenerate

Rebuild the database whenever:
- The `words/` pipeline data changes (new entries, schema update)
- `EntryEntity` or `DictionaryDatabase` change (the Room identity hash changes)

### How to regenerate

**Step 1 — Get the Room identity hash**

Build the app once so KSP generates the Room schema export:

```bash
cd android/CheatShqip
./gradlew :app:kspDebugKotlin
```

Read the `identityHash` field from the generated schema:

```bash
cat app/schemas/com.cheatshqip.adapter.output.DictionaryDatabase/2.json \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['identityHash'])"
```

**Step 2 — Build the database**

```bash
cd words
uv run dict-build run \
  --sources-dir /path/to/sources \
  --out-dir /tmp/dict \
  --emit sqlite \
  --room-identity-hash <hash-from-step-1>
```

`--sources-dir` defaults to `/tmp/sources` (or `$DICT_SOURCES_DIR`), which is empty unless you've populated it — omitting it fails with `FileNotFoundError: uniparser/paradigms.txt`. It must contain:
- `uniparser/paradigms.txt` and `uniparser/sqi_lexemes_{A,N,V,rest}.txt`
- `kaikki-Albanian.jsonl` **at the sources-dir root** (not under a `kaikki/` subfolder)

See `words/AGENTS.md` ("Verify Against Real Data") for how to obtain these.

**Step 3 — Place the database as an asset**

```bash
cp /tmp/dict/dictionary.db \
   android/CheatShqip/app/src/main/assets/dictionary.db
```

### What the hash does

Room's `createFromAsset` validates that the bundled database was built for the exact same schema version by comparing the `identity_hash` column in `room_master_table` against a hash derived from the current `@Entity`/`@Database` annotations. If they don't match, the app crashes on first launch. The hash only changes when `EntryEntity` or `DictionaryDatabase` change — updating the dictionary data alone does not change it.

