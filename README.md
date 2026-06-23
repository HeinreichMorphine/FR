# Flood Rescue (FR)

Flood Rescue (FR) is a modern Android mobile application designed to support coordination, communication, and response actions during flooding emergencies. 

The application is built using **Kotlin**, **Jetpack Compose**, and **Material 3 Design Guidelines**, providing a responsive and modern interface optimized for mobile devices.

---

## Features

- **Authentication System**: Includes user registration and login flows.
  - Toggled authentication view (`AuthScreen`) for ease of access.
  - Form validation ready components (`LoginScreen`, `RegisterScreen`).
- **Dynamic Routing**: Configured navigation using Jetpack Compose Navigation controller.
- **Home Dashboard**: Welcomes users upon successful authentication.
- **Modern Theme Support**: Equipped with dark/light mode configurations built around Material 3 components.

---

## Current Application Functionality

Here is the breakdown of what the application currently does and how it behaves:

1. **Authentication Screen (`AuthScreen.kt`)**:
   - Renders a form asking for **Email** and **Password**.
   - Contains a toggle button (`Go to Register` / `Go to Login`) that switches the screen title and the primary CTA button text dynamically.
   - Text inputs utilize Jetpack Compose mutable states (`email`, `password`, `isLogin`) to update the UI on keypress.
   - The password field utilizes `PasswordVisualTransformation` to obscure input.
   - *Note*: Clicking the login/register button currently has no action associated with it (empty callback), meaning it does not transition yet.
2. **Alternative Screen Templates (`LoginScreen.kt` & `RegisterScreen.kt`)**:
   - The codebase has isolated template screens for `LoginScreen` and `RegisterScreen` with parameters/callbacks for standard inputs (Email, Password, Name).
   - These are currently placeholders and are not configured inside the navigation host.
3. **Home Screen (`HomeScreen.kt`)**:
   - Displays a welcoming layout with a header text stating `"Welcome, {name}!"`.
   - The name is dynamically passed as a string parameter from the navigation controller.
4. **App Navigation (`Navigation.kt`)**:
   - Initiates with the navigation controller starting at the `"auth"` route.
   - Declares the home screen route as `"home/{name}"` which expects a string parameter.

---

## Project Structure

The project follows a standard modern Android structure with single-module architecture (`:app`):

```text
FR/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/fr/
│   │   │   │   ├── MainActivity.kt        # Application Entry Point
│   │   │   │   ├── ui/
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   └── Navigation.kt  # App Navigation Controller
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── AuthScreen.kt  # Toggled Login/Register Form
│   │   │   │   │   │   ├── LoginScreen.kt # Modular Login Composable
│   │   │   │   │   │   ├── RegisterScreen.kt # Modular Register Composable
│   │   │   │   │   │   └── HomeScreen.kt  # User Landing Screen
│   │   │   │   │   └── theme/             # M3 Color, Shape, and Typography definitions
│   │   │   └── res/                       # App Icons and Values Resources
│   │   └── test/                          # Unit and Instrumentation Tests
│   └── build.gradle.kts                   # App-level dependencies
│
├── gradle/                                # Gradle Version Catalog (libs.versions.toml)
└── settings.gradle.kts                    # Root build settings
```

---

## Tech Stack & Dependencies

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (using Compose BOM)
- **Design System**: Material Design 3 (M3)
- **Navigation**: Jetpack Compose Navigation (`androidx.navigation:navigation-compose`)
- **Build Tool**: Gradle (Kotlin DSL)
- **Minimum SDK**: API 24 (Android 7.0)
- **Target/Compile SDK**: API 36 (Android 16)

---

## Getting Started

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (Jellyfish or newer recommended)
- Java Development Kit (JDK) 11 or later

### Installation & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/HeinreichMorphine/FR.git
   cd FR
   ```
2. Open the project in Android Studio.
3. Allow Gradle to sync dependencies.
4. Select a virtual emulator or connect a physical Android device.
5. Click **Run** (`Shift + F10`) or execute the build via Gradle CLI:
   ```bash
   ./gradlew assembleDebug
   ```

---

## Future Enhancements
- [ ] Connect Authentication buttons to a backend API or Firebase Authentication.
- [ ] Implement real-time geo-location sharing for flood victims and rescuers.
- [ ] Integrate a map interface to pin rescue requests and flood boundaries.
- [ ] Add offline support/caching for emergency contact information.
