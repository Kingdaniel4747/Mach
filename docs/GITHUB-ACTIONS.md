# APK auf GitHub bauen lassen

Du änderst den Quellcode und lädst deine Änderungen mit GitHub Desktop hoch. GitHub Actions baut daraus die APK. Android Studio ist dafür auf deinem Rechner nicht nötig.

Die App bleibt offline. Nur der **Build** läuft auf GitHub; App-Daten werden dadurch nicht synchronisiert.

## 1. Workflow hochladen und starten

1. Die geänderten Projektdateien in GitHub Desktop als Commit speichern und **Push origin** drücken. Auch `.github/workflows/android-apk.yml` muss mit hochgeladen werden.
2. Dein Repository auf GitHub öffnen und oben **Actions** auswählen.
3. Links **MACH APK bauen** öffnen. Wenn GitHub Actions noch deaktiviert ist, zunächst für dieses Repository aktivieren.
4. Nach Code-Änderungen auf `main` oder `master` startet automatisch ein signierter `release`-Build. Pull Requests werden separat als `debug` geprüft. Reine Dokumentationsänderungen lösen keinen Build aus.
5. Nach erfolgreicher Prüfung erstellt der Workflow automatisch ein neues GitHub-Release mit eigener Versionsnummer und hängt die signierte APK an.
6. Alternativ **Run workflow** anklicken und einen Build manuell starten. Der Workflow muss dafür auf dem Standardbranch vorhanden sein.
7. Die APK liegt sowohl im neuen Bereich **Releases** als auch im Workflow-Artefakt. `SHA256SUMS.txt` und `BUILD-INFO.txt` dokumentieren Prüfsumme, Versionscode und Commit-ID.

Workflow-Artefakte werden für 30 Tage, Android-Prüfberichte für 14 Tage angefordert. GitHub-Releases bleiben erhalten, bis du sie selbst löschst. Der Workflow verändert den Quellcode nicht, erzeugt aber automatisch einen Release-Tag und ein Release.

