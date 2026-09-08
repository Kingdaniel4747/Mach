# Eigene Tagesquests

Bearbeite `quests.json`. Jeder Eintrag braucht diese Felder:

```json
{
  "id": "eindeutiger-kurzname",
  "title": "Titel in der App",
  "detail": "Kurze Erklärung",
  "category": "Kategorie",
  "difficulty": "easy",
  "durationMinutes": 20,
  "xp": 25
}
```

Erlaubte Schwierigkeitsstufen: `easy`, `medium`, `hard`.
`durationMinutes` muss zwischen 1 und 1.440 liegen. Beim nächsten lokalen Build oder GitHub-Workflow wird die Datei automatisch in die APK eingebettet.
