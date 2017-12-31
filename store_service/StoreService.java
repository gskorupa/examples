/*
Copyright 2017 by Grzegorz Skorupa
*/
import org.cricketmsf.Event;
import org.cricketmsf.Kernel;
import java.util.ArrayList;
import java.util.Map;
import org.cricketmsf.annotation.EventHook;
import org.cricketmsf.annotation.HttpAdapterHook;
import org.cricketmsf.in.http.HttpAdapter;
import org.cricketmsf.in.http.HttpAdapterIface;
import org.cricketmsf.in.http.StandardResult;
import org.cricketmsf.in.scheduler.SchedulerIface;
import org.cricketmsf.out.db.KeyValueDBException;
import org.cricketmsf.out.db.KeyValueDBIface;
import org.cricketmsf.out.log.LoggerAdapterIface;

public class StoreService extends Kernel {

    // Every adapter implementing HttpAdapter interface is responsible for accepting
    // HTTP requests, packing them into an Event object, forwarding to the service classs
    // and generating HTTP response from the service response
    //
    // In REST API we need dedicated adapter for each data type
    // The storeAdapter is responsible for request sent to the product repository
    HttpAdapterIface storeAdapter = null;

    // We will need database to store products data
    KeyValueDBIface database = null;

    // scheduler and logger adapters are always useful
    SchedulerIface scheduler = null;
    LoggerAdapterIface logAdapter = null;

    @Override
    public void getAdapters() {
        // standard Cricket adapters
        storeAdapter = (HttpAdapterIface) getRegistered("StoreService");
        database = (KeyValueDBIface) getRegistered("Database");
        scheduler = (SchedulerIface) getRegistered("Scheduler");
        logAdapter = (LoggerAdapterIface) getRegistered("Logger");
    }

    @Override
    public void runInitTasks() {
        try {
            database.addTable("store", 1000, true);
        } catch (KeyValueDBException ex) {
            Kernel.handle(Event.logInfo(this, ex.getMessage()));
        }
    }

    /**
     * Get store products
     */
    @HttpAdapterHook(adapterName = "StoreService", requestMethod = "GET")
    public Object getProducts(Event requestEvent) {
        String productToSearch = requestEvent.getRequest().pathExt;
        StandardResult result = new StandardResult();
        result.setCode(HttpAdapter.SC_OK);
        try {
            if (!productToSearch.isEmpty()) {
                Product product = (Product) database.get("store", productToSearch);
                if(product!=null){
                    result.setData(product);
                }else{
                    result.setCode(404);
                    result.setData("");
                }
            } else {
                ArrayList<Product> list = new ArrayList<>();
                @SuppressWarnings("unchecked")
                Map<String,Product> map = database.getAll("store");
                map.keySet().forEach(key -> {
                    list.add((Product) map.get(key));
                });
                result.setData(list);
            }
        } catch (KeyValueDBException ex) {
            Kernel.handle(Event.logSevere(this, ex.getMessage()));
        } catch (ClassCastException ex){
            //it shouldn't happen
            ex.printStackTrace();
            System.exit(0);
        }
        return result;
    }

    @HttpAdapterHook(adapterName = "StoreService", requestMethod = "POST")
    public Object addProduct(Event requestEvent) {
        StandardResult result = new StandardResult();
        Product product = new Product();
        product.id = requestEvent.getRequestParameter("id");
        product.name = requestEvent.getRequestParameter("name");
        product.sku = requestEvent.getRequestParameter("sku");
        product.unit = requestEvent.getRequestParameter("unit");
        try {
            product.quantity = Integer.parseInt(requestEvent.getRequestParameter("quantity"));
            product.unitPrice = Double.parseDouble(requestEvent.getRequestParameter("price"));
            database.put("store", product.id, product);
            result.setCode(HttpAdapter.SC_CREATED);
        } catch (NumberFormatException | KeyValueDBException e) {
            result.setCode(HttpAdapter.SC_BAD_REQUEST);
            result.setData(e.getMessage());
        }
        return result;
    }

    @EventHook(eventCategory = Event.CATEGORY_LOG)
    public void logEvent(Event event) {
        logAdapter.log(event);
    }

    @EventHook(eventCategory = Event.CATEGORY_HTTP_LOG)
    public void logHttpEvent(Event event) {
        logAdapter.log(event);
    }

    /*
    If not handled by other methods, we will simply log the event payload
     */
    @EventHook(eventCategory = "*")
    public void processEvent(Event event) {
        handleEvent(Event.logInfo("StoreService", event.getPayload().toString()));
    }

}
