# NavGurukulam Android App

## Architecture Overview

This app follows a **Clean + MVVM architecture** with separation of concerns:

- **UI Layer (Activity/Fragment + ViewModel):**  
  Handles UI logic and observes LiveData from repositories.

- **Repository Layer:**  
  Acts as a single source of truth. Handles data from **local Room database** and **remote API**. Provides methods for fetching, creating, updating, and deleting entities.

- **Database Layer (Room):**  
  Stores `Student` and `ScoreCard` entities locally for offline access. Each entity tracks `updatedAt` and `syncStatus` for synchronization.

- **Network Layer (Retrofit API):**  
  Handles server communication for syncing Students and ScoreCards.

- **Worker Layer (WorkManager):**  
  Handles background syncing tasks with retries when network is available.


---

## Offline-First & Sync Logic

1. **Offline Support:**
    - All CRUD operations are performed on the **local Room database** immediately.
    - Each entity has a `syncStatus` field (`SYNCED`, `PENDING`, `FAILED`) to track synchronization.

2. **Synchronization:**
    - **WorkManager** triggers a `SyncWorker` periodically or when the network is available.
    - Sync order is maintained: **Students are synced before their ScoreCards** to preserve foreign key integrity.
    - API responses update local entities with new `updatedAt` timestamps and change `syncStatus` to `SYNCED`.

---

## Conflict Resolution

- **Strategy:** Last-write-wins based on `updatedAt`.
- **Process:**
    - When syncing, compare `updatedAt` between local and remote versions.
    - If local record is newer, push to server.
    - If remote record is newer, update local database.
- Ensures data consistency even if multiple devices modify the same record offline.

---

## Chosen Patterns & Libraries

- **MVVM + Repository Pattern:**  
  Provides clean separation of concerns, easier testing, and reactive UI updates.

- **Room:**  
  Offline-first local storage with support for entities, relations, and type converters.

- **Retrofit + Coroutine + Gson/Moshi:**  
  Simple and reliable network layer for asynchronous API calls.

- **WorkManager:**  
  Handles background sync with guaranteed execution and retry support even after app restarts.

- **DiffUtil + Paging (optional):**  
  Efficiently updates RecyclerViews for large datasets.

- **Why these choices:**
    - MVVM + Repository + LiveData ensures scalable architecture.
    - Room + WorkManager combination provides a robust offline-first experience.
    - Coroutines allow clean asynchronous code without callbacks.

---

## How to Build & Run

1. Clone the repository.
2. Open in Android Studio with **Gradle Kotlin DSL** support.
3. Build and run on an emulator or device.
4. Test offline CRUD operations and observe automatic syncing when network becomes available.
