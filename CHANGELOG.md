# Änderungsverlauf

## Nächste Version

- Neue Marke **MACH.** mit dem Leitsatz „Kein später.“
- Neues App-Icon passend zur App in Nachtblau, Mint, Eisblau und Weiß.
- Der technische Paketname bleibt für kompatible Updates unverändert.
- Tägliche lokale Quests mit XP, Level, Serie und gedeckelten Bonusminuten – bewusst ohne Spielfigur und ohne Konto.
- Einheitlicher Sperrbildschirm startet NFC oder QR sofort und behandelt Direkt-, Limit- und Uhrzeitregeln getrennt.
- NFC-Wecker fordert bei einem gesperrten Gerät zuerst klar zum sicheren Entsperren auf; Snooze ist als großer hellblauer Knopf sichtbar.
- Mittig ausgerichtete Ziehgriffe in den App-Dialogen.
- Zyklus-Wecker besitzen nun einen eigenen Einschlafplan: gewählte Bettzeit bei „Aufstehen um“, sofortiger Start bei „Ich schlafe jetzt“.
- Normale Wecker können ihre Einschlaf-Erinnerung einzeln aktivieren und zeitlich festlegen, ohne den täglichen Plan zu überschreiben.
- Lokaler JSON-Export und -Import für Einstellungen, Wecker und Fortschritt; Sicherheitszugriffe und eigene Audiodateien werden bewusst nicht exportiert.
- GitHub Actions vergibt bei jedem Lauf einen höheren Android-Versionscode und veröffentlicht erfolgreiche, signierte Standardbranch-Builds automatisch als Release.

## 1.13.0 · 2. September 2026

### Neu

- **Blocker → Morgen:** ausgewählte Apps nach einer erfolgreich abgeschlossenen Aufwachaufgabe für eine konfigurierbare Dauer sperren.
- Dauerhaft gespeicherte Warteschlange für ausgelöste Alarmereignisse.
- Hinweis in der App, über den die laufende Aufwachaufgabe wieder geöffnet werden kann.

### Korrigiert

- Tagesnutzung aus Android-Ereignissen seit Mitternacht statt übergroßer aggregierter Tagesfenster.
- Keine pauschale Übernahme des höheren Werts aus System- und lokalem Zähler.
- Alarmton in eigenem Vordergrunddienst; Lebenszyklus der Ansicht beendet den Alarm nicht mehr.
- NFC-Leser beim Zurückkehren neu aktivieren, aktuelle Aufwachaufgabe bei neuen Intents laden.
- Gerätesperre, fehlendes NFC und ausgeschaltetes NFC sichtbar behandeln.
- Getrennte Behandlung der Morgensperre und der bisherigen Sperrbereiche.
- Android-8.0-kompatible Aufteilung der Navigationsleisten-Theme-Einstellung.

### Prüfstand

25 Android-Unit-Tests und 5 Web-Logiktests erfolgreich; keine Lint-Fehler. Geräteprüfung für NFC, gesperrten Bildschirm und Samsung-Energiesparverhalten steht aus.

## 1.12.0

- Drei wählbare Farbpaletten: Original, Sonnenwärme und Abendruhe.
- Kompaktere Zahlenfelder mit Drehrad.
- Vergrößerte Schließgesten-Zone für die unteren Dialoge.
- Habit-Markierungen in einer Zeile.
- Getrennte Schlüssel und Aktivierungszustände für Direkt, Limits und Uhrzeiten.
- Überlappende Sperren separat freigeben.
