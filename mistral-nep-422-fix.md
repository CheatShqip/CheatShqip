# Android Studio NEP → Mistral API: 422 Fix

## Problem

Android Studio's Next Edit Prediction (NEP) feature sends requests using the OpenAI Chat Completions API format. Two fields in these requests are OpenAI-specific and rejected by Mistral with HTTP 422:

### 1. `role: "developer"` (primary cause)

OpenAI introduced the `"developer"` role in late 2024 as a replacement for `"system"` in o1/o3 models. Mistral's API only accepts `"system"`, `"user"`, and `"assistant"` roles. Sending `"developer"` causes an unprocessable entity error.

```json
// What Android Studio sends (invalid for Mistral)
{ "role": "developer", "content": "..." }

// What Mistral expects
{ "role": "system", "content": "..." }
```

### 2. `stream_options: { "include_usage": true }` (secondary cause)

Also an OpenAI-specific extension. Mistral may reject or ignore it.

```json
// Sent by Android Studio, not supported by Mistral
"stream_options": { "include_usage": true }
```

## Solution

Run a local proxy that rewrites the request before it reaches Mistral, and point Android Studio's API base URL to `http://localhost:8080`.

### proxy.py

```python
import httpx
from fastapi import FastAPI, Request
from fastapi.responses import StreamingResponse

app = FastAPI()
MISTRAL_BASE = "https://api.mistral.ai"

@app.post("/v1/chat/completions")
async def proxy(request: Request):
    body = await request.json()

    for msg in body.get("messages", []):
        if msg.get("role") == "developer":
            msg["role"] = "system"

    body.pop("stream_options", None)

    async def stream():
        async with httpx.AsyncClient() as client:
            async with client.stream(
                "POST",
                f"{MISTRAL_BASE}/v1/chat/completions",
                json=body,
                headers={"Authorization": request.headers.get("Authorization", "")},
                timeout=60,
            ) as r:
                async for chunk in r.aiter_bytes():
                    yield chunk

    return StreamingResponse(stream(), media_type="text/event-stream")
```

```bash
pip install fastapi uvicorn httpx
uvicorn proxy:app --port 8080
```

Then set the Mistral base URL in Android Studio to `http://localhost:8080`.

## Root Cause Summary

Android Studio's NEP client targets the OpenAI API spec, which has diverged from Mistral's implementation on the `role` field enum. This is not a bug in the user's configuration — it is an incompatibility between the IDE's hardcoded request format and Mistral's API validation.
