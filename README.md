# MACH.

**Kein später.**

MACH. ist eine reine Android-App für Wecker, Aufwachaufgaben, Einschlaf-Erinnerungen, Fokus-Sessions, App-Sperren, To-dos und Habits.

**Android 8.0+ · vollständig offline · kein Benutzerkonto · keine Werbung**

Die App benötigt keinen Server und besitzt keine `INTERNET`-Berechtigung. Einstellungen und Fortschritt bleiben auf dem Gerät.

## Projektstruktur

```text
app/       Native Android-App und eingebettete Offline-Oberfläche
ui/        Quellcode der Oberfläche, die in die APK eingebaut wird
gradle/    Gradle-Wrapper
docs/      APK-Installation, Build und Datenschutz
```

`ui/` ist kein separates Produkt und keine Website. Der Ordner enthält ausschließlich die Oberfläche der Android-APK. `pnpm build` erzeugt daraus `app/src/main/assets/site/index.html`.

## APK lokal bauen

Benötigt werden JDK 17 sowie Android SDK 35.

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

Die APK liegt anschließend unter `app/build/outputs/apk/debug/app-debug.apk`.

Nach Änderungen an der Oberfläche zuerst:

```powershell
cd ui
pnpm install --frozen-lockfile
pnpm exec tsc --noEmit
pnpm test
pnpm build
cd ..
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

## GitHub-Workflow

Der Workflow `.github/workflows/android-apk.yml` testet und baut bei Pull Requests eine Debug-APK. Auf `main` oder `master` erstellt er mit vorhandenen Signatur-Secrets eine signierte APK und veröffentlicht sie als GitHub-Release. Ohne Signatur-Secrets wird eine Debug-APK als Workflow-Artefakt bereitgestellt.

Ausführliche Hinweise:

- [Installation](docs/INSTALLATION.md)
- [Entwicklung](docs/ENTWICKLUNG.md)
- [GitHub Actions](docs/GITHUB-ACTIONS.md)
- [Datenschutz](docs/DATENSCHUTZ.md)
- [Änderungen](CHANGELOG.md)

## Kompatibilität

Der technische Paketname `de.danberg.wachwerk` sowie bestehende Speicher-, Alarm- und Kanal-IDs bleiben absichtlich unverändert. Android und bereits installierte MACH.-Versionen benötigen diese IDs für kompatible Updates und den Erhalt vorhandener Nutzerdaten. Sie sind kein sichtbarer App-Name.
