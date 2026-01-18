# 🚀 Feature Flag Backend

A **backend feature flag platform** supporting **environment isolation**, **rule-based targeting**, and **percentage-based rollout** — designed to power SDKs and dynamic feature control without redeployments.

---

## ✨ What this does

This service allows teams to:

* Create feature flags per environment (DEV / STAGING / PROD)
* Enable or disable features instantly
* Gradually roll out features using percentage-based rollout
* Target features using rule-based conditions
* Evaluate features dynamically at runtime

All **decision logic lives on the backend**.

---

## 🧠 How feature evaluation works

Feature evaluation follows this order:

```
Global Toggle → Percentage Rollout → Rules → Final Decision
```

If any step fails, the feature is disabled for that request.

---

## 🔌 Evaluation API

```
POST /api/features/{key}/evaluate?env=STAGING
```

**Request**

```json
{
  "attributes": {
    "userId": "user-123",
    "country": "IND"
  }
}
```

**Response**

```json
true | false
```

---

## ✅ Key Features

* Environment-scoped feature flags
* Rule-based targeting (e.g. country, user attributes)
* Deterministic percentage-based rollout
* Centralized evaluation engine
* Safe runtime evaluation (no redeploys)

---

## 🧩 Why SDKs?

While this backend provides full evaluation logic, direct API usage is not developer-friendly at scale.

SDKs (built separately) will:

* Provide a simple `isEnabled()` API
* Hide HTTP and error handling
* Cache results safely
* Ensure consistent behavior across services

> **Backend is the brain. SDKs are thin clients.**

---

## 🛣️ Roadmap

* Rollout percentage update API
* Java SDK (followed by other languages)
* Caching & observability
* Admin UI

---

## 📦 Scope

This repository contains **backend logic only**.
SDKs and client libraries live in **separate repositories**.

---

## 📄 License

(To be added)

