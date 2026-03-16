#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCREENSHOTS_DIR="$SCRIPT_DIR/screenshots"
BASELINES_DIR="$SCREENSHOTS_DIR/baselines"
ACTUAL_DIR="$SCREENSHOTS_DIR/actual"
DIFFS_DIR="$SCREENSHOTS_DIR/diffs"
MAGICK="/opt/homebrew/bin/magick"
SCREENSHOT_THRESHOLD="${SCREENSHOT_THRESHOLD:-100}"
WIREMOCK_PORT="${WIREMOCK_PORT:-9090}"
UPDATE_BASELINES=false
WIREMOCK_PID=""

for arg in "$@"; do
  if [[ "$arg" == "--update-baselines" ]]; then
    UPDATE_BASELINES=true
  fi
done

WIREMOCK_JAR="$SCRIPT_DIR/../.wiremock/wiremock-standalone.jar"

if [[ ! -f "$WIREMOCK_JAR" ]]; then
  echo "ERROR: WireMock JAR not found at $WIREMOCK_JAR"
  exit 1
fi

enable_demo_mode() {
  adb shell settings put global sysui_demo_allowed 1
  adb shell am broadcast -a com.android.systemui.demo -e command enter
  adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1200
  adb shell am broadcast -a com.android.systemui.demo -e command battery -e level 100 -e plugged false
  adb shell am broadcast -a com.android.systemui.demo -e command network -e wifi show -e level 4
  adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false
}

disable_demo_mode() {
  adb shell am broadcast -a com.android.systemui.demo -e command exit || true
}

cleanup() {
  disable_demo_mode
  [[ -n "${WIREMOCK_PID:-}" ]] && kill "$WIREMOCK_PID" 2>/dev/null || true
}
trap cleanup EXIT

mkdir -p "$ACTUAL_DIR" "$DIFFS_DIR"

echo "Starting WireMock on port $WIREMOCK_PORT..."
java -jar "$WIREMOCK_JAR" --port "$WIREMOCK_PORT" --root-dir "$SCRIPT_DIR/../.wiremock" &
WIREMOCK_PID=$!

echo "Waiting for WireMock to be ready..."
for i in $(seq 1 20); do
  curl -s "http://localhost:${WIREMOCK_PORT}/__admin/mappings" > /dev/null 2>&1 && break
  sleep 0.5
done

echo "Enabling Android demo mode..."
enable_demo_mode

echo "Building and installing mock APK..."
cd "$SCRIPT_DIR/.." && ./gradlew installMockDebug

echo "Running Maestro flows..."
maestro test "$SCRIPT_DIR/"

echo "Disabling Android demo mode..."
disable_demo_mode

if [[ "$UPDATE_BASELINES" == true ]]; then
  echo "Updating baselines..."
  cp "$ACTUAL_DIR/"*.png "$BASELINES_DIR/" 2>/dev/null || true
  echo "Baselines updated in $BASELINES_DIR"
  exit 0
fi

echo "Diffing screenshots against baselines..."
FAILURES=0

for actual in "$ACTUAL_DIR"/*.png; do
  [[ -f "$actual" ]] || continue
  name="$(basename "$actual")"
  baseline="$BASELINES_DIR/$name"
  diff_out="$DIFFS_DIR/$name"

  if [[ ! -f "$baseline" ]]; then
    echo "MISSING BASELINE: $name — run with --update-baselines to create it"
    FAILURES=$((FAILURES + 1))
    continue
  fi

  pixel_diff=$("$MAGICK" compare -metric AE "$baseline" "$actual" "$diff_out" 2>&1 || true)

  if ! [[ "$pixel_diff" =~ ^[0-9]+$ ]]; then
    echo "WARN: unexpected output from magick compare for $name: $pixel_diff"
    pixel_diff=0
  fi

  if [[ "$pixel_diff" -gt "$SCREENSHOT_THRESHOLD" ]]; then
    echo "FAIL: $name — $pixel_diff pixels differ (threshold: $SCREENSHOT_THRESHOLD). Diff: $diff_out"
    FAILURES=$((FAILURES + 1))
  else
    echo "PASS: $name — $pixel_diff pixels differ"
  fi
done

if [[ "$FAILURES" -gt 0 ]]; then
  echo ""
  echo "$FAILURES screenshot test(s) failed."
  exit 1
fi

echo ""
echo "All screenshot tests passed."
