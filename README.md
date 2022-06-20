# Vizsgaremek - `IoTea`

## Leírás

Az indíttatást az okoslámpáim biztosították, hiszen bár számos praktikus funkció elérhető a gyártó applikációjából, de:

 * Sokkal többre képesek
 * Ehhez API elérés is társul

Az alkalmazás kialakítását a rugalmasság és a későbbi bővíthetőség szabta meg, így a kezdeti ötlet (modulárisan bővíthető, általános okoslámpa-vezérlő) átalakult: 
A cél egy IoT eszközöket és parancssablonokat katalogizáló program lett. A két fő entitás REST alapú kezelése mellett célom volt összekötésük és vezérlésük hatékony és egyszerű megoldása. 


### Workflow

A felhasználó tetszőleges számú IoT eszközt (`Device`) és parancssablont (`Command`) vehet fel az alkalmazás REST végpontjain (rendre `/api/devices/...` és `/api/commands/...`). 
Egy `Device` bármennyi `Command` entitáshoz kapcsolódhat és viszont (N:M reláció). A két entitástípus közötti kapcsolatot a `Job` (`/api/jobs/...`) entitás felvételével lehet létrehozni: 
Táblája (`jobs`) kapcsolótábla, viszont rendelkezik egyedi kulcsokkal (PK: id INT(11), AK: name VARCHAR(255)), így hivatkozható: Ezáltal lehetőség van a kapcsolat által reprezentált művelet 
(`Command` küldése a `Device` részére) tetszőleges számú, könnyű meghívására. Ha a parancssablon rendelkezik paraméterekkel, azokat minden híváskor meg kell adni: Nem kerülnek eltárolásra.


#### Bemutató HTTPie segítségével

##### Egy *Device* létrehozása

`echo '{"uid": "0x000000003ac864d5", "name": "Desk", "ip": "192.168.0.50", "port": 55443}' | http POST :8080/api/devices`

```json
HTTP/1.1 200 OK
Content-Length: 111
Content-Type: application/json

{
    "address": {
        "ip": "192.168.0.50",
        "reachable": true
    },
    "id": 1,
    "name": "Desk",
    "port": 55443,
    "uid": "0x000000003ac864d5"
}
```

##### Egy (paraméter nélküli) *Command* létrehozása

`echo '{"name": "YeeToggle", "template": "{\"id\": 1, \"method\": \"toggle\", \"params\": []}"}'  | http POST :8080/api/commands`

```json
HTTP/1.1 200 OK
Content-Length: 102
Content-Type: application/json

{
    "id": 1,
    "name": "YeeToggle",
    "note": "",
    "template": "{\"id\": 1, \"method\": \"toggle\", \"params\": []}"
}
```


##### Egy *Job* létrehozása

`echo '{"name": "ToggleDesk", "device_id": 1, "command_id": 1}' | http POST :8080/api/jobs`

```json
HTTP/1.1 200 OK
Content-Length: 275
Content-Type: application/json

{
    "command": {
        "id": 1,
        "name": "YeeToggle",
        "note": "",
        "template": "{\"id\": 1, \"method\": \"toggle\", \"params\": []}"
    },
    "device": {
        "address": {
            "ip": "192.168.0.50",
            "reachable": true
        },
        "id": 1,
        "name": "Desk",
        "port": 55443,
        "uid": "0x000000003ac864d5"
    },
    "id": 1,
    "name": "YeeToggle",
    "result": null
}
```


##### Egy *Job* paraméter nélküli meghívása

`echo '{}' | http :8080/api/jobs/id/1`

```json
HTTP/1.1 200 OK
Content-Length: 483
Content-Type: application/json

{
    "command": {
        "id": 1,
        "name": "YeeToggle",
        "note": "",
        "template": "{\"id\": 1, \"method\": \"toggle\", \"params\": []}"
    },
    "device": {
        "id": 1,
        "address": {
            "ip": "192.168.0.50",
            "reachable": true
        },
        "name": "Desk",
        "port": 55443,
        "uid": "0x000000003ac864d5"
    },
    "id": 1,
    "name": "ToggleDesk",
    "result": {
        "parameters": [],
        "payload": "{\"id\": 1, \"method\": \"toggle\", \"params\": []}",
        "raw_parameters": null,
        "response": "{\"method\":\"props\",\"params\":{\"power\":\"on\"}}",
        "started": "2022-01-10T20:30:40.299970222"
    }
}
```


