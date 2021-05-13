# Terrafying
Terrafying ist ein Multiplayer-2D-Sandbox-Spiel, inspiriert durch Minecraft und Terraria.

## Download
[Neuste Version](https://github.com/Tresonic/Terrafying/releases/download/latest/Terrafying.jar) (enthält mit großer Wahrscheinlichkeit Bugs oder stürzt ab)

Stabile Version (hoffentlich bald)

## Bauen
### Linux
`gradlew dist:desktop`
### Windows
`.\gradlew dist:desktop`

Die fertige Jar-Datei befindet sich in `desktop/build/libs`

## Bedienung
### Menü
1. Spielernamen eingeben
2. Spielmodus auswählen:

Button | Aktion
--- | ---
Join | Einem bestehenden Spiel beitreten. Dabei muss, sofern nicht automatisch vom Spiel erkannt, eine IP-Adresse eingegeben werden.
Host | Ein neues Spiel eröffnen. Hier können andere Spieler im lokalen Netz beitreten.

### Spiel

| Taste | Aktion |
| --- | --- | 
| <kbd>A</kbd> | nach links gehen |
| <kbd>D</kbd> | nach rechts gehen |
| <kbd>Leertaste</kbd> | springen |
| Linke Maustaste | Block abbauen | 
| Rechte Maustaste | Block platzieren |
| <kbd>E</kbd> | Inventar öffnen |

## Server
Mit dem Argument `server` kann ein dedizierter Server gestartet werden:

`java -jar Terrafying.jar server`
