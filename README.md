# Scam Message Detector 🛡️

An AI-powered Android application that detects scam messages (SMS, Emails, Phishing URLs) using Large Language Models (LLM) via the Groq API. Inspired by Norton Genie.

## 🚀 Features
- **AI Analysis**: Get real-time risk assessment (Safe, Suspicious, Dangerous).
- **Explanation**: Understand *why* a message is flagged as a scam.
- **Example Scams**: Tap on pre-defined scam examples to see how the detector works.
- **Clean Architecture**: Built using Domain-Driven Design (DDD) principles.

## 🛠 Tech Stack
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **DI**: Hilt (Dependency Injection)
- **Networking**: Retrofit & OkHttp
- **AI Engine**: Groq API (LLM)
- **Testing**: JUnit, Mockito, Kotlin Coroutines Test

---

## 🏁 How to Run the App

### 1. Prerequisites
- Android Studio Ladybug (or newer).
- An active internet connection (for API calls).

### 2. API Key Configuration
The app comes with a **pre-configured API key** in `Constants.kt` for convenience, so you can run it immediately. 

> **Note**: For production apps, never hardcode API keys. In a real scenario, this would be handled via backend services or secure secrets management.

### 3. Build and Launch
1. Connect your Android device or start an Emulator.
2. Press the **Run** button (green arrow) in Android Studio.
3. The app will install and open automatically.

---

## 🧪 How to Run Tests

### Unit Tests (Logic Only)
These tests check the business logic without making real network calls.
- **Via Android Studio**: Right-click on `app/src/test/java/.../domain/usecase/AnalyzeScamMessageUseCaseTest.kt` and select **Run**.
- **Via Terminal**:
  ```bash
  ./gradlew :app:testDebugUnitTest
  ```

### Integration Tests (Real API Calls)
These tests make actual calls to the Groq API to verify everything is working.
- **Via Android Studio**: Open `app/src/test/java/.../ApiIntegrationTest.kt` and click the play button next to the class name.
---

## 📂 Project Structure
- `domain/`: Business logic, Models, and Use Cases.
- `data/`: API implementation, Repository implementation, and DTOs.
- `presentation/`: ViewModel and UI State management.
- `ui/`: Jetpack Compose components and screens.
