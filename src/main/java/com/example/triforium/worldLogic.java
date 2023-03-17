package com.example.triforium;

//intended to take care running and communicating between all moving parts of the simulation

import javafx.scene.text.Text;

import java.util.*;

public class worldLogic extends Thread {


    //In order to make population graphs we need to measure time somehow, this is what a gamecycle will help us with
    private int gameCycles = 0;
    //this feels like a very sub optimal solution for the time being
    private List<Pop> popList;
    //The simulation will need to keep track of the resoruces
    private Storage storeHouse = new Storage(300,50,10);

    //In order to run some setup variabels once for the simulation we will
    private Boolean hasStarted = false;

    Text ironInfo;
    Text foodInfo;
    Text toolInfo;
    Text population;



    public worldLogic() {

        ironInfo = new Text();
        ironInfo.setX(10);
        ironInfo.setY(35);

        foodInfo = new Text();
        foodInfo.setX(10);
        foodInfo.setY(50);

        toolInfo = new Text();
        toolInfo.setX(10);
        toolInfo.setY(65);

        population = new Text();
        population.setX(10);
        population.setY(80);

        createPopList();
        updateResourceTable();

    }

    public List<Pop> getPopList() {
        return popList;
    }

    //Starting the thread of each pop
    public void activatePopThreads() throws InterruptedException {

        for (int i = 0; i < popList.size(); i++) {

            Thread threadstart = new Thread(popList.get(i));
            threadstart.start();
            System.out.println("Started Thread:" + i);
            Thread.sleep(1000);
        }
    }

    public void createPopList() { //create a random pop list

        List<Pop> tempPop = new ArrayList<>();
        String[] nameArray = {"Bob", "Henry", "David", "Jake", "William", "Eddie", "Philip", "Kasimir", "Adam", "Bert"};
        int random;
        //number of characters of each class
        int[] inputArray = {10, 10, 5}; //Farmer,Miner,Smith

        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i]; j++) {
                random = new Random().nextInt(nameArray.length);
                if (i == 0) {
                    tempPop.add(new PopFarmer(nameArray[random]));
                } else if (i == 1) {
                    tempPop.add(new PopMiner(nameArray[random]));
                } else {
                    tempPop.add(new PopSmith(nameArray[random]));
                }

            }
        }
        System.out.println(tempPop);
        this.popList = tempPop;
    }

    //this is a silly test function that creates one base pop
    public void addPop(){

        String[] nameArray = {"Sofia","Elizabeth","Magdalena"};
        int random;
        Pop returnPop = new Pop();
        random = new Random().nextInt(nameArray.length);
        returnPop = new PopFarmer(nameArray[random]);
    }

    public void updateResourceTable(){
        foodInfo.setText("Food: " + storeHouse.getFood());
        ironInfo.setText("Iron: " + storeHouse.getIronOre());
        toolInfo.setText("Tools: "+storeHouse.getTools());
        population.setText("Population: " + popList.size());
    }


    //checks if someone wants to deliver resources to a storage and adds to storage
    public void recieveDelivery(){

        for (int i = 0; i < popList.size(); i++) {
            //check if pop has requested to use storage and its not currently walking
            if (popList.get(i).storeRequest && !popList.get(i).onMyWay) {

                if (popList.get(i).getClass() == PopMiner.class) {
                    //if a miner is at the iron storage
                    if(popList.get(i).location == Location.IRONSTORAGE) {
                        storeHouse.ironOre = popList.get(i).CurrentlyCarrying + storeHouse.ironOre;
                        System.out.println("Our iron storage now have " + storeHouse.ironOre);
                        popList.get(i).destination = Location.MINE;
                        //miner is at the tool storage
                    } else if(popList.get(i).location == Location.TOOLSTORAGE){
                        //no more tools, need to wait
                        if(storeHouse.getTools() <= 0){
                            continue;
                        } else {
                            System.out.println("I am taking a new tool");
                            storeHouse.tools -= 1;
                            popList.get(i).toolCondition = 100;
                        }
                    }

                } else if (popList.get(i).getClass() == PopSmith.class) {
                    //smith at iron storage to take some iron
                    if (popList.get(i).location == Location.IRONSTORAGE) {
                        if(storeHouse.getIronOre() == 0){ //if there is no iron to take, skip your turn and hope that there will be some more iron there soon
                            continue;
                        } else if (storeHouse.getIronOre() <= (popList.get(i).CarryAmount) * 3 ){ //if there is less iron than the max amount he can take, take what is left
                            popList.get(i).Materials += storeHouse.getIronOre();
                            storeHouse.ironOre = 0;
                        }else {
                            storeHouse.ironOre = storeHouse.ironOre - (popList.get(i).CarryAmount * 3); //take as much as you can
                            popList.get(i).Materials += (popList.get(i).CarryAmount) * 3; //A smith can carry 10 tools and 30 iron ores, which is why the iron is multiplied by 3
                            popList.get(i).destination = Location.SMITH;
                        }
                        System.out.println("This is the smith" + popList.get(i).getName() + " and I am here to collect some iron ore");
                    } else {
                        //drop off tools at the tool storage
                        storeHouse.tools = popList.get(i).CurrentlyCarrying + storeHouse.tools;
                        System.out.println("Our tool storage now have " + storeHouse.tools);
                        popList.get(i).destination = Location.SMITH;
                    }

                } else if (popList.get(i).getClass() == PopFarmer.class) {
                    //farmer dropping off food at food storage
                    if (popList.get(i).location == Location.FOODSTORAGE) {
                        storeHouse.food = popList.get(i).CurrentlyCarrying + storeHouse.food;
                        System.out.println("Our food storage is now:" + storeHouse.food);
                        popList.get(i).destination = Location.FARM;
                        //farmer getting a new tool
                    } else if (popList.get(i).location == Location.TOOLSTORAGE){
                        System.out.println("I am taking a new tool");
                        storeHouse.tools -= 1;
                        popList.get(i).toolCondition = 100;
                    }
                }

                popList.get(i).CurrentlyCarrying = 0; //reset inventory
                popList.get(i).setRequested(false);   //this pop does not want to use storage anymore
                synchronized (popList.get(i)) {
                    popList.get(i).notify(); //wake up the pop thread so it can continue to do its work
                }
                System.out.println("Notify " + popList.get(i).getName() + " to go back to work");
                updateResourceTable(); //update the text that shows how much resources you have
            }
        }
    }
//each time a pop is working, eat will be set to true and the world will drain 3 food from storage
    public void checkFoodDrain() throws InterruptedException {
        for (int i = 0; i < popList.size(); i++) {
            if(popList.get(i).eat){
                storeHouse.food -= 3;
                popList.get(i).eat = false;
                updateResourceTable();
            }
        }
    }
    //if there is no food in storage, randomly roll pops to die
    public void checkDeath(){

        int deathNumber;

        if (storeHouse.getFood() <= 0){
            for (int i = 0; i < popList.size(); i++) {
                deathNumber = new Random().nextInt(600,700);
                if(deathNumber == 666){
                    popList.get(i).Alive = false;
                    popList.get(i).portrait.setVisible(false);
                    popList.remove(i);
                    updateResourceTable();
                }
            }
        }
    }


    @Override
    public void run() {
        //These numbers are used for random events
        int max = 10;
        int min = 1;
        int range = max - min + 1;

        System.out.println("Starting simulation");
        Collections.shuffle(popList);
        try {
            activatePopThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        while (true) {

            try {
                // the world thread is in charge of resource and population management
                recieveDelivery();
                checkFoodDrain();
                checkDeath();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}


