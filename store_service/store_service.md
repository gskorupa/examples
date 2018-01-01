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
aaa
```
The service exposes our store API at http://localhost:8080/api/store

We can add new product by sending POST request
```
curl --data="id=p001&sku=abc123&unit=kg&unitPrice=1.5&name='My first product'" http://localhost:8080/api/store
```
and read all stored products or selected product by sending GET requests
```
curl http://localhost:8080/api/store

curl http://localhost:8080/api/store/p001
```

## Now let's see how this is done 

Assuming that you have run the example as described in the previous section, we can look at the Product source Code. To keep Things simple the example has no getters and setters but it's up to you, as well as adding more fields.

```
//Product.java
public class Product {

    public String id;
    public String name;
    public String sku;
    public String unit;
    public Double unitPrice;

    public Product() {
    }
}
```

Then let's create our service class, one that will be run by the Cricket's runtime. In our example this is StoreService. Let's look inside the file.

Major part of the code is default for a typical Cricket service, providing event support, logging, key-value database initialization and file readout for embeded http server.
The business logic of our store API is contained in two methods: `addProduct` and `getProducts`.

```

```

Now we need to look at the configuration file where all adapters used by our service and the service itself is configured. 
The JSON format of the file is not complicated and should be self explanatory.
Zwróćmy uwagę na najważniejsze dla naszego przykładu elementy.

TODO

Modyfikujemy cricket.json deklarujac standardowy adapter http nasluchujacy na ścieżce /api/store (jak pamiętamy, nasz serwis będzie dostępny pod adresem http://localhost:8080)

Nasz adapter uzyska dostęp do dedykowanych dla niego metod serwisu dzięki anotacjom.
Rezultat wykonania tych metod zostań następnie przetransformowany do typu application/json i odesłany.

Możemy już przetestować.

W kolejnym odcinku - WEB GUI
