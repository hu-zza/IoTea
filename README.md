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

`http :8080/api/devices uid=0x000000003ac864d5 name=Desk ip=192.168.0.50 port=55443`

```json
HTTP/1.1 201 Created
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

`http :8080/api/commands name=YeeToggle template='{"id": 1, "method": "toggle", "params": []}' note='Toggle command for Yeelight devices.'`

```json
HTTP/1.1 201 Created
Content-Length: 138
Content-Type: application/json

{
    "id": 1,
    "name": "YeeToggle",
    "note": "Toggle command for Yeelight devices.",
    "template": "{\"id\": 1, \"method\": \"toggle\", \"params\": []}"
}
```


##### Egy *Job* létrehozása

`http :8080/api/jobs name=ToggleDesk device_id=1 command_id=1`

```json
HTTP/1.1 201 Created
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
    "name": "ToggleDesk",
    "result": null
}
```


##### Egy *Job* paraméter nélküli meghívása

`http :8080/api/jobs/name/ToggleDesk parameters:='[]'`

```json
HTTP/1.1 202 Accepted
Content-Length: 482
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
        "started": [
            2022,
            1,
            10,
            20,
            30,
            40,
            299970222
        ]
    }
}
```


##### Egy (paraméterezett) *Command* létrehozása

`http :8080/api/commands name=YeeBright template='{"id": 1, "method": "set_bright", "params": [%d, "%s", %d]}' note='Set brightness command for Yeelight devices. (1-100%, smooth / sudden, 30-... ms)'`

```json
HTTP/1.1 201 Created
Content-Length: 201
Content-Type: application/json

{
    "id": 2,
    "name": "YeeBright",
    "note": "Set brightness command for Yeelight devices. (1-100%, smooth / sudden, 30-... ms)",
    "template": "{\"id\": 1, \"method\": \"set_bright\", \"params\": [%d, \"%s\", %d]}"
}

```


##### Újabb *Job* létrehozása

`http :8080/api/jobs name=BrightDesk device_id=1 command_id=2`


```json
HTTP/1.1 201 Created
Content-Length: 375
Content-Type: application/json