---

## Felépítés

### Device

A `Device` entitás a következő attribútumokkal rendelkezik:

* **id** – Egyedi azonosító elsősorban API hívások kiszolgálására
	* `PUT /commands/id/{id}` esetén szabadon választható, valid, egész szám
	* Minden más esetben adatbázis által generált érték (AUTO INCREMENT)
* **uid** __*__ – Egyedi azonosító elsősorban a DHCP miatt új címen elérhető eszközök felismeréséhez 
	* Előállítása és felhasználása az `IoTea`-hez kapcsolódó szerviztől függ
* **name** __*__ – Egyedi (human-readable) azonosító
* **address** __*__ – Az eszköz IP címe, mint `java.net.InetAddress`
	* Beküldött objektumnál `ip` kulcs alatt csak az IP címet kell megadni szöveges formátumban
* **port** __*__ – Az eszköz portja

Mindegyik attribútum letárolásra kerül az adatbázis `devices` táblájában, az oszlopok nevei az objektum mezőiével mindenhol egyeznek.


| Név        | Típus              | Megkötések, megjegyzések                                  |     TYPE      | NOT NULL |  UNIQUE  | 
| ---------- | ------------------ | --------------------------------------------------------- | :-----------: | :------: | :------: |
| id         | Integer            | Nem kötelező, ignorált (input objektumban nincs szerepe)  | INT(11)       |    X     |    X     |
| uid        | String             | Kötelező, 1 - 255 karakter, nem null, nem 'blank'         | VARCHAR(255)  |    X     |    X     |
| name       | String             | Kötelező, 1 - 255 karakter, nem null, nem 'blank'         | VARCHAR(255)  |    X     |    X     |
| address    | DeviceAddress      | Kötelező, 1 - 255 karakter, nem null, nem 'blank'         | VARCHAR(255)  |    X     |          |
| port       | Integer            | Kötelező, 0 és 65535 közötti egész szám, nem null         | INT(11)       |    X     |          |


A `Device` és a `Job` entitások között a `Job` felől egyirányú, 1:N kapcsolat van.

Végpontok:

| HTTP | Végpont                                          | Leírás                                                                             |
| ---- | ------------------------------------------------ | ---------------------------------------------------------------------------------- |
| GET  | `/api/devices/` | |
| GET  | `/api/devices/` | |


---

### DeviceAddress

A `DeviceAddress` a `java.net.InetAddress` osztályt burkolja és egészíti ki funkcionalitással.

Adatbázisban közvetlenül nem kerül letárolásra, csak karakterlánccá alakított IP címe (InetAddress::getHostAddress) a `Device` entitás részeként.


---

### Command 

A `Command` entitás a következő attribútumokkal rendelkezik:

* **id** – Egyedi azonosító elsősorban API hívások kiszolgálására
	* `PUT /commands/id/{id}` esetén szabadon választható, valid, egész szám
	* Minden más esetben adatbázis által generált érték (AUTO INCREMENT)
* **name** __*__ – Egyedi (human-readable) azonosító
* **template** __*__ – printf formátumú sablon, amely a parancsot reprezentálja
* **note** – Szöveges jegyzet a parancsról

Mindegyik attribútum letárolásra kerül az adatbázis `commands` táblájában, az oszlopok nevei az objektum mezőiével mindenhol egyeznek.


| Név        | Típus              | Megkötések, megjegyzések                                  |     TYPE      | NOT NULL |  UNIQUE  | 
| ---------- | ------------------ | --------------------------------------------------------- | :-----------: | :------: | :------: |
| id         | Integer            | Nem kötelező, ignorált (input objektumban nincs szerepe)  | INT(11)       |    X     |    X     |
| name       | String             | Kötelező, 1 - 255 karakter, nem null, nem 'blank'         | VARCHAR(255)  |    X     |    X     |
| template   | String             | Kötelező, 0 - 30000 karakter, nem null                    | TEXT          |    X     |          |
| note       | String             | Nem kötelező, 0 - 30000 karakter, lehet null (map -> "")  | TEXT          |    X     |          |

