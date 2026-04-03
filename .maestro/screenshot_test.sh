#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
GENERATED_DIR="$SCRIPT_DIR/generated"
SCREENSHOTS_DIR="$GENERATED_DIR/screenshots"
BASELINES_DIR="$GENERATED_DIR/baselines"
ACTUAL_DIR="$GENERATED_DIR/actual"
DIFFS_DIR="$GENERATED_DIR/diffs"
SCREENSHOT_THRESHOLD="${SCREENSHOT_THRESHOLD:-100}"
WIREMOCK_PORT="${WIREMOCK_PORT:-9090}"
UPDATE_BASELINES=false
SKIP_COMPARE=false
COMPARE_ONLY=false
WIREMOCK_PID=""

for arg in "$@"; do
  if [[ "$arg" == "--update-baselines" ]]; then
    UPDATE_BASELINES=true
  elif [[ "$arg" == "--skip-compare" ]]; then
    SKIP_COMPARE=true
  elif [[ "$arg" == "--compare-only" ]]; then
    COMPARE_ONLY=true
  fi
done

run_compare() {
  mkdir -p "$ACTUAL_DIR" "$BASELINES_DIR" "$DIFFS_DIR"
  echo "Diffing screenshots against baselines..."
  local failures=0

  for actual in "$ACTUAL_DIR"/*.png; do
    [[ -f "$actual" ]] || continue
    local name
    name="$(basename "$actual")"
    local baseline="$BASELINES_DIR/$name"
    local diff_out="$DIFFS_DIR/$name"

    if [[ ! -f "$baseline" ]]; then
      echo "MISSING BASELINE: $name — run with --update-baselines to create it"
      failures=$((failures + 1))
      continue
    fi

    local pixel_diff
    pixel_diff=$(compare -metric AE "$baseline" "$actual" "$diff_out" 2>&1 || true)
    pixel_diff=$(echo "$pixel_diff" | grep -oE '^[0-9]+' || echo "")

    if [[ -z "$pixel_diff" ]]; then
      echo "WARN: unexpected output from compare for $name, treating as failure"
      failures=$((failures + 1))
      continue
    fi

    if [[ "$pixel_diff" -gt "$SCREENSHOT_THRESHOLD" ]]; then
      echo "FAIL: $name — $pixel_diff pixels differ (threshold: $SCREENSHOT_THRESHOLD). Diff: $diff_out"
      failures=$((failures + 1))
    else
      echo "PASS: $name — $pixel_diff pixels differ"
    fi
  done

  if [[ "$failures" -gt 0 ]]; then
    echo ""
    echo "$failures screenshot test(s) failed."
    return 1
  fi

  echo ""
  echo "All screenshot tests passed."
}

if [[ "$COMPARE_ONLY" == true ]]; then
  run_compare
  exit $?
fi

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

mkdir -p "$ACTUAL_DIR" "$BASELINES_DIR" "$DIFFS_DIR"

echo "Organizing actual screenshots..."
mv "$SCREENSHOTS_DIR/"*.png "$ACTUAL_DIR/" 2>/dev/null || true

if [[ "$UPDATE_BASELINES" == true ]]; then
  echo "Updating baselines..."
  cp "$ACTUAL_DIR/"*.png "$BASELINES_DIR/" 2>/dev/null || true
  echo "Baselines updated in $BASELINES_DIR"
  exit 0
fi

if [[ "$SKIP_COMPARE" == true ]]; then
  echo "Skipping comparison (--skip-compare)."
  exit 0
fi

run_compare