# Git für SWTPRA 6

## An die SWTPRA-ORGA 

### Unsere Website http://agilebearco.ga/index.html

### Anleitungen für die einzelnen Komponenten:

#### Server
1. Importiert das Projekt aus dem Ordner Qwirkle_Server/Server
2. Startet das Projekt mit der Ausrichter.java Klasse
3. Drückt auf den Start Server Button -> Server sollte laufen
4. Wenn ihr Spiele erstellen wollt:
    - Drückt im Sidebar auf Create Game
    - Wenn ihr keine Lust habt Konfigs auszufüllen, clickt direkt auf Apply bzw Make Game
    - Nun sollten Standardkonfigs geladen sein
    - Falls ihr Änderungen in Konfigeinstellungen macht, immer vorher auf Apply drücken
    - Falls nichts schon getan, drückt auf make Game, und es erstellt das Spiel
5. Falls ihr Clienten joinen wollt
    - Geht auf den Server Overview 
    - Clickt auf den Clienten, den ihr joinen wollt
    - Clickt auf das Spiel
    - Clickt auf den ForceJoin Button (ist manchmal buggy, wählt, falls es nicht wählbar wird nochmals den Clienten und das Game aus)
6. Die anderen Buttons funktionieren analog, bei Game-Relevanten Sachen Game auswählen und dann Button clicken

#### KI
1. Importiert Projekt aus dem Ordner Qwirkle_KI
2. Setzt die KI in der Klasse Initiate_ENGINEPLAYER.java auf die richtigen Serverdaten (und nach wunsch namen ;) )
3. Started die Initiate_ENGINEPLAYER Klasse, wenn ihr einen Server laufen habt
4. [FALLS IHR DIE KI AN UNSEREN SERVER TESTED, DIE STÜRZT MANCHMAL AB WEGEN INTERRUPTED, SOMIT MEHRERE STARTEN]

#### Beobachter
1. Importiert Projekt aus dem Ordner PC-O&P/Observer
2. Startet das Projekt mit der Klasse Mainstarter.java
3. Gibt die Daten ein, und joint dem Spiel, den ihr joinen wollt

#### Android
1. Importiert das Projekt im Android-O&P
2. Builded das Projekt mit Gradle [ Falls Probleme aufkommen, einmal versuchen zu Cleanen und Rebuilden ]
3. Started das Applet
4. Macht weiter so wie Beobachter