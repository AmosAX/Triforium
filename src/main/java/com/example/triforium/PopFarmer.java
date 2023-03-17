package com.example.triforium;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PopFarmer extends Pop {

    public PopFarmer(String name) {
        this.Name = name;
        this.textDisplay = new Text(Name);
        this.onMyWay = false;
        this.Alive = true;
        this.storeRequest = false;
        this.CurrentlyCarrying = 0;
        this.CarryAmount = 100;
        this.toolCondition = 100;
        this.eat = false;
        this.location = Location.CHURCH;
        this.destination = Location.FARM;
        this.portrait = new ImageView(new Image("/farmer.png"));
        this.portrait.setVisible(false);
    }


    public void harvest() throws InterruptedException {
        int farmAmount = (toolCondition/generateRandomNumber(3,8))+1;
        System.out.println("Farming " + farmAmount + " food.");
        //If you can't carry another full harvest, just harvest as much as you need for a full inventory
        if(CarryAmount-CurrentlyCarrying < farmAmount){
            CurrentlyCarrying = CarryAmount;
        }else {
            this.CurrentlyCarrying = this.CurrentlyCarrying + farmAmount;
        }
        this.toolCondition = toolCondition - 5;
        eat();
        System.out.println("Farmer " + this.getName() + " is currently carrying " + CurrentlyCarrying + " food");
    }

    @Override
    public void run() {
        System.out.println("Hello I am a farmer and my name is " + Name);
        while (Alive) {
            synchronized (this) {
                if (onMyWay){
                    try {
                        Thread.sleep(1000); //need more time to walk
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if (this.location != Location.FARM) {
                    try {
                        Thread.sleep(3000);
                        System.out.println("This is farmer " + Name + " and I should probably go to the farm!");
                        this.destination = Location.FARM;
                        walkAnimation(this);
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else { //Farmer is working
                    if (CurrentlyCarrying >= CarryAmount) {
                        System.out.println("My inventory is full of from harvesting! time to drop it off");
                        this.destination = Location.FOODSTORAGE;
                        walkAnimation(this);
                        try {
                            Thread.sleep(3000);
                            popDropOff();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        if(toolCondition <= 10){
                            this.destination = Location.TOOLSTORAGE;
                            walkAnimation(this);
                            try {
                                Thread.sleep(3000);
                                popDropOff();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                if(generateRandomNumber(0,3) != 0) {
                                    System.out.println(Name + " is at the farm, time to harvest");
                                    harvest();
                                } else {
                                    System.out.println(this.getName() + " failed to harvest");
                                }
                                Thread.sleep(3000); //time it takes to attempt to harvest

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
