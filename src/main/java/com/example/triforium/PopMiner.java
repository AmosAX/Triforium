package com.example.triforium;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PopMiner extends Pop {


    public PopMiner(String name) {
        this.Name = name;
        this.textDisplay = new Text(Name);
        this.onMyWay = false;
        this.eat = false;
        this.Alive = true;
        this.storeRequest = false;
        this.toolCondition = 100;
        this.CurrentlyCarrying = 0;
        this.CarryAmount = 30;
        this.location = Location.CHURCH;
        this.destination = Location.MINE;
        this.portrait = new ImageView(new Image("/miner.png"));
        this.portrait.setVisible(false);
    }

    public void gatherOre() throws InterruptedException {

        int gatherAmount = (toolCondition/generateRandomNumber(10,30))+1;
        System.out.println("I am mining " + gatherAmount + " iron ores.");
        //If you can't take a whole bunch of ore, take as much as you need to fill inventory
        if(CarryAmount-CurrentlyCarrying < gatherAmount){
            CurrentlyCarrying = CarryAmount;
        }else {
            CurrentlyCarrying += gatherAmount;
        }
        toolCondition -= 5;
        eat();
        System.out.println("Currently carrying: " + CurrentlyCarrying);
    }


    @Override
    public void run() {
        System.out.println("Hello I am a miner and my name is " + Name);
        while (Alive) {
            synchronized (this) {
                if (onMyWay){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if (this.location != Location.MINE) {
                    try {
                        System.out.println("This is " + Name + " and I should probably go to work!");
                        Thread.sleep(3000);
                        this.destination = Location.MINE;
                        walkAnimation(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (CurrentlyCarrying >= CarryAmount) {
                        System.out.println("I got so much iron ore! time to drop it off");
                        this.destination = Location.IRONSTORAGE;
                        walkAnimation(this);
                        try {
                            Thread.sleep(3000);
                            popDropOff();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(this.location == Location.MINE) {
                        if(toolCondition <= 10){
                            this.destination = Location.TOOLSTORAGE;
                            walkAnimation(this);
                            try {
                                Thread.sleep(3000);
                                popDropOff();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println(Name + " is at work, time to mine");
                            try {
                                if (generateRandomNumber(0,3) != 0) {
                                    gatherOre();
                                } else {
                                    System.out.println(this.getName() + " failed to mine some ore");
                                }
                                Thread.sleep(3000); //time it takes to mine
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    /*
                    if(Energy < 25)
                    try {
                        System.out.println(Name + " is feeling tired I should rest my soul for a bit");
                        walk();
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                     */
                }

            }
        }
    }
}
