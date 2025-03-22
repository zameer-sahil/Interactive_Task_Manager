# My Android App

## Requirements
- Android Studio Flamingo or later
- Kotlin 2.x
- Compose 1.6.x
- SDK 34/35

## Setup Instructions
1. Clone the repository from GitHub.
2. Open the project in Android Studio.
3. Sync Gradle and run the app.

## Design Rationale
- Uses **MVVM + Jetpack Compose** for modern UI.
- Uses **Room Database** for offline storage.
- Clean architecture for scalability.

- ## Databse Schema
- CREATE TABLE IF NOT EXISTS task_table (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    priority TEXT NOT NULL,
    dueDate TEXT,
    status TEXT

)

