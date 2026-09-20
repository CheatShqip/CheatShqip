#!/bin/bash
set -e

TMP_DIR=""
cleanup() {
  if [ -n "$TMP_DIR" ] && [ -d "$TMP_DIR" ]; then
    rm -rf "$TMP_DIR"
  fi
}
trap cleanup EXIT

if [ -z "$KEYSTORE_PATH" ] || [ -z "$KEYSTORE_PASSWORD" ] || [ -z "$KEY_ALIAS" ] || [ -z "$KEY_PASSWORD" ]; then
  echo "🔑 Fetching signing credentials from Dashlane..."
  TMP_DIR=$(mktemp -d)
  KEYSTORE_PATH="$TMP_DIR/cheatshqipstore.p12"
  dcli note title=cheatshqipstore_base64 | base64 --decode > "$KEYSTORE_PATH"
  if [ ! -s "$KEYSTORE_PATH" ]; then
    echo "❌ Decoded keystore is empty"
    exit 1
  fi
  CREDS=$(dcli note title=cheatshqipstore_credentials)
  KEYSTORE_PASSWORD=$(echo "$CREDS" | jq -r '.KEYSTORE_PASSWORD')
  KEY_ALIAS=$(echo "$CREDS" | jq -r '.KEY_ALIAS')
  KEY_PASSWORD=$(echo "$CREDS" | jq -r '.KEY_PASSWORD')
fi

if [ -z "$KEYSTORE_PATH" ] || [ -z "$KEYSTORE_PASSWORD" ] || [ -z "$KEY_ALIAS" ] || [ -z "$KEY_PASSWORD" ]; then
  echo "❌ Missing signing credentials"
  echo "Usage: KEYSTORE_PATH=... KEYSTORE_PASSWORD=... KEY_ALIAS=... KEY_PASSWORD=... ./scripts/release.sh"
  exit 1
fi

if [ ! -f "$HOME/.config/gcloud/application_default_credentials.json" ]; then
  echo "❌ Missing Application Default Credentials"
  echo "Run first: gcloud auth application-default login --impersonate-service-account=fastlane-deploy@cheatshqip.iam.gserviceaccount.com"
  exit 1
fi

export KEYSTORE_PATH KEYSTORE_PASSWORD KEY_ALIAS KEY_PASSWORD

echo "🔨 Building release AAB..."
./gradlew :app:bundleRelease --no-daemon

echo "🚀 Uploading to Google Play Store..."
fastlane release
