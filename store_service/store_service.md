# Microservice with Java in a flash? Not a problem at all.

We will show how simple and fast you can build a basic microservice using Cricket. And here I don't mean something that only displays "Hello World", but an almost fully functional prototype with REST API, database and web GUI.

Let's implement a Product store .

To do the we will need:

* the Cricket MSF library  
* Product class - reflecting our real product
* StoreService class - implementing our service logic
* configuration file

## First the method for impatient ones, i. e. the microservice in 5 minutes.

* download library and the source codes
* compile
* run
* test


```
# create service folder
mkdir mystore
cd mystore
# download library and source codes
wget https://github.com/Cricket/releases/1.2.33/cricket-1.2.33.jqr
wget https://github.com/signocom/examples/raw/master/store_service/Product.java
wget https://github.com/signocom/examples/raw/master/store_service/StoreService.java
wget https://github.com/signocom/examples/raw/master/store_service/cricket.json
# complie
javac -cp.:cricket-1.2.33.jar StoreService.java
# run
java -cp .:cricket-1.2.33.jar org.cricketmsf.Runner -r -c cricket.json
```
If all goes OK the servcice should print on the terminal:
```
aaa
```
The service exposes our store API at http://localhost:8080/api/store

We can create add new product by sending POST request
```
curl 
```
and read all stored products or selected product by sending GET requests
```
curl http://localhost:8080/api/store

curl http://localhost:8080/api/store/p001
```

## Now let's see how this is done 

Assuming that you have run the example as described in the previous section, we can look at the Product source Code. To keep Things simple the example has no getters and setters but it's up to you, as well as adding mogę fields.

KOD

Then let's create our service class, one that will be run by the Cricket's runtime. In our example this is StoreService. Let's look inside the file.

Major part of the code is default for a typical Cricket service, providing event support, login, key-value database declaration and file readout for http server.
The business logic of our store API is contained in two methods.

KOD i opis metod

Do uruchomienia serwisu będziemy potrzebowali pliku konfiguracyjnego. Najprościej będzie go wyciągnąć z biblioteki.
Podmieniamy klasę serwisu, konfigurujemy i uruchamiamy. Na razie nasz serwis jeszcze nie robi tego co planujemy, ale jak widać po uruchomieniu nasluchuje na wybranym porcie na requesty http.

Czas zatem uruchomić nasze API.

Modyfikujemy cricket.json deklarujac standardowy adapter http nasluchujacy na ścieżce /api/store (jak pamiętamy, nasz serwis będzie dostępny pod adresem http://localhost:8080)

Nasz adapter uzyska dostęp do dedykowanych dla niego metod serwisu dzięki anotacjom.
Rezultat wykonania tych metod zostań następnie przetransformowany do typu application/json i odesłany.

Możemy już przetestować.

Uwaga - dodać 404 do obecnego kodu.

W kolejnym odcinku - WEB GUI



id: 435a8847cdc64b238bf30f0e52a21c7e
parent_id: f059caf934a1491aa6e065ec6fcaa3ac
created_time: 2017-12-29T21:53:20.739Z
updated_time: 2017-12-31T12:37:34.080Z
is_conflict: 0
latitude: 51.75000000
longitude: 19.46670000
altitude: 0.0000
author: 
source_url: 
is_todo: 0
todo_due: 0
todo_completed: 0
source: joplin
source_application: net.cozic.joplin-cli
application_data: 
order: 0
user_created_time: 2017-12-29T21:53:20.739Z
user_updated_time: 2017-12-31T12:37:34.080Z
type_: 1
