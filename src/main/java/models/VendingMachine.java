package models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Exceptions.NotAvailableException;
import Exceptions.NotSufficientException;

// Basic vending machine which has some outlets and some stocks of ingredients
abstract class VendingMachine {

    private HashMap<String, Long> ingredientsInStock;

    // For parallel surving of drinks from outlets
    // This will help in running multiple threads of the bevergae maker process
    ExecutorService executorService;

    // Constructor to initialize vending machine with its stock with empty quantity, number of outlets
    // executor service denotes a parallel serving mechanism through outlets
    public VendingMachine(int outlets){
        this.ingredientsInStock = new HashMap<>();
        executorService = Executors.newFixedThreadPool(outlets);
    }

    // Adding ingredients to the vending machine, used in initialization of machine as well as in adding an ingredient in middle
    public void addIngredient(String ingredientName, long ingredientQuantity){
        this.ingredientsInStock.put(ingredientName, ingredientQuantity);
    }

    // Updating ingredient in vending machine - used to refill an existing ingredient
    public void updateIngredient(String ingredientName, long ingredientQuantity){
        long previousQuantity = ingredientsInStock.get(ingredientName);
        long newQuantity = previousQuantity + ingredientQuantity;
        ingredientsInStock.replace(ingredientName, newQuantity);
    }

    // Stopping the vending machine for further processing - similar to disable outlets
    public void shutDownMachine(){
        executorService.shutdown();
    }

    public HashMap<String, Long> getCurrentStock(){
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
            return ingredientsInStock;
        } catch (InterruptedException e) {
            return ingredientsInStock;
        }
    }


    // Consume ingredients from the stock as needed by the recipe of the required beverage
    // It is synchronized so that only one recipe(thread) would be able to access the stock of ingredient
    // This is a critical section as shared resource of stock might be accessed by different threads at the same time
    protected synchronized void consumeIngredients(String beverageName, HashMap<String, Long> recipe) throws Exception{
        validateStockAgainstRecipe(beverageName, recipe);
        recipe.keySet().forEach(recipeIngredientName->{
            long recipeIngredientQuantity = recipe.get(recipeIngredientName);
            long availableQuantity = ingredientsInStock.get(recipeIngredientName);
            availableQuantity = availableQuantity - recipeIngredientQuantity;
            ingredientsInStock.replace(recipeIngredientName, availableQuantity);
        });
    }

    // Helper function used by above synchronized method to check the sufficiency and availability of stock
    // throws exception depending on whether stock is enough for the drink to be made
    private void validateStockAgainstRecipe(String beverageName, HashMap<String, Long> recipe) throws Exception{
        Iterator iterator = recipe.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)iterator.next();
            long recipeIngredientQuantity = (long) mapElement.getValue();
            String recipeIngredientName = (String) mapElement.getKey();
            if(!ingredientsInStock.containsKey(recipeIngredientName)){
                String errorMessage = beverageName + " cannot be prepared because " + recipeIngredientName + " is not available\n";
                throw new NotAvailableException(errorMessage);
            }else{
                long availableQuantity = ingredientsInStock.get(recipeIngredientName);
                if(availableQuantity < recipeIngredientQuantity){
                    String errorMessage =  beverageName + " cannot be prepared because " + recipeIngredientName + " is not sufficient\n";
                    throw new NotSufficientException(errorMessage);
                }
            }
        }
    }

    abstract public void makeBeverage(String beverageName, HashMap<String, Long> recipe);
}
