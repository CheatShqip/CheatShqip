# CI — GitHub Actions (Android)

## Recommended: API-level matrix

Each matrix job gets an isolated emulator, so flows run in parallel at no extra cost.

```yaml
# .github/workflows/e2e.yml
name: E2E

on: [push, pull_request]

jobs:
  maestro:
    runs-on: ubuntu-latest
    strategy:
      fail-fast: false
      matrix:
        api-level: [24, 30, 35]

    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 25

      - uses: gradle/actions/setup-gradle@v4

      - name: Build debug APK
        run: ./gradlew assembleDebug

      - uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: ${{ matrix.api-level }}
          arch: x86_64
          profile: pixel_6
          script: |
            adb install -r app/build/outputs/apk/debug/app-debug.apk
            maestro test .maestro/

      - name: Upload Maestro report on failure
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: maestro-report-api${{ matrix.api-level }}
          path: ~/.maestro/tests/
```

### Key points

- `fail-fast: false` — all API levels run even if one fails, giving a full picture.
- The app is fully offline: no network service is needed, so flows run against the plain
  `debug` APK with no WireMock setup.
- The APK is built once per job; caching via `gradle/actions/setup-gradle` avoids
  redundant compilation across the matrix.
- Artifacts are uploaded on failure so Maestro's HTML reports are inspectable in CI.

## Alternative: local parallel run

For quick local smoke tests across multiple running emulators:

```bash
#!/usr/bin/env bash
for device in $(adb devices | grep emulator | awk '{print $1}'); do
  maestro test .maestro/ --device "$device" &
done
wait
```

Requires one emulator per target API level to be running. Since the app is offline,
no host service needs to run.