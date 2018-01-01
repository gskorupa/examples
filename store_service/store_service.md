# Microservice with Java in a flash? Not a problem at all.

We will show how simple and fast you can build a basic microservice using Cricket. And I don't mean something that only displays "Hello World", but an almost fully functional prototype with REST API, database and web GUI.

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
wget https://github.com/gskorupa/Cricket/releases/download/1.2.33/cricket-1.2.33.jar
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
INFO:2018-01-01 12:07:07 +0000:   __|  \  | __|  Cricket
INFO:2018-01-01 12:07:07 +0000:  (    |\/ | _|   Microservices Framework
INFO:2018-01-01 12:07:07 +0000: \___|_|  _|_|    version 1.2.33
INFO:2018-01-01 12:07:07 +0000: 
INFO:2018-01-01 12:07:07 +0000: # Service: StoreSvc
INFO:2018-01-01 12:07:07 +0000: # UUID: a4e600b8-1f1b-4c4b-8deb-4ba73bf96957
INFO:2018-01-01 12:07:07 +0000: # NAME: CricketService
INFO:2018-01-01 12:07:07 +0000: #
INFO:2018-01-01 12:07:07 +0000: # HTTP listening on port 8080
INFO:2018-01-01 12:07:07 +0000: #
INFO:2018-01-01 12:07:07 +0000: # Started in 104ms. Press Ctrl-C to stop
```
The service exposes our store API at http://localhost:8080/api/store

We can add new product by sending POST request:
```
curl -d "id=p001&sku=MPEXPL01&unit=pcs&stock=10.0&price=1.5&name=My first example product" http://localhost:8080/api/store
```

After adding one or more product we can read all store content or selected product by sending GET requests:
```
# read all products
curl http://localhost:8080/api/store
# read product with ID equals p001
curl http://localhost:8080/api/store/p001
```

## Now let's see how this is done 

Assuming that you have run the example as described in the previous section, we can look at the Product source code. To keep things simple the code has no getters and setters but it's up to you, as well as adding more fields.

```
//Product.java
public class Product {

    public String id;
    public String name;
    public String sku;
    public String unit;
    public Double stock;
    public Double unitPrice;

    public Product() {
    }
}
```

Then let's look at the service class, one that will be run by the Cricket's runtime. In our example this is StoreService.
Major part of the code is default for a typical Cricket service, providing event support, logging, key-value database initialization and file readout for embeded http server.

What we are interested in are two methods implementing the store business logic (exposed as the store API): `addProduct` and `getProducts`.
```

```

The last parto of the puzzle is the configuration file where all adapters used by our service and the service itself is configured. 
The JSON format of the file is not complicated and should be self explanatory.
Zwróćmy uwagę na najważniejsze dla naszego przykładu elementy.

TODO

Modyfikujemy cricket.json deklarujac standardowy adapter http nasluchujacy na ścieżce /api/store (jak pamiętamy, nasz serwis będzie dostępny pod adresem http://localhost:8080)

Nasz adapter uzyska dostęp do dedykowanych dla niego metod serwisu dzięki anotacjom.
Rezultat wykonania tych metod zostań następnie przetransformowany do typu application/json i odesłany.

Możemy już przetestować.

W kolejnym odcinku - WEB GUI
