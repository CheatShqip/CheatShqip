#!/usr/bin/env bash
set -euo pipefail

source .envrc

PROJECT_KEY=$(grep '^sonar.projectKey=' sonar-project.properties | cut -d'=' -f2-)
ORGANIZATION=$(grep '^sonar.organization=' sonar-project.properties | cut -d'=' -f2-)
HOST=$(grep '^sonar.host.url=' sonar-project.properties | cut -d'=' -f2-)

echo "Running SonarQube analysis..."
TASK_ID=$(sonar-scanner 2>&1 | tee /dev/stderr | grep 'api/ce/task?id=' | grep -o 'id=[^[:space:]]*' | cut -d'=' -f2)

if [[ -z "$TASK_ID" ]]; then
  echo "Error: could not extract task ID from sonar-scanner output" >&2
  exit 1
fi

echo ""
echo "Waiting for server to process analysis (task: $TASK_ID)..."
while true; do
  STATUS=$(curl -s -u "${SONAR_TOKEN}:" "${HOST}/api/ce/task?id=${TASK_ID}" | python3 -c "import json,sys; print(json.load(sys.stdin)['task']['status'])")
  echo "  Status: $STATUS"
  if [[ "$STATUS" == "SUCCESS" ]]; then break; fi
  if [[ "$STATUS" == "FAILED" || "$STATUS" == "CANCELLED" ]]; then
    echo "Analysis $STATUS" >&2; exit 1
  fi
  sleep 3
done

echo ""
echo "Fetching issues..."
curl -s -u "${SONAR_TOKEN}:" \
  "${HOST}/api/issues/search?projectKeys=${PROJECT_KEY}&organization=${ORGANIZATION}&ps=500&resolved=false" \
  | python3 -c "
import json, sys
data = json.load(sys.stdin)
issues = data.get('issues', [])
print(f'=== {len(issues)} open issue(s) ===')
print()
for i in issues:
    component = i.get('component', '').split(':')[-1]
    line = i.get('line', '?')
    severity = i.get('severity', 'UNKNOWN')
    msg = i.get('message', '')
    rule = i.get('rule', '')
    print(f'[{severity}] {component}:{line}')
    print(f'  Rule   : {rule}')
    print(f'  Message: {msg}')
    print()
"