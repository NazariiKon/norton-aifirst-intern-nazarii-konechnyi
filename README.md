# Scam Message Detector

An Android application that helps users assess potentially suspicious SMS messages, emails, and URLs. The app uses structured LLM analysis to classify submitted content as **Safe**, **Suspicious**, or **Dangerous**, with a confidence score and an explanation of the signals behind the result.

> **Disclaimer:** This project provides an AI-assisted risk assessment, not a definitive fraud or security verdict. Users should avoid sharing sensitive personal, financial, or authentication information in the app.

## Demo

[Watch the demo video](https://youtu.be/vPtWdLEZ9Uo)

## Screenshots

<p align="center">
  <img src="screenshots/1.png" width="250" alt="Message input screen" />
  <img src="screenshots/2.png" width="250" alt="Example scam-message scenarios" />
  <img src="screenshots/3.png" width="250" alt="Risk assessment result" />
</p>

## Features

- Analyse SMS messages, emails, and URLs for potential scam indicators.
- Classify content as Safe, Suspicious, or Dangerous.
- Return a confidence score and a human-readable explanation.
- Provide realistic example scenarios for quick testing.
- Enforce a 2,000-character input limit to keep requests bounded.
- Handle long model explanations in a scrollable result view.
- Keep API credentials out of source control through `local.properties` and `BuildConfig`.
- Test ViewModel state transitions with coroutine-based unit tests.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Architecture:** MVVM, Hilt dependency injection
- **Networking:** Retrofit
- **AI integration:** Groq LLM API
- **Testing:** JUnit, Mockito, Kotlin Coroutines Test
- **Build system:** Gradle

## How It Works

1. The user enters or pastes a message, email, or URL.
2. The ViewModel validates the input and sends it to the analysis layer.
3. The app calls the Groq API with a constrained prompt that requests structured JSON only.
4. The response is parsed into a `ScamAnalysisResult` containing:
   - `riskLevel`
   - `confidenceScore`
   - `explanation`
5. The UI displays the assessment using a colour-coded result state.

The structured response format keeps the UI independent from free-form model output and makes the result easier to validate and render consistently.

## Architecture Notes

The project separates UI state, business logic, and API integration:

```text
Compose UI
    ↓
ScamDetectorViewModel
    ↓
Scam Analyzer / Repository
    ↓
Retrofit Client
    ↓
Groq LLM API
```

Key implementation decisions:

- `ScamDetectorViewModel` owns input, loading, success, and error states.
- `ScamDetectorUiState` is marked `@Immutable` to avoid unnecessary Compose recompositions.
- The LLM is prompted to return JSON with a fixed schema rather than unrestricted text.
- Retrofit uses a 60-second read timeout to handle slower model responses gracefully.
- API keys are loaded from `local.properties` rather than committed to the repository.

## Getting Started

### Prerequisites

- Android Studio Koala or later
- Android device or emulator running API 26+
- A Groq API key

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/NazariiKon/norton-aifirst-intern-nazarii-konechnyi.git
   cd <your-repository>
   ```

2. Open the project in Android Studio.

3. Add your Groq API key to the root `local.properties` file:

   ```properties
   GROQ_API_KEY=your_key_here
   ```

4. Sync Gradle and run the app on an Android device or emulator.

### Run Tests

```bash
./gradlew test
```

## Testing

The project includes ViewModel unit tests that verify UI-state changes during analysis. Tests use `StandardTestDispatcher` and `advanceUntilIdle()` to execute coroutine work deterministically.

The tested flow covers:

- Successful analysis updates.
- Loading-state transitions.
- Result rendering state.
- Validation of input constraints.

## Security and Reliability

- API credentials are kept in `local.properties` and exposed through `BuildConfig` rather than hard-coded in source files.
- The app limits submitted text to 2,000 characters.
- The structured JSON response is designed to reduce parsing errors caused by free-form LLM output.
- Network calls use explicit timeout handling.
- The application should not be used to process passwords, card details, one-time codes, or other sensitive data.

## Roadmap

- Add local rule-based checks for common phishing patterns before calling the LLM.
- Add Room persistence for scan history.
- Add share/paste integration from other Android applications.
- Add clearer error states for invalid API configuration and network failures.
- Add accessibility improvements and broader test coverage.