{
    "command": {
        "id": 2,
        "name": "YeeBright",
        "note": "Set brightness command for Yeelight devices. (1-100%, smooth / sudden, 30-... ms)",
        "template": "{\"id\": 1, \"method\": \"set_bright\", \"params\": [%d, \"%s\", %d]}"
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
    "id": 2,
    "name": "BrightDesk",
    "result": null
}
```


##### Egy *Job* paraméterezett meghívása

`http :8080/api/jobs/name/BrightDesk parameters:='[100, "smooth", 30000]'`


```json
HTTP/1.1 202 Accepted
Content-Length: 642
Content-Type: application/json

{
    "command": {
        "id": 2,
        "name": "YeeBright",
        "note": "Set brightness command for Yeelight devices. (1-100%, smooth / sudden, 30-... ms)",
        "template": "{\"id\": 1, \"method\": \"set_bright\", \"params\": [%d, \"%s\", %d]}"
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
    "id": 2,
    "name": "BrightDesk",
    "result": {
        "parameters": [
            100,
            "smooth",
            30000
        ],
        "payload": "{\"id\": 1, \"method\": \"set_bright\", \"params\": [100, \"smooth\", 30000]}",
        "raw_parameters": null,
        "response": "{\"method\":\"props\",\"params\":{\"active_bright\":100,\"nl_br\":100}}",
        "started": [
            2022,
            1,
            10,
            20,
            30,
            40,
            299970222
        ]
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

| HTTP   | Végpont                           | Leírás                                                                                          |
| ------ | ----------------------------------| ----------------------------------------------------------------------------------------------- |
| GET    | `/api/devices`                    | Listaként visszatér az adatbázisban tárolt összes Device entitással.                            |
| POST   | `/api/devices`                    | Beküld az adatbázisba egy Device entitást. Az id automatikusan kerül hozzárendelésre.           |
| GET    | `/api/devices/{identifier}`       | Listaként visszatér az egyező azonosítóval rendelkező (id, uid, name) Device entitásokkal.      |
| GET    | `/api/devices/id/{id}`            | Lekérdezi az adott id alatt található Device entitást.                                          |
| PUT    | `/api/devices/id/{id}`            | Az adott id alá létrehoz egy új Device entitást, vagy a meglévőt frissíti.                      |
| DELETE | `/api/devices/id/{id}`            | Törli az adott id alatti Device entitást.                                                       |
| GET    | `/api/devices/uid/{uid}`          | Lekérdezi az adott uid alatt található Device entitást.                                         | 
| PUT    | `/api/devices/uid/{uid}`          | Az adott uid alá létrehoz egy új Device entitást, vagy a meglévőt frissíti. (Ha új: auto id)    |
| DELETE | `/api/devices/uid/{uid}`          | Törli az adott uid alatti Device entitást.                                                      |
| GET    | `/api/devices/name/{name}`        | Lekérdezi az adott név alatt található Device entitást.                                         |
| PUT    | `/api/devices/name/{name}`        | Az adott név alá létrehoz egy új Device entitást, vagy a meglévőt frissíti. (Ha új: auto id)    |
| DELETE | `/api/devices/name/{name}`        | Törli az adott név alatti Device entitást.                                                      |
| GET    | `/api/devices/ip/{ip}`            | Listaként visszatér az egyező IP címmel rendelkező Device entitásokkal.                         |


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

| HTTP   | Végpont                           | Leírás                                                                                          |
| ------ | ----------------------------------| ----------------------------------------------------------------------------------------------- |
| GET    | `/api/commands`                   | Listaként visszatér az adatbázisban tárolt összes Command entitással.                           |
| POST   | `/api/commands`                   | Beküld az adatbázisba egy Command entitást. Az id automatikusan kerül hozzárendelésre.          |
| GET    | `/api/commands/{identifier}`      | Listaként visszatér az egyező azonosítóval rendelkező (id, name) Command entitásokkal.          |
| GET    | `/api/commands/id/{id}`           | Lekérdezi az adott id alatt található Command entitást.                                         |
| PUT    | `/api/commands/id/{id}`           | Az adott id alá létrehoz egy új Command entitást, vagy a meglévőt frissíti.                     |
| DELETE | `/api/commands/id/{id}`           | Törli az adott id alatti Command entitást.                                                      |
| GET    | `/api/commands/name/{name}`       | Lekérdezi az adott név alatt található Command entitást.                                        |
| PUT    | `/api/commands/name/{name}`       | Az adott név alá létrehoz egy új Command entitást, vagy a meglévőt frissíti. (Ha új: auto id)   |
| DELETE | `/api/commands/name/{name}`       | Törli az adott név alatti Command entitást.                                                     |



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

| HTTP   | Végpont                           | Leírás                                                                                          |
| ------ | ----------------------------------| ----------------------------------------------------------------------------------------------- |
| GET    | `/api/jobs`                       | Listaként visszatér az adatbázisban tárolt összes Job entitással.                               |
| POST   | `/api/jobs`                       | Beküld az adatbázisba egy Job entitást. Az id automatikusan kerül hozzárendelésre.              |
| GET    | `/api/jobs/{identifier}`          | Listaként visszatér az egyező azonosítóval rendelkező (id, uid, name) Job entitásokkal.         |
| GET    | `/api/jobs/id/{id}`               | Lekérdezi az adott id alatt található Job entitást.                                             |
| PUT    | `/api/jobs/id/{id}`               | Az adott id alá létrehoz egy új Job entitást, vagy a meglévőt frissíti.                         | 
| POST   | `/api/jobs/id/{id}`               | Az adott id alatti Job entitást lefuttatja a kérés tözseként átadott paraméterekkel.            |
| DELETE | `/api/jobs/id/{id}`               | Törli az adott id alatti Job entitást.                                                          |
| GET    | `/api/jobs/name/{name}`           | Lekérdezi az adott név alatt található Job entitást.                                            | 
| PUT    | `/api/jobs/name/{name}`           | Az adott név alá létrehoz egy új Job entitást, vagy a meglévőt frissíti. (Ha új: auto id)       |
| POST   | `/api/jobs/name/{name}`           | Az adott név alatti Job entitást lefuttatja a kérés tözseként átadott paraméterekkel.           |
| DELETE | `/api/jobs/name/{name}`           | Törli az adott név alatti Job entitást.                                                         |




---

### Run

A `Run` a `Job` egy lefutását összegzi. Adatbázisban nem kerül tárolásra, így értéke `null`, ha a `Job` objektum még nem lett lefuttatva. A következő attribútumokkal rendelkezik: 

| Név           | Típus              | Leírás                                                                                                  | 
| ------------- | ------------------ | ------------------------------------------------------------------------------------------------------- |
| started       | LocalDateTime      | Az adott Job meghívásának időpontja. Például: "2022-01-10T20:30:40.299970222"                           |
| rawParameters | String             | Ha @PathVariable által átadott paraméterrel / paraméterlistával hívjuk meg, az itt jelenik meg.         |
| parameters    | Object[]           | Az átadott paraméterek listája. A feldolgozott rawParameters is megjelenik itt.                         |
| payload       | String             | A Command entitás template-je, és a paraméterek által létrehozott body. A Device felé ez lett kiküldve. |
| response      | String             | A payload-ra adott válasz, feldolgozás nélküli, nyers formában.                                         |


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
