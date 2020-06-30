import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import java.util.HashMap;

import service.CoffeeVendingMachineService;

public class CoffeeVendingMachineServiceTest {

    private CoffeeVendingMachineService coffeeVendingMachineService;
    private final int outLetCount = ((int) (Math.random()*3)) + 1 ;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void init(){
        coffeeVendingMachineService = new CoffeeVendingMachineService(outLetCount);
    }

    @Test
    public void testCoffeeMachineWithEmptyStock(){
        HashMap<String, Long> currentStock = coffeeVendingMachineService.getCurrentStock();
        assertEquals(currentStock.size(),0);
    }

    @Test
    public void testCoffeeMachineWithNonEmptyStock(){
        HashMap<String, Long> initialStock = initializeCoffeeVendingMachineWithSomeStock();
        HashMap<String, Long> currentStock = coffeeVendingMachineService.getCurrentStock();
        assertEquals(currentStock, initialStock);
    }

    @Test
    public void testCoffeeMachineValidOrder(){
        HashMap<String, Long> initialStock = initializeCoffeeVendingMachineWithSomeStock();
        HashMap<String,Object> orders = new HashMap<>();

        // create new order whose one ingredient is less than stock ingredient by some value
        HashMap<String, Long> newOrderRecipe = (HashMap<String, Long>)initialStock.clone();
        String firstKey = newOrderRecipe.keySet().stream().findFirst().get();
        long value = newOrderRecipe.get(firstKey);
        long reduceBy = 10;
        newOrderRecipe.replace(firstKey, value-reduceBy);

        // request new order of this beverage
        final String beverageName = "sample_beverage";
        orders.put(beverageName, newOrderRecipe);
        coffeeVendingMachineService.serveOrders(orders);
        HashMap<String, Long> finalStock = coffeeVendingMachineService.getCurrentStock();

        // Check if inital and final stock have difference
        assertNotSame(initialStock, finalStock);
        assertEquals((long)finalStock.get(firstKey), reduceBy);
    }

    @Test
    public void testCoffeeMachineWithInSufficientOrder(){
        HashMap<String, Long> initialStock = initializeCoffeeVendingMachineWithSomeStock();
        HashMap<String,Object> orders = new HashMap<>();

        // create a new order with one of the ingredient greater than stock
        HashMap<String, Long> newOrderRecipe = (HashMap<String, Long>)initialStock.clone();
        String firstKey = newOrderRecipe.keySet().stream().findFirst().get();
        long value = newOrderRecipe.get(firstKey);
        newOrderRecipe.replace(firstKey, value+10);

        // request new order of this beverage
        final String beverageName = "sample_beverage";
        orders.put(beverageName, newOrderRecipe);
        coffeeVendingMachineService.serveOrders(orders);
        HashMap<String, Long> finalStock = coffeeVendingMachineService.getCurrentStock();
        assertEquals(initialStock, finalStock);
    }

    @Test
    public void testCoffeeMachineWithUnAvailableOrder(){
        HashMap<String, Long> initialStock = initializeCoffeeVendingMachineWithSomeStock();
        HashMap<String,Object> orders = new HashMap<>();


        HashMap<String, Long> newOrderRecipe = (HashMap<String, Long>)initialStock.clone();

        // putting some unavailable ingredient in new order
        String unavailableIngredientToPut = "sample_unavailable_ingredient";
        newOrderRecipe.put(unavailableIngredientToPut,100L);

        // request new order of this beverage
        final String beverageName = "sample_beverage";
        orders.put(beverageName, newOrderRecipe);
        coffeeVendingMachineService.serveOrders(orders);
        HashMap<String, Long> finalStock = coffeeVendingMachineService.getCurrentStock();
        assertEquals(initialStock, finalStock);
    }

    private HashMap<String, Long> initializeCoffeeVendingMachineWithSomeStock(){
        HashMap<String, Long> initialStock = new HashMap<>();
        initialStock.put("hot_water", 500L);
        initialStock.put("hot_milk",500L);
        initialStock.put("ginger_syrup",100L);
        initialStock.put("sugar_syrup",100L);
        initialStock.put("tea_leaves_syrup",100L);
        coffeeVendingMachineService.fillCoffeeVendingMachineWithGivenStock(initialStock);
        return initialStock;
    }
}
