# CI — BrowserStack App Automate

Use when real device coverage is needed (OEM-specific bugs, manufacturer skins, etc.).
BrowserStack App Automate supports Maestro flows natively via their REST API.

## App accessibility

BrowserStack devices run on their cloud infrastructure. The app is fully offline
(screens, translations, and word details come from the bundled dictionary), so no host
service needs to be reachable — the `debug` APK works as-is.

## Workflow skeleton

```yaml
# .github/workflows/e2e-browserstack.yml
name: E2E BrowserStack

on: [push, pull_request]

jobs:
  browserstack:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 25

      - uses: gradle/actions/setup-gradle@v4

      - name: Build debug APK
        run: ./gradlew assembleDebug

      - name: Upload APK to BrowserStack
        run: |
          APP_URL=$(curl -su "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
            -F "file=@app/build/outputs/apk/debug/app-debug.apk" \
            https://api-cloud.browserstack.com/app-automate/upload \
            | jq -r '.app_url')
          echo "APP_URL=$APP_URL" >> $GITHUB_ENV

      - name: Run Maestro flows on BrowserStack
        run: |
          curl -su "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
            -X POST https://api-cloud.browserstack.com/app-automate/maestro/build \
            -H "Content-Type: application/json" \
            -d '{
              "app": "'"$APP_URL"'",
              "devices": ["Google Pixel 6-12.0", "Samsung Galaxy S23-13.0"],
              "flowFile": ".maestro/"
            }'
```

## Trade-offs vs GitHub Actions matrix

| | GitHub Actions matrix | BrowserStack App Automate |
|---|---|---|
| Cost | Free | Paid |
| Real devices | No (emulators) | Yes |
| Device variety | API levels only | Manufacturer / OS / model matrix |