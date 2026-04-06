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
if command -v magick > /dev/null 2>&1; then
  MAGICK_CONVERT="magick"
  MAGICK_COMPARE="magick compare"
else
  MAGICK_CONVERT="convert"
  MAGICK_COMPARE="compare"
fi
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
  echo "Waiting for SystemUI to be ready..."
  for i in $(seq 1 40); do
    adb shell pidof com.android.systemui > /dev/null 2>&1 && break
    sleep 0.5
  done

  echo "Disabling network interfaces..."
  adb shell su root svc wifi disable
  adb shell su root svc data disable

  echo "Fixing system date and time..."
  adb shell su root cmd time_detector set_auto_detection_enabled false
  ELAPSED_MS=$(adb shell cat /proc/uptime | awk '{printf "%d", $1*1000}')
  adb shell su root cmd time_detector suggest_manual_time --elapsed_realtime "$ELAPSED_MS" --unix_epoch_time 1735732800000
  adb shell su root date 010112002025

  echo "Fixing battery state..."
  adb shell su root dumpsys battery set level 100
  adb shell su root dumpsys battery set ac 0
  adb shell su root dumpsys battery set usb 0
  adb shell su root dumpsys battery set wireless 0
  adb shell su root dumpsys battery set status 4

  echo "Applying demo mode..."
  adb shell su root settings put system text_cursor_blink_rate 0

  # Enable demo mode
  adb shell su root settings put global sysui_demo_allowed 1

  # Enter demo mode with a clean status bar
  adb shell am broadcast -a com.android.systemui.demo -e command enter
  adb shell am broadcast -a com.android.systemui.demo -e command battery -e visible false -e plugged false
  adb shell am broadcast -a com.android.systemui.demo -e command network -e airplane hide
  adb shell am broadcast -a com.android.systemui.demo -e command network -e mobile hide
  adb shell am broadcast -a com.android.systemui.demo -e command network -e wifi hide
  adb shell am broadcast -a com.android.systemui.demo -e command network -e nosim hide
  adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false
  adb shell am broadcast -a com.android.systemui.demo -e command clock -e visible false
  adb shell am broadcast -a com.android.systemui.demo -e command status -e volume hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e bluetooth hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e location hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e alarm hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e sync hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e tty hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e eri hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e mute hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e speakerphone hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e usb hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e cdma_eri hide
  adb shell am broadcast -a com.android.systemui.demo -e command status -e managed_profile hide
  sleep 2
}

disable_demo_mode() {
  adb shell am broadcast -a com.android.systemui.demo -e command exit || true
  adb shell dumpsys battery reset || true
}

cleanup() {
  disable_demo_mode
  [[ -n "${WIREMOCK_PID:-}" ]] && kill "$WIREMOCK_PID" 2>/dev/null || true
}
trap cleanup EXIT

echo "Freeing port $WIREMOCK_PORT if already in use..."
lsof -ti:"${WIREMOCK_PORT}" | xargs -r kill -9 2>/dev/null || true

echo "Starting WireMock on port $WIREMOCK_PORT..."
java -jar "$WIREMOCK_JAR" --port "$WIREMOCK_PORT" --root-dir "$SCRIPT_DIR/../.wiremock" &
WIREMOCK_PID=$!

echo "Waiting for WireMock to be ready..."
wiremock_ready=false
for i in $(seq 1 20); do
  if curl -s "http://localhost:${WIREMOCK_PORT}/__admin/mappings" > /dev/null 2>&1; then
    wiremock_ready=true
    break
  fi
  sleep 0.5
done
if [[ "$wiremock_ready" != "true" ]]; then
  echo "ERROR: WireMock failed to start on port $WIREMOCK_PORT"
  exit 1
fi

echo "Waiting for Maestro driver to initialize..."
adb forward tcp:7001 tcp:7001 2>/dev/null || true
for i in $(seq 1 40); do
  nc -z localhost 7001 2>/dev/null && break
  sleep 0.5
done
adb forward --remove tcp:7001 2>/dev/null || true

echo "Enabling Android demo mode..."
enable_demo_mode

echo "Building and installing mock APK..."
cd "$SCRIPT_DIR/.." && ./gradlew installMockDebug

echo "Setting up ADB reverse tunnel for WireMock..."
adb reverse tcp:${WIREMOCK_PORT} tcp:${WIREMOCK_PORT}

echo "Running Maestro flows..."
maestro test "$SCRIPT_DIR/"

echo "Disabling Android demo mode..."
disable_demo_mode

mkdir -p "$ACTUAL_DIR" "$BASELINES_DIR" "$DIFFS_DIR"

echo "Organizing actual screenshots..."
mv "$SCREENSHOTS_DIR/"*.png "$ACTUAL_DIR/" 2>/dev/null || true

echo "Cropping system UI from screenshots..."
SCREEN_SIZE=$(adb shell wm size | grep -oE '[0-9]+x[0-9]+$')
SCREEN_W=$(echo "$SCREEN_SIZE" | cut -dx -f1)
SCREEN_H=$(echo "$SCREEN_SIZE" | cut -dx -f2)
ROTATION0=$(adb shell dumpsys window displays | grep "ROTATION_0=")
OVERRIDE=$(echo "$ROTATION0" | grep -o 'overrideNonDecorInsets=\[[^]]*\]\[[^]]*\]')
TOP_INSET=$(echo "$OVERRIDE" | cut -d',' -f2 | cut -d']' -f1)
BOTTOM_INSET=$(echo "$OVERRIDE" | cut -d',' -f3 | cut -d']' -f1)
CONTENT_H=$((SCREEN_H - TOP_INSET - BOTTOM_INSET))
echo "Screen: ${SCREEN_W}x${SCREEN_H}, top inset: ${TOP_INSET}, bottom inset: ${BOTTOM_INSET}, crop: ${SCREEN_W}x${CONTENT_H}+0+${TOP_INSET}"
for img in "$ACTUAL_DIR"/*.png; do
  [[ -f "$img" ]] || continue
  $MAGICK_CONVERT "$img" -crop "${SCREEN_W}x${CONTENT_H}+0+${TOP_INSET}" +repage "$img"
done

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

  pixel_diff=$($MAGICK_COMPARE -metric AE "$baseline" "$actual" "$diff_out" 2>&1 || true)

  pixel_diff=$(echo "$pixel_diff" | grep -oE '^[0-9]+' || echo "")
  if [[ -z "$pixel_diff" ]]; then
    echo "WARN: unexpected output from magick compare for $name, treating as failure"
    FAILURES=$((FAILURES + 1))
    continue
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
