
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.HashMap;

import service.CoffeeVendingMachineService;

public class VendingShop {
    public static void main(String args[]){
        JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try{
            // Reading input file and fetch number of outlets, initial stock and orders
            Object obj = jsonParser.parse(new FileReader("/Users/mtripathi/Downloads/vendingshop/src/main/java/input.json"));
            JSONObject input = (JSONObject)obj;
            JSONObject machine = (JSONObject) input.get("machine");

            HashMap<String,Long> initialStock= (HashMap<String,Long>) machine.get("total_items_quantity");
            HashMap<String,Object> orders = (HashMap<String,Object>) machine.get("beverages");
            JSONObject outlets = (JSONObject) machine.get("outlets");
            int outLetCount = (int) (long)outlets.get("count_n");

            // Getting a coffee vending machine with outlets and initial ingredients
            CoffeeVendingMachineService coffeeVendingMachineService = new CoffeeVendingMachineService(outLetCount);
            coffeeVendingMachineService.fillCoffeeVendingMachineWithGivenStock(initialStock);

            // Start serving orders in async manner
            coffeeVendingMachineService.serveOrders(orders);

            // Stop outlets for further processing
            coffeeVendingMachineService.shutDownMachine();


        }catch (Exception e){
            System.out.println("Unknown Exception occurred");
        }
    }
}
