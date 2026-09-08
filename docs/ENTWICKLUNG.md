# MACH. entwickeln und bauen

Das Repository enthält nur die Android-App. Die Oberfläche unter `ui/` ist Bestandteil der APK und keine separat veröffentlichte Website.

## Architektur

- `app/`: native Java-Komponenten, Android-Ressourcen, Tests und die eingebettete Offline-Oberfläche
- `ui/`: React-/TypeScript-Quellen der APK-Oberfläche
- `app/src/main/assets/site/index.html`: vollständig gebündelte Oberfläche, die die WebView offline lädt
- `gradle/`, `gradlew`, `gradlew.bat`: reproduzierbarer Android-Build

Die lokale JavaScript-Brücke `MachAndroid` verbindet die Oberfläche mit Alarmplanung, NFC, QR-Scanner, App-Blocker, Nutzungsdaten und Fokus-Timer.

## Voraussetzungen

- JDK 17
- Android SDK Platform 35 und Build Tools 35.0.0
- Für UI-Änderungen: Node.js 22 und pnpm 11.19.0

## Oberfläche prüfen und einbetten

```powershell
cd ui
pnpm install --frozen-lockfile
pnpm exec tsc --noEmit
pnpm test
pnpm build
cd ..
```

`pnpm build` schreibt die gebündelte Offline-Datei direkt nach `app/src/main/assets/site/index.html`.

## Android-App prüfen und bauen

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

Ergebnis: `app/build/outputs/apk/debug/app-debug.apk`.

## Signierte APK

Für ein Update einer vorhandenen Installation muss derselbe private Signaturschlüssel verwendet werden. Der Build unterstützt die neuen `MACH_*`-Variablen und aus Kompatibilitätsgründen weiterhin die früheren `WACHWERK_*`-Namen:

| Variable | Bedeutung |
| --- | --- |
| `MACH_KEYSTORE` | Absoluter Pfad zur Keystore-Datei |
| `MACH_STORE_PASSWORD` | Keystore-Passwort |
| `MACH_KEY_ALIAS` | Schlüssel-Alias |
| `MACH_KEY_PASSWORD` | Schlüsselpasswort |

```powershell
.\gradlew.bat assembleRelease
```

Ergebnis: `app/build/outputs/apk/release/app-release.apk`.

NFC, Alarme bei gesperrtem Bildschirm und herstellerspezifisches Energiesparen müssen zusätzlich auf einem echten Android-Gerät geprüft werden.

[Zurück zur README](../README.md)
