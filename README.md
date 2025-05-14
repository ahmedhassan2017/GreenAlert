# Green Alert

Green Alert is a modern Android application focused on agricultural land reclamation. It provides information about agricultural land, ideal cultivation percentages, warnings, and tips for farmers. The app also allows users to activate and track agricultural processes, with reminders and a process history.

---

## Features

- **Splash Screen:** Modern splash with app logo and name.
- **Language Selection:** Choose between Arabic and English on first launch. Full RTL support for Arabic.
- **Home Screen:** List of agricultural indicators (soil types, irrigation, etc.), each with details.
- **Details Screen:**
  - Shows indicator details, ideal cultivation percentage, warnings, and tips.
  - "Activate Process" button: lets users register a process (name + date) and schedules a reminder notification for 90 days later.
- **Process List:** View all registered processes, sorted by date.
- **Local Notifications:** Reminds users to repeat a process after 90 days.
- **Localization:** All content is available in both Arabic and English, with proper layout direction.
- **Modern UI:** Material Design, custom fonts (Articulate for English, Cairo for Arabic), and responsive layouts.

---

## Tech Stack

- **IDE:** Android Studio (Giraffe or newer recommended)
- **Programming Language:** Kotlin
- **UI Layouts:** XML (Material Design components)
- **Architecture:**
  - MVVM-inspired, with separation of UI, data, and logic
  - Room Database for local storage
  - WorkManager for background notifications
  - ViewBinding for type-safe UI
- **Libraries Used:**
  - [AndroidX Core, AppCompat, ConstraintLayout, RecyclerView](https://developer.android.com/jetpack/androidx)
  - [Material Components](https://material.io/develop/android)
  - [Room Database](https://developer.android.com/jetpack/androidx/releases/room)
  - [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
  - [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

## Project Structure

```
app/
├── src/main/java/com/example/greenalert/
│   ├── MainActivity.kt
│   ├── SplashActivity.kt
│   ├── DetailsActivity.kt
│   ├── AboutActivity.kt
│   ├── LanguageSelectionActivity.kt
│   ├── ProcessListActivity.kt
│   ├── ProcessListAdapter.kt
│   ├── ProcessReminderWorker.kt
│   ├── data/
│   │   ├── AppDatabase.kt
│   │   ├── ProcessDao.kt
│   │   └── ProcessEntity.kt
│   └── ...
├── res/
│   ├── layout/ (XML layouts)
│   ├── values/ (strings, colors, themes)
│   ├── values-ar/ (Arabic translations)
│   ├── font/ (Cairo and Articulate font files)
│   └── drawable/ (icons, logo)
├── AndroidManifest.xml
└── build.gradle.kts
```

---

## Setup & Running

1. **Clone the repository** and open in Android Studio.
2. **Fonts:** Ensure `font/cairo.ttf` and `font/articulate.ttf` (or their variants) are present in `res/font/`.
3. **Build the project** (Android Studio will handle Gradle sync and dependencies).
4. **Run on a device or emulator** (Android 8.0+ recommended).
5. **Permissions:**
   - On Android 13+, the app will request notification permission at runtime.
6. **First Launch:**
   - You will be prompted to select a language (Arabic or English).
   - The app will restart and display content in the selected language and layout direction.

---

## Usage

- **Browse indicators** on the Home screen.
- **Tap an indicator** to view details, warnings, and tips.
- **Activate a process** from the Details screen:
  - Enter a process name and select a date.
  - The process is saved and a notification is scheduled for 90 days later.
- **View all processes** from the main menu ("Process List").
- **Switch language** at any time from the main menu.

---

## Customization

- **Add more indicators:** Edit the static data in `MainActivity.kt`.
- **Change logo or colors:** Update resources in `res/drawable/` and `res/values/colors.xml`.
- **Add more languages:** Add new `values-xx/strings.xml` folders and translations.

---

## License

This project is for educational and demonstration purposes. Please contact the author for production/commercial use. 