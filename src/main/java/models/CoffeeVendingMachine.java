package models;

import java.util.HashMap;

import Exceptions.NotAvailableException;
import Exceptions.NotSufficientException;

// Other vending machine of different types can be created by extending the basic vending machine
public class CoffeeVendingMachine extends VendingMachine {

    public CoffeeVendingMachine(int outlets){
        super(outlets);
    }

    // This will call the runnable instance of coffee maker
    public void makeCoffee(String beverageName, HashMap<String, Long> recipe) {
        executorService.execute(new CoffeeMaker(beverageName, recipe));
    }

    // Initializing steps for this specific machine can be put inside.
    private void initializeCoffeeMaking(String beverageName) {
        System.out.println("Initialize "  + beverageName +  " making process");
    }

    // Finishing steps for this specific machine can be put inside.
    private void finishCoffeeMaking(String beverageName) {
        System.out.println(beverageName +  " is prepared");
    }

    // Each extension of a vending machine has to implement a beverage maker
    // Following class denotes a runnable class which mimics a beverage maker
    // A beverage maker consumes the ingredients and then makes the drink
    private class CoffeeMaker implements Runnable{
        String beverageName;
        HashMap<String, Long> recipe;

        public CoffeeMaker(String beverageName, HashMap<String, Long> recipe){
            this.beverageName = beverageName;
            this.recipe = recipe;
        }

        @Override
        public void run() {
            initializeCoffeeMaking(beverageName);
            // Entering into critical section where many thread may contend for common resource - ingredients stock
            // using try and catch to process exceptions if occur
            try {
                consumeIngredients(beverageName, recipe);
                finishCoffeeMaking(beverageName);
            }catch (NotSufficientException e){
                System.err.println(e);
            }catch (NotAvailableException e){
                System.err.println(e);
            }catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
