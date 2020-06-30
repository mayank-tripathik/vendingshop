# vending shop
CoffeeVendingShop Assignemnt

About the structure
1. Main method : VendingShop.java // it reads input json file and performs the serving of each beverage using CoffeeVendingMachineService
2. VendingMachine - abstract class which provides the basic functionality of vending machine
3. CoffeeVendingMachine - Class which extends VendingMachine 
4. CoffeeVendingMachineService - Service class to abstract the functionlity of a Coffee vending machine
5. Two exception classes - each for checking availibility and sufficiency of ingredients while preparing beverage
6. Test class - CoffeeVendingMachineServiceTest which test the overall functionality.

Rationale behind the design:
--------------------------
1. Each vending machine should have at least some outlets and a beverage maker inside it. The working of beverage maker depends on the actual implementation of machine.
Hence the basic functionality of vending machine is put in abstract class VendingMachine so that any vending machine(like coffee vending machine) can
extend it and add other functionality if any. It is asbtract so its object creation is not allowed. Also it has one abstract function called makeBeverage which
every vending machine which is extending abstract class has to implement. That makes sense as every vending machine has different beverage making ability which it
can implement. 
2. makeBeverage of CoffeeVendingMachine is implemented using executor service which is a way to manage multiple thread in Java. Number of threads is fixed as outlet size.
executor service allows the beverage making functionality to run in parallel, and at the same time monitors if all thread are occupied or not. In such cases, pending requests
waits in the queues (like real life scenario).
3. Executor service executes runnable object called CoffeeMaker. Also since we have a shared resource here - the stock of ingredients inside
the coffee machine, the critical section is put in synchronzed block to avoid more than one thread to access it simultenously, which could have resulted in 
inconsistency. This functionality is common to every vending machine so it is put in abstract class.
4. Exception classes provides flexibility to envoke proper error message. Moreover in case of insufficient or unavailable ingredient, proper msg is printed
and that very order is skipped (instead of exiting) because it might be possible that next order may have vailid ingredient quantity.

Some assumptions (on the line of what was stated in the problem doc)
--------------------------------------------------------------------
1. Storage was given as reliable and fast so more emphasize is given on design rather than storage optimization and data structure efficiency.
2. CPU was given as multicore so this approach with threading should be desirable.
3. API was not allowed so resorted to testing in main function using input file.

Other suggestions:
-----------------
If scaling would have been the concern, using a queueing mechanism for submitting the beverage request and consuming the request would have been better.
That would also allow us to decouple ordering system and processing system. 
