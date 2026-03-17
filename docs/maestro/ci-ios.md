# CI — iOS (Future)

> **Prerequisite**: iOS support requires a cross-platform rewrite (e.g. Kotlin Multiplatform)
> since the current network layer (Retrofit/OkHttp) and ML Kit adapter are Android-specific.

## Key differences from Android

| | Android | iOS |
|---|---|---|
| CI runner | `ubuntu-latest` | `macos-15` (simulators require macOS) |
| Device boot | `reactivecircus/android-emulator-runner` | `xcrun simctl boot "iPhone 16"` |
| WireMock host | `10.0.2.2:9090` | `localhost:9090` |
| Matrix axis | API level (24, 30, 35) | iOS version (16.4, 17.5, 18.0) |
| App install | `adb install` | `xcrun simctl install booted <app.app>` |
| `appId` in flows | `com.cheatshqip` | iOS bundle ID |

## Workflow skeleton

```yaml
# .github/workflows/e2e-ios.yml
name: E2E iOS

on: [push, pull_request]

jobs:
  maestro-ios:
    runs-on: macos-15
    strategy:
      fail-fast: false
      matrix:
        ios-version: ["16.4", "17.5", "18.0"]

    steps:
      - uses: actions/checkout@v4

      - name: Boot simulator
        run: |
          UDID=$(xcrun simctl create "iPhone 16" \
            "com.apple.CoreSimulator.SimDeviceType.iPhone-16" \
            "com.apple.CoreSimulator.SimRuntime.iOS-${{ matrix.ios-version }}")
          xcrun simctl boot "$UDID"
          echo "SIMULATOR_UDID=$UDID" >> $GITHUB_ENV

      - name: Build & install app
        run: |
          xcodebuild -scheme CheatShqip -configuration MockDebug \
            -destination "id=$SIMULATOR_UDID" \
            -derivedDataPath build/ build
          xcrun simctl install "$SIMULATOR_UDID" \
            build/Build/Products/MockDebug-iphonesimulator/CheatShqip.app

      - name: Start WireMock
        run: |
          java -jar .wiremock/wiremock-standalone.jar \
            --port 9090 --root-dir .wiremock &
          for i in $(seq 1 10); do
            curl -sf http://localhost:9090/__admin/health && break
            sleep 1
          done

      - name: Run Maestro flows
        run: maestro test .maestro/ --device "$SIMULATOR_UDID"

      - name: Upload Maestro report on failure
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: maestro-report-ios${{ matrix.ios-version }}
          path: ~/.maestro/tests/
```

## Flow compatibility

Maestro flow files are platform-agnostic for interactions (`tapOn`, `inputText`,
`assertVisible`, etc.). The only required change per platform is `appId`. A shared
flows directory with platform-specific entry points is one way to manage this:

```
.maestro/
  flows/      # shared interaction steps (included via runFlow)
  android/    # appId: com.cheatshqip
  ios/        # appId: com.example.cheatshqip
```