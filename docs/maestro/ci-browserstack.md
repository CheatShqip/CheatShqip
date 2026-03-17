# CI — BrowserStack App Automate

Use when real device coverage is needed (OEM-specific bugs, manufacturer skins, etc.).
BrowserStack App Automate supports Maestro flows natively via their REST API.

## WireMock accessibility

BrowserStack devices run on their cloud infrastructure — they cannot reach `localhost` or
`10.0.2.2` on the CI runner. Two things are required:

**1. BrowserStack Local tunnel** — exposes the CI runner's services to BrowserStack's cloud:
```bash
./BrowserStackLocal --key $BROWSERSTACK_ACCESS_KEY --daemon start
```

**2. Update the WireMock base URL** in the mock flavor from `10.0.2.2` to `bs-local.com`:
- App network config: `http://bs-local.com:9090/`
- WireMock scripts (`wiremock-reset.js`, `wiremock-stub-error.js`): replace
  `localhost:9090` with `bs-local.com:9090`

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

      - name: Build mockDebug APK
        run: ./gradlew assembleMockDebug

      - name: Start WireMock
        run: |
          java -jar .wiremock/wiremock-standalone.jar \
            --port 9090 --root-dir .wiremock &
          for i in $(seq 1 10); do
            curl -sf http://localhost:9090/__admin/health && break
            sleep 1
          done

      - name: Start BrowserStack Local tunnel
        run: |
          curl -sO https://www.browserstack.com/browserstack-local/BrowserStackLocal-linux-x64.zip
          unzip -q BrowserStackLocal-linux-x64.zip
          ./BrowserStackLocal --key $BROWSERSTACK_ACCESS_KEY --daemon start

      - name: Upload APK to BrowserStack
        run: |
          APP_URL=$(curl -su "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
            -F "file=@app/build/outputs/apk/mock/debug/app-mock-debug.apk" \
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

      - name: Stop BrowserStack Local tunnel
        if: always()
        run: ./BrowserStackLocal --key $BROWSERSTACK_ACCESS_KEY --daemon stop
```

## Trade-offs vs GitHub Actions matrix

| | GitHub Actions matrix | BrowserStack App Automate |
|---|---|---|
| Cost | Free | Paid |
| Real devices | No (emulators) | Yes |
| WireMock setup | Works out of the box | Requires Local tunnel + URL changes |
| Device variety | API levels only | Manufacturer / OS / model matrix |