A `Command` és a `Job` entitások között a `Job` felől egyirányú, 1:N kapcsolat van.

Végpontok:

| HTTP | Végpont                                          | Leírás                                                                             |
| ---- | ------------------------------------------------ | ---------------------------------------------------------------------------------- |
| GET  | `/api/commands/` | |
| GET  | `/api/commands/` | |



---

### Job 

A `Job` entitás a következő attribútumokkal rendelkezik:

* **id** – Egyedi azonosító elsősorban API hívások kiszolgálására
	* `PUT /jobs/id/{id}` esetén szabadon választható, valid, egész szám
	* Minden más esetben adatbázis által generált érték (AUTO INCREMENT)
* **name** __*__ – Egyedi (human-readable) azonosító
* **device** __*__ – A kapcsolódó `Device`
	* Beküldött objektumnál `device_id` kulcs alatt a `Device` azonosítóját (`id`) kell megadni
* **command** __*__ – A kapcsolódó `Command`
	* Beküldött objektumnál `command_id` kulcs alatt a `Command` azonosítóját (`id`) kell megadni
* **result** – A lefuttatott `Job` eredménye, értéke:
	* `Run` objektum, ha a futtátási kérésre kapjuk a `Job`-ot válaszként
	* `null`, ha más kérésre kapjuk válaszul a `Job`-ot: létrehozás, módosítás, lekérdezés 

Csak az első négy attribútum kerül letárolásra az adatbázis `jobs` táblájában. Az oszlopok nevei két esetben térnek el az objektum mezőiétől:
* `device_id` tárolja a `Job`-hoz tartozó `Device` azonosítóját (`id`)
* `command_id` tárolja a `Job`-hoz tartozó `Command` azonosítóját (`id`)


| Név        | Típus              | Megkötések, megjegyzések                                  |     TYPE      | NOT NULL |  UNIQUE  | 
| ---------- | ------------------ | --------------------------------------------------------- | :-----------: | :------: | :------: |
| id         | Integer            | Nem kötelező, ignorált (input objektumban nincs szerepe)  | INT(11)       |    X     |     X    |
| name       | String             | Kötelező, 1 - 255 karakter, nem null, nem 'blank'         | VARCHAR(255)  |    X     |     X    |
| device     | Device             | Kötelező, 0 és 2147483647 közötti egész szám, nem null    | INT(11)       |    X     |          |
| command    | Command            | Kötelező, 0 és 2147483647 közötti egész szám, nem null    | INT(11)       |    X     |          |
| result     | Run                | Tranziens, nem kötelező, ignorált. Nincs validálás.       |      ---      |   ---    |    ---   |



A `Job` és a `Device` entitások között a `Job` felől egyirányú, N:1 kapcsolat van.
A `Job` és a `Command`  entitások között a `Job` felől egyirányú, N:1 kapcsolat van.

Végpontok:

| HTTP | Végpont                                          | Leírás                                                                             |
| ---- | ------------------------------------------------ | ---------------------------------------------------------------------------------- |
| GET  | `/api/jobs/` | |
| GET  | `/api/jobs/` | |



---

### Run

A `Run` a `Job` egy lefutását összegzi. Adatbázisban nem kerül tárolásra, így értéke `null`, ha a `Job` objektum még nem lett lefuttatva. A következő attribútumokkal rendelkezik: 

| Név           | Típus              | Leírás                                                                                                  | 
| ------------- | ------------------ | ------------------------------------------------------------------------------------------------------- |
| started       | LocalDateTime      |                                                                                                         |
| rawParameters | String             |                                                                                                         |
| parameters    | Object[]           |                                                                                                         |
| payload       | String             |                                                                                                         |
| response      | String             |                                                                                                         |


---

## Futtatás

A projekt főkönyvtárában adjuk ki a `docker-compose up -d` parancsot.

| Komponens  | Elérési út                               |
| :--------: | ---------------------------------------- |
| MariaDB    | Nincs kivezetve.                         | 
| Frontend   | http://localhost:8080/                   |
| REST       | http://localhost:8080/api                |
| Swagger UI | http://localhost:8080/swagger-ui.html    |
| OpenAPI    | http://localhost:8080/IoTea_OpenAPI.yaml |
