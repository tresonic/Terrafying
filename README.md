
# ![icon](https://github.com/Tresonic/Terrafying/blob/main/core/assets/icon.png)errafying
Terrafying ist ein Multiplayer-2D-Sandbox-Spiel, inspiriert durch Minecraft und Terraria.  
Für mehr Informationen gibt es ein [Wiki](https://github.com/Tresonic/Terrafying/wiki).  
## Download
[Neueste Version](https://github.com/Tresonic/Terrafying/releases/download/latest/Terrafying.jar) (enthält mit großer Wahrscheinlichkeit Bugs oder stürzt ab)

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
Join | Einem bestehenden Spiel beitreten. Dabei muss, sofern nicht automatisch vom Spiel erkannt, die IP-Adresse des Servers eingegeben werden.
Host | Ein neues Spiel eröffnen. Hier können andere Spieler im lokalen Netz beitreten.
Exit | Das Spiel verlassen.

### Spiel

Taste | Aktion
--- | ---
<kbd>D</kbd> | nach rechts gehen
<kbd>A</kbd> | nach links gehen
<kbd>Leertaste</kbd> | springen
<kbd>LMT</kbd> | Block abbauen
<kbd>RMT</kbd> | Block platzieren
<kbd>E</kbd> | Inventar öffnen
<kbd>1</kbd> - <kbd>9</kbd> | Hotbar-Slot auswählen

## Server
Mit dem Argument `server` kann ein dedizierter Server gestartet werden:

`java -jar Terrafying.jar server`  
