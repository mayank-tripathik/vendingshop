package service;

import org.json.simple.JSONObject;

import java.util.HashMap;

import models.CoffeeVendingMachine;

public class CoffeeVendingMachineService {

    private CoffeeVendingMachine coffeeVendingMachine;

    public CoffeeVendingMachineService(int outLetCount){
        coffeeVendingMachine = new CoffeeVendingMachine(outLetCount);
    }

    // initilaize the coffee vending machine with given set of outlets and initial stock
    public CoffeeVendingMachine fillCoffeeVendingMachineWithGivenStock(HashMap<String, Long> initialStock) {
        initialStock.keySet().forEach(key -> {
            String ingredientName = key;
            long ingredientQuantity = initialStock.get(key);
            coffeeVendingMachine.addIngredient(ingredientName, ingredientQuantity);
        });
        return coffeeVendingMachine;
    }

    // Serve each order in async manner via threads
    public void serveOrders(HashMap<String, Object> orders){
        // passing each orders to vending machine
        orders.keySet().forEach(key->{
            String beverageName = key;
            HashMap<String, Long> recipe = (HashMap<String, Long>) orders.get(key);
            coffeeVendingMachine.makeCoffee(beverageName, recipe);
        });
    }

    public HashMap<String, Long> getCurrentStock(){
        return coffeeVendingMachine.getCurrentStock();
    }

    public void shutDownMachine(){
        coffeeVendingMachine.shutDownMachine();
    }
}
