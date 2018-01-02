// StoreService.java

import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import java.util.ArrayList;
import java.util.Map;
import org.cricketmsf.annotation.EventHook;
import org.cricketmsf.annotation.HttpAdapterHook;
import org.cricketmsf.in.http.HttpAdapter;
import org.cricketmsf.in.http.StandardResult;
import org.cricketmsf.in.scheduler.SchedulerIface;
import org.cricketmsf.out.db.KeyValueDBException;
import org.cricketmsf.out.db.KeyValueDBIface;
import org.cricketmsf.out.log.LoggerAdapterIface;

public class StoreService extends Kernel {
    
    // Notice:
    // There is no need to declare variables for inbound adapters such as 
    // HttpGenAdapter or StoreAdapter (declared in cricket.json) as far as
    // there is no need to access their methods explicitely from the service code

    /**
     * Adapter of the database used to store products.
     */
    KeyValueDBIface database = null;

    /**
     * Adapter responsible for scheduling of events.
     */
    SchedulerIface scheduler = null;

    /**
     * Adapter used for logging
     */
    LoggerAdapterIface logAdapter = null;

    /**
     * Method responsible for binding adapter variables to its implementing
     * classes based on configuration. This method is invoked by the Cricket
     * runtime on start.
     */
    @Override
    public void getAdapters() {
        //storeAdapter = (HttpAdapterIface) getRegistered("StoreService");
        database = (KeyValueDBIface) getRegistered("Database");
        scheduler = (SchedulerIface) getRegistered("Scheduler");
        logAdapter = (LoggerAdapterIface) getRegistered("Logger");
    }

    /**
     * Can be use to do required tasks before exposing the service interfaces. 
     * In this case we need to create a table in the default database,
     * used to store Product objects.
     */
    @Override
    public void runInitTasks() {
        try {
            database.addTable("store", 1000, true);
        } catch (KeyValueDBException ex) {
            Kernel.handle(Event.logInfo(this, ex.getMessage()));
        }
    }

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

    // We are leaving these standard methods below unchanged.

    /**
     * Logs event of category CATEGORY_LOG using log adapter.
     * @param event 
     */
    @EventHook(eventCategory = Event.CATEGORY_LOG)
    public void logEvent(Event event) {
        logAdapter.log(event);
    }

    /**
     * Logs event of category CATEGORY_HTTP_LOG using log adapter.
     * @param event 
     */
    @EventHook(eventCategory = Event.CATEGORY_HTTP_LOG)
    public void logHttpEvent(Event event) {
        logAdapter.log(event);
    }

    /**
     * Logs event of category not handled by other handler methods.
     * @param event 
     */
    @EventHook(eventCategory = "*")
    public void processEvent(Event event) {
        handleEvent(Event.logInfo("StoreService", event.getPayload().toString()));
    }

}
