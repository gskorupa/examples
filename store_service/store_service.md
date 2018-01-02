# Microservice with Java in a flash? Not a problem at all.

We will show how simple and fast you can build a basic microservice using Cricket. And I don't mean something that only displays "Hello World", but an almost fully functional prototype with REST API, database and web GUI.

Let's implement a Product store .

To do the we will need:

* the Cricket MSF library  
* Product class - reflecting our real product
* StoreService class - implementing our service logic
* configuration file

## First the method for the impatient, i.e. microservice in 5 minutes.

* download library and the source codes
* compile
* run
* test

```
# create the service folder
mkdir mystore
cd mystore

# download library and source codes
wget https://github.com/gskorupa/Cricket/releases/download/1.2.34/cricket-1.2.34.jar
wget https://github.com/signocom/examples/raw/master/store_service/Product.java
wget https://github.com/signocom/examples/raw/master/store_service/StoreService.java
wget https://github.com/signocom/examples/raw/master/store_service/cricket.json

# complie
javac -cp.:cricket-1.2.34.jar StoreService.java

# run
java -cp .:cricket-1.2.34.jar org.cricketmsf.Runner -r -c cricket.json
```

If all goes OK the servcice should print on the terminal:
```
INFO:2018-01-01 12:07:07 +0000:   __|  \  | __|  Cricket
INFO:2018-01-01 12:07:07 +0000:  (    |\/ | _|   Microservices Framework
INFO:2018-01-01 12:07:07 +0000: \___|_|  _|_|    version 1.2.34
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

We can add new product by sending POST request (we can use cURL application to do this):
```
curl -i -d "id=p001&sku=MPEXPL01&unit=pcs&stock=10.0&price=1.5&name=My first example product" http://localhost:8080/api/store
```

After adding one or more product we can read all store content or selected product by sending GET requests:
```
# read all products
curl -i http://localhost:8080/api/store

# read product with ID equals p001
curl -i http://localhost:8080/api/store/p001
```

## Now let's see how this is done 

### Product
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
    
    // getters and setters
}
```

### Service
Then let's look at the service class, one that will be run by the Cricket's runtime. In our example this is StoreService.
Major part of the code is default for a typical Cricket service, providing event support, logging, key-value database initialization and file readout for embeded http server.

What we are interested in are two methods implementing the store business logic (exposed as the store API): `addProduct` and `getProducts`.
```
/**
 * Provides information about products in the store. 
 * The search parameter is a product ID which is passed 
 * as an URL extension related to the context parameter of the StoreService adapter.
 * This means that if the context is "/api/store" and the URL is "/api/store/p001", 
 * then the resulting ID will be "p001".
 * 
 * @param requestEvent Event object encapsulating HTTP request received by
 * the StoreService adapter. The method is binded to the adapter using annotation.
 * 
 * @return StandardResult object encapsulating a Product or a List of Products, depending 
 * on request.
 */
@HttpAdapterHook(adapterName = "StoreAPI", requestMethod = "GET")
public Object getProducts(Event requestEvent) {
    String productToSearch = requestEvent.getRequest().pathExt;
    StandardResult result = new StandardResult();
    result.setCode(HttpAdapter.SC_OK);
    try {
        if (!productToSearch.isEmpty()) {
            // read object with key==productToSearch from database
            Product product = (Product) database.get("store", productToSearch);
            if (product != null) {
                 result.setData(product);
            } else {
                result.setCode(HttpAdapter.SC_NOT_FOUND);
            }
        } else {
            ArrayList<Product> list = new ArrayList<>();
            // read objects stored in the "store" table of the database
            // and put into list
            @SuppressWarnings("unchecked")
            Map<String, Product> map = database.getAll("store");
            map.keySet().forEach(key -> {
                list.add((Product) map.get(key));
            });
            result.setData(list);
        }
    } catch (KeyValueDBException ex) {
        Kernel.handle(Event.logSevere(this, ex.getMessage()));
    } catch (ClassCastException ex) {
        //it shouldn't happen
        ex.printStackTrace();
        System.exit(0);
    }
    return result;
}

/**
 * Creates a Product object based on the request parameters. The product object is stored
 * in the "store" table of the configured database.  
 * 
 * @param requestEvent Event object encapsulating HTTP request received by
 * the StoreService adapter. The method is binded to the adapter using annotation.
 * 
 * @return StandardResult object with response code set according to the method result.
 */
@HttpAdapterHook(adapterName = "StoreAPI", requestMethod = "POST")
public Object addProduct(Event requestEvent) {
    StandardResult result = new StandardResult();
    try {
        Product product = new Product();
        product.id = requestEvent.getRequestParameter("id");
        product.name = requestEvent.getRequestParameter("name");
        product.sku = requestEvent.getRequestParameter("sku");
        product.unit = requestEvent.getRequestParameter("unit");
        product.stock = Double.parseDouble(requestEvent.getRequestParameter("stock"));
        product.unitPrice = Double.parseDouble(requestEvent.getRequestParameter("price"));
        // product update requests must be send with PUT not POST method
        if(database.containsKey("store", product.id)){
            result.setCode(HttpAdapter.SC_CONFLICT);
            result.setData(product.id + " already defined");
        }else{
            database.put("store", product.id, product);
            result.setCode(HttpAdapter.SC_CREATED);
            result.setData(product.id + " added");
        }
    } catch (NumberFormatException | KeyValueDBException e) {
        result.setCode(HttpAdapter.SC_BAD_REQUEST);
        result.setData(e.getMessage());
    }
    return result;
}
```