Zum Herunterladen von Actions-Artefakten musst du bei GitHub angemeldet sein und Zugriff auf das Repository haben. Erfolgreiche Release-Builds werden zusätzlich automatisch als GitHub-Release veröffentlicht. Bei einem öffentlichen Repository lässt sich die APK darüber ohne GitHub-Anmeldung herunterladen. [GitHub erklärt den Artefakt-Download hier](https://docs.github.com/en/actions/how-tos/manage-workflow-runs/download-workflow-artifacts).

## 2. Test-APK oder Update für dein Handy?

| Variante | Einrichtung | Verwendung |
| --- | --- | --- |
| `debug` | Keine Secrets nötig | Testinstallation mit automatisch erzeugtem Debug-Schlüssel. Nicht als Update der bisher signierten Release-App geeignet. |
| `release` | Vier private GitHub-Secrets | Automatisch veröffentlichte APK mit dauerhaft gleichem Schlüssel und steigendem Versionscode; dadurch als Update geeignet. |

**Wichtig:** Debug-APKs und Release-APKs besitzen unterschiedliche Signaturen. Android akzeptiert ein Update nur bei identischem Paketnamen, identischem Signaturschlüssel und höherem Versionscode. Installiere auf dem Alltagshandy einmal die korrekt signierte GitHub-Release-APK; danach können alle folgenden automatischen Releases darüber installiert werden, ohne App-Daten zu löschen. Wenn die aktuell installierte APK mit einem anderen Schlüssel signiert wurde, ist genau einmal eine Neuinstallation nötig – vorher über **Einstellungen → Backup & Wiederherstellung** die Daten sichern.

## 3. Signierte Release-APK einmalig einrichten

Im Repository **Settings → Secrets and variables → Actions → New repository secret** öffnen. Folgende vier Secrets anlegen:

| Secret | Inhalt |
| --- | --- |
| `WACHWERK_KEYSTORE_BASE64` | Der Inhalt deiner bisherigen Keystore-Datei, als Base64 codiert |
| `WACHWERK_STORE_PASSWORD` | Passwort dieser Keystore-Datei |
| `WACHWERK_KEY_ALIAS` | Alias des bisherigen Schlüssels; beim ursprünglichen MACH-Schlüssel `wachwerk` |
| `WACHWERK_KEY_PASSWORD` | Passwort des Schlüssels |

Den **bisherigen** privaten Signaturschlüssel benutzen, nicht einen neuen erzeugen. Er ist absichtlich nicht Teil des Repositorys. Lade weder die Schlüsseldatei noch ihr Backup in Git, Issues oder Releases hoch.

So kannst du die Base64-Fassung lokal mit PowerShell direkt in die Zwischenablage kopieren, ohne sie in einer Repository-Datei zu speichern. Den Beispielpfad durch den echten Pfad zu deiner Keystore-Datei ersetzen:

```powershell
$keystorePath = 'C:\Privat\wachwerk-release.jks'
[Convert]::ToBase64String([IO.File]::ReadAllBytes($keystorePath)) | Set-Clipboard
```

Den Zwischenablage-Inhalt ausschließlich als Wert von `WACHWERK_KEYSTORE_BASE64` in GitHub einfügen. Danach die Zwischenablage und gegebenenfalls deren Verlauf leeren. Base64 ist **keine Verschlüsselung**: Behandle den Inhalt wie die private Schlüsseldatei. Speichere ihn nicht bei den normalen „Variables“, sondern unter **Secrets**. GitHub beschreibt die Verwaltung verschlüsselter [Actions-Secrets hier](https://docs.github.com/en/actions/how-tos/write-workflows/choose-what-workflows-do/use-secrets).

Anschließend genügt jeder Push von App-Code auf den Standardbranch. Der Workflow prüft den Code, erhöht den Android-Versionscode, signiert die APK und veröffentlicht sie unter **Releases**. Ein manueller `release`-Lauf ist weiterhin möglich.

Wenn ein Secret fehlt oder die Signierung scheitert, schlägt der Lauf fehl. Es wird **nicht** stillschweigend eine unpassende Test- oder unsignierte APK als Release ausgegeben. Release-Builds sind nur bei Pushes oder manuellen Starts auf dem Standardbranch erlaubt. Verwende dafür ausschließlich geprüften Code; wer diesen Code oder den Workflow ändern kann, könnte sonst beim Build auf die freigegebenen Secrets zugreifen. Branch-Schutz und sorgfältige Prüfung von Änderungen sind deshalb sinnvoll.

`versionCode` und die vollständige `versionName` werden im Workflow automatisch aus GitHub-Laufnummer und -Versuch gebildet. Die Basisversion in `wachwerk-local-web/package.json` wird nur bei größeren geplanten Versionssprüngen von Hand geändert.

## Was der Workflow macht

1. Node.js 24, die in `package.json` festgelegte pnpm-Version, Java 17 und Android SDK 35 einrichten.
2. Web-Abhängigkeiten exakt aus dem Lockfile installieren, TypeScript prüfen und Web-Tests ausführen.
3. Die React-Oberfläche frisch bauen und über `inline.mjs` in die Android-Assets übernehmen. Änderungen an der Oberfläche landen dadurch wirklich in der neuen APK.
4. Den Gradle-Wrapper prüfen, Android-Unit-Tests und Release-Lint ausführen.
5. Die gewünschte APK bauen, ihre Signatur mit `apksigner` prüfen und die SHA-256-Prüfsumme erzeugen.
6. APK und Android-Prüfberichte als getrennte Downloads bereitstellen und erfolgreiche Release-Builds automatisch als neues GitHub-Release veröffentlichen.

Der Release-Schlüssel wird nur für den Signierschritt in einer temporären Datei angelegt und danach entfernt. Er ist nicht Bestandteil der hochgeladenen Artefakte oder des Gradle-Caches. Externe Actions sind auf feste Commit-IDs festgelegt. Der Workflow besitzt Schreibzugriff auf Inhalte ausschließlich, um Release-Tags und Releases zu erstellen; er schreibt den generierten Versionscode nicht in den Quellcode zurück.

Der erste Lauf braucht wegen der Downloads gewöhnlich länger. Bei einem roten Lauf zuerst den fehlgeschlagenen Schritt und dessen Fehlermeldung öffnen. Ein erfolgreicher Build ersetzt keinen echten Handytest von NFC, Alarmton, Display-Sperre und Energiesparen.

[Zurück zur README](../README.md)
