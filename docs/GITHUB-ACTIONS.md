# MACH. APK mit GitHub Actions bauen

Der Workflow `.github/workflows/android-apk.yml` baut ausschließlich die Android-App. Dabei wird die Oberfläche aus `ui/` frisch erzeugt und in die APK eingebettet.

## Automatischer Ablauf

1. Node.js 22, pnpm 11.19.0, Java 17 und Android SDK 35 werden eingerichtet.
2. TypeScript-Prüfung und UI-Logiktests laufen.
3. Die Offline-Oberfläche wird nach `app/src/main/assets/site/index.html` geschrieben.
4. Android-Unit-Tests und Lint laufen.
5. Eine Debug-APK wird gebaut.
6. Bei vorhandenen Signatur-Secrets wird zusätzlich eine signierte Release-APK erstellt und als GitHub-Release veröffentlicht.

Pull Requests erhalten immer eine Debug-APK als Workflow-Artefakt. Bei Pushes auf `main` oder `master` und manuellen Läufen wird ohne vollständig eingerichtete Signierung ebenfalls die Debug-APK bereitgestellt.

## Signierte Updates einrichten

Unter **Settings → Secrets and variables → Actions** diese vier Repository-Secrets anlegen:

| Secret | Inhalt |
| --- | --- |
| `MACH_KEYSTORE_BASE64` | Keystore-Datei als Base64 |
| `MACH_STORE_PASSWORD` | Keystore-Passwort |
| `MACH_KEY_ALIAS` | Schlüssel-Alias |
| `MACH_KEY_PASSWORD` | Schlüsselpasswort |

Der vorhandene Signaturschlüssel muss weiterverwendet werden, damit Android die APK als Update akzeptiert. Frühere Secrets mit dem Präfix `WACHWERK_` werden vom Workflow weiterhin als Fallback erkannt.

PowerShell-Beispiel zum Kopieren des Keystores als Base64:

```powershell
$keystorePath = 'C:\Privat\mach-release.jks'
[Convert]::ToBase64String([IO.File]::ReadAllBytes($keystorePath)) | Set-Clipboard
```

Base64 ist keine Verschlüsselung. Keystore und Secret-Werte dürfen nicht in Git, Issues, Logs oder Releases gespeichert werden.

## Ergebnisse

- Debug: Actions-Artefakt `MACH-debug-<Laufnummer>`
- Release: `MACH-<Version>.apk` unter GitHub Releases
- Prüfsumme: `SHA256SUMS.txt` im Release

Debug- und Release-APK sind unterschiedlich signiert. Eine Debug-APK kann eine installierte Release-App normalerweise nicht aktualisieren.

[Zurück zur README](../README.md)
