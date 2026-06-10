# SML File Share

A peer-to-peer Android file sharing application for fast, offline file transfers between devices using WiFi Direct, hotspot connections, or QR codes.

- **Package:** `com.mrp.sml`
- **Platform:** Android (minSdk 26, targetSdk 36)
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3

---

## Features

### Device Discovery & Pairing
- **WiFi Direct** — Discover and connect to nearby peers
- **Google Nearby Connections** — Fallback discovery mechanism
- **Hotspot + QR Code** — Receiver opens LocalOnlyHotspot, displays a QR code; sender scans it to connect
- **QR Code Scanning** — CameraX + ZXing for real-time QR scanning

### File Transfer
- Send and receive any file type (images, videos, documents, APKs, etc.)
- Multiple file selection with system file picker
- Real-time progress: percentage, speed (MB/s), ETA, file index
- Pause, resume, and cancel in-flight transfers
- **AES-256-GCM encryption** per chunk using a session-derived key
- **SHA-256 verification** per file after transfer completes
- Chunked streaming (configurable chunk size: 512KB–32MB)
- Foreground service with persistent notification during transfers

### Transfer History
- Room database with transfer_history, paired_devices, and transfer_progress tables
- Filter by All / Sent / Received / Failed
- Transfer detail view (metadata, timestamps, file info)
- Retry failed transfers with exponential backoff (up to 3 attempts)
- Background cleanup of old records (30 days)

### Settings
- Device name configuration
- Dark mode toggle (persisted, applies system-wide)
- Theme color picker (6 preset colors)
- Chunk size configuration
- Save transfer history toggle
- Save location picker
- Network fallback (WiFi Direct → hotspot) toggle
- Permission management

### UI Screens
| Screen | Description |
|--------|-------------|
| Splash | Gradient splash screen → home or permissions |
| Permissions | Runtime permission requests (WiFi, location, notifications) |
| Home | 4 action cards (Send, Receive, History, Settings), connection status, last transfer summary |
| Send | File picker with validation (<5GB), file list with remove |
| Receive | QR code display, hotspot toggle, discovered device list |
| Discovery | Pairing role/method selection, peer list, QR display/scan |
| Transfer | Animated progress bar, speed, ETA, pause/cancel/resume/retry |
| Transfer Detail | Full transfer metadata and file info |
| History | Filtered list grouped by date, retry/open/clear |
| Settings | Device name, dark mode, theme, chunk size, save history, network fallback, permissions |
| QR Scanner | CameraX-based real-time QR scanning |
| QR Display | QR code with connection details card |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture (presentation/domain/data) |
| DI | Dagger Hilt |
| Database | Room (3 tables, 3 DAOs) |
| Preferences | DataStore (Preferences) |
| Navigation | Jetpack Compose Navigation |
| Networking | TCP sockets (ServerSocket/Socket), WiFi P2P API |
| Discovery | WiFi Direct, Google Nearby Connections, QR codes (ZXing) |
| Camera | CameraX (Camera2, Lifecycle, View) |
| Async | Kotlin Coroutines + StateFlow |
| Background | WorkManager, Foreground Service |
| Encryption | AES-256-GCM (per-chunk) |
| Integrity | SHA-256 (per-file) |
| Build | Gradle (Kotlin DSL + Version Catalog) |

---

## Architecture

```
app/src/main/java/com/mrp/sml/
├── core/               # Constants, models, extensions, utils
│   ├── constants/      # AppConstants, NetworkConstants, TransferConstants
│   ├── models/         # ConnectionState, Device, TransferFile, TransferProgress, TransferSession
│   ├── extensions/     # ContextExt, FlowExt
│   ├── permissions/    # PermissionManager
│   └── utils/          # FileUtils, QrCodeUtils, WifiUtils, TransferUtils, etc.
├── data/
│   ├── local/          # Room DB, DAOs, entities, DataStore
│   ├── remote/         # WiFi Direct, Nearby, Hotspot, TCP sockets
│   ├── repository/     # TransferRepositoryImpl, DeviceRepositoryImpl, ConnectionRepositoryImpl
│   └── mapper/         # DeviceMapper, TransferMapper
├── di/                 # Hilt modules (App, Database, Dispatcher, Network, Repository)
├── domain/
│   ├── model/          # DeviceModel, TransferModel
│   ├── repository/     # Interfaces
│   └── usecase/        # discovery/, transfer/, settings/
├── receivers/          # BootReceiver, WifiStateReceiver
├── services/           # DiscoveryService, TransferForegroundService, NotificationService
├── ui/
│   ├── components/     # DeviceCard, FileItem, SMLTopBar, TransferProgressCard
│   ├── navigation/     # Screens (sealed class), NavGraph
│   ├── screens/        # 12 screens (splash, home, send, receive, discovery, transfer, etc.)
│   ├── theme/          # Color, Theme, Typography (Material 3)
│   └── viewmodel/      # 9 ViewModels
└── workers/            # CleanupWorker, RetryTransferWorker
```

---

## Networking

### Discovery Priority
1. **WiFi Direct** — `WifiP2pManager.discoverPeers()` + `requestPeers()`
2. **Google Nearby** — `ConnectionsClient` advertising/discovery
3. **QR Code** — Encoded JSON payload with device info + hotspot credentials
4. **Hotspot** — `LocalOnlyHotspot` (API 26+) / `WifiNetworkSpecifier` (API 29+)

### Transfer Protocol
1. Handshake: metadata JSON → accept/reject
2. Per-file: FILE_START → CHUNKs (encrypted) → FILE_DONE
3. Completion: byte `8` signals all files received
4. Encryption: AES-256-GCM, key = SHA-256(session token), 12-byte nonce per chunk
5. Verification: SHA-256 hash compared after each file

---

## Build & Run

```bash
# Build all modules
./gradlew assembleDebug

# Run tests
./gradlew test

# Lint
./gradlew lint

# Clean build
./gradlew clean assembleDebug
```

Requires Android Studio Hedgehog (2023.1.1+) or newer with JDK 17.

---

## Permissions

| Permission | Purpose |
|-----------|---------|
| `ACCESS_FINE_LOCATION` | WiFi Direct peer discovery |
| `NEARBY_WIFI_DEVICES` (API 33+) | WiFi Direct (replacement for location) |
| `POST_NOTIFICATIONS` (API 33+) | Foreground transfer notification |
| `INTERNET` | Local socket communication |
| `ACCESS_WIFI_STATE` / `CHANGE_WIFI_STATE` | WiFi/hotspot management |
| `FOREGROUND_SERVICE` | Transfer foreground service |
| `RECEIVE_BOOT_COMPLETED` | Schedule background cleanup |

---

## Author

Developed by MRP
