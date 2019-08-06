# Java-Bibliothek für das Qwirkle-Interface

## Aufbau

In der Bibliothek befinden sich die vom Interface-Komitee bestimmten Klassen für das Erstellen von Client- und Serveranfragen sowie ein Parser, der Nachrichten-Objekte in JSON-Strings und JSON-Strings in Nachrichten-Objekte umwandeln kann.

Der Aufbau der Bibliothek ist wie folgt:

    interfaceLibrary (Library)
    └── de.upb.swtpra1819interface (main-package)
            'keine Klassen'
        ├── parser (sub-package)
            'Parser'
            'ParsingException'
        └── messages (sub-package)
            [ Nachrichtenklassen]

In den beiden Subpackages für den Client und den Server liegen entsprechende Java-Klassen, die für die Umwandlung von Nachrichten-Objekt in JSON-String und umgekehrt zuständig sind.

## Nutzung

Um eine Anfrage zu erzeugen, ist es zunächst notwendig, das Package `de.upb.swtpra1819interface.messages` zu importieren. Anschließend erstellt man ein Objekt der jeweiligen Anfragenklasse.

Um nun einen JSON-String aus dem Objekt zu erstellen, wird der Parser benötigt. Diesen importiert man sich zunächst, mit `import de.upb.swtpra1819interface.Parser`, und erstellt anschließend ein Objekt.

Die Parser-Klasse besitzt vier Methoden. `serialize`, `deserialize`, `loadConfig` und `saveConfig`.
`serialize`erhält ein Nachrichtenobjekt und gibt den dazu entsprechenden JSON-String aus.
`deserialize` hingegen bekommt einen JSON-String und wandelt diesen in das entsprechende Nachrichtenobjekt um.
`loadConfig` bekommt ein Dateinamen (String ohne '.json'-Endung) und lädt das Configuration-Objekt aus einer Datei im aktuellen Verzeichnis.
`saveConfig` bekommt ein Configuration-Objekt und einen Dateinamen (String ohne '.json'-Endung) und erstellt eine '.json'-Datei mit diesem Namen.

Dabei ist zu beachten, dass JSON-Strings, die empfangen werden, als Objekte des Typs `String` eingegeben werden müssen. Die Funktion Konfigurationen in einem beliebigen Pfad zu speichern wird in einer späteren Version der Interface-Library umgesetzt.

## Fehlerbehandlung

Die Methode `deserialize` des Parsers wirft eine `ParsingException`, falls keine ID im JSON-String gelesen werden konnte. Deswegen ist eine entsprechende Fehlerbehandlung im Caller der Methode notwendig.