### Configuration
The last part of the puzzle is the configuration file where all adapters used by our service and the service itself are configured. The JSON format of the file is not complicated and should be self explanatory.
```
{
    "@type": "org.cricketmsf.config.ConfigSet",
    "description": "This is a sample configuration",
    "services": [
        {
            "@type": "org.cricketmsf.config.Configuration",
            "id": "StoreSvc",
            "service": "StoreService",
            "properties": {
                "filter": "org.cricketmsf.SecurityFilter",
                "SRVC_NAME_ENV_VARIABLE": "CRICKET_NAME",
                "cors": "Access-Control-Allow-Origin:*",
                "port": "8080",
                "host": "0.0.0.0",
                "threads": "0",
                "keystore": "./data/cricket_publickeystore.jks",
                "ssl": "false",
                "keystore-password": "cricket15$#17",
                "time-zone": "GMT"
            },
            "adapters": {
                "StoreAPI": {
                    "@type": "org.cricketmsf.config.AdapterConfiguration",
                    "name": "StoreAPI",
                    "interfaceName": "HttpAdapterIface",
                    "classFullName": "org.cricketmsf.in.http.StandardHttpAdapter",
                    "description": "The service class responsible for the product store businness logic.",
                    "properties": {
                        "silent-mode": "false",
                        "context": "/api/store"
                    }
                },
                "Scheduler": {
                    "@type": "org.cricketmsf.config.AdapterConfiguration",
                    "name": "Scheduler",
                    "interfaceName": "SchedulerIface",
                    "classFullName": "org.cricketmsf.in.scheduler.Scheduler",
                    "properties": {
                        "path": ".",
                        "file": "scheduler.xml",
                        "envVariable": "SCHEDULER_DB_PATH"
                    }
                },
                "Database": {
                    "@type": "org.cricketmsf.config.AdapterConfiguration",
                    "name": "Database",
                    "interfaceName": "KeyValueDBIface",
                    "classFullName": "org.cricketmsf.out.db.KeyValueDB",
                    "properties": {
                        "path": ".",
                        "name": "local"
                    }
                },
                "Logger": {
                    "@type": "org.cricketmsf.config.AdapterConfiguration",
                    "name": "Logger",
                    "interfaceName": "LoggerAdapterIface",
                    "classFullName": "org.cricketmsf.out.log.StandardLogger",
                    "properties": {
                        "console": "true",
                        "log-file-name": "./cricket%g.log",
                        "level": "FINEST",
                        "name": "EchoService",
                        "max-size": "1000000",
                        "count": "10"
                    }
                },
                "Dispatcher": {
                    "name": "Dispatcher",
                    "interfaceName": "DispatcherIface",
                    "classFullName": "org.cricketmsf.out.EventDispatcherAdapter",
                    "description": "default dispatcher does nothing",
                    "properties": {
                    }
                }
            }
        }
    ]
}
```
Let's pay attention to the most important elements for our example.

In the "adapters" section, we have defined an adapter called "StoreAPI". This adapter has a declared compatibility with the HttpAdapterIface interface, which means that Cricket runtime enviromnent:
* considers it as an inbound adapter type
* binds it to the embeded HTTP server as handler for "/api/store" context (defined in the adapter properties)

As an implementation class of the adapter, we use `org.cricketmsf.in.http.StandardHttpAdapter` offering standard support for incoming requests and automatic serialization of objects transferred by service methods to JSON, XML or CSV format in response. The working adapter finds the right service methods thanks to `@HttpAdapterHook` annotations.

### Components binding
Probably the reader of the article already realised how the elements of the service are linked together.

In short, the Dependency Injection mechanism in the Cricket framework is controlled in two places:

* the configuration file defines the late-binding of adapters interfaces with their implementing classes
* in the service source code, adapters are bound with the handling methods by using annotations

This approach keeps Dependency Injection magic at the lowest possible level, so that we can quickly realize how the service works.

# Summary
In the article we saw how to quickly prepare a prototype of the service, taking as an example a simple product store. 

We used a small part of the Cricket framework functionality, such as the built-in HTTP, key-value database, automatic object serialization by adapters and Dependency Injection controlled by a configuration file.

It would also be nice to have a simple web interface that allows us to use the service through a web browser. But this will be the subject of another article.
