package com.example.triforium;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PopSmith extends Pop {


    public PopSmith(String name) {
        this.Name = name;
        this.textDisplay = new Text(Name);
        this.onMyWay = false;
        this.Alive = true;
        this.storeRequest = false;
        this.CurrentlyCarrying = 0;
        this.CarryAmount = 10;
        this.Materials = 0;
        this.eat = false;
        this.location = Location.CHURCH;
        this.destination = Location.SMITH;
        this.portrait = new ImageView(new Image("/smith.png"));
        this.portrait.setVisible(false);
    }

    public void makeTool() throws InterruptedException {
        Materials = Materials - 5;
        CurrentlyCarrying = CurrentlyCarrying + 1;
        eat();
    }

    @Override
    public void run() {

        System.out.println("Hello I am a smith and my name is " + Name);
        while (Alive) {
            synchronized (this) {
                if (onMyWay){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(this.location != Location.SMITH) {
                    try {
                        System.out.println("This is " + Name + "the smith and I should probably go to work!");
                        Thread.sleep(3000);
                        this.destination = Location.SMITH;
                        walkAnimation(this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else { //Smith is currently working



                        if (CurrentlyCarrying >= (5)) { //Can't carry more tools, need to drop off at tool storage
                            System.out.println("My inventory is full from making tools! time to drop it off");
                            this.destination = Location.TOOLSTORAGE;
                            walkAnimation(this);
                            try {
                                Thread.sleep(3000);
                                popDropOff();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }   else if (Materials <= 5) { //Out of iron so need to collect more from storage
                            System.out.println("This is" + Name + " and I need to go and get more iron ore to smith with");
                            try {
                                this.destination = Location.IRONSTORAGE;
                                walkAnimation(this);
                                Thread.sleep(3000);
                                popDropOff();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        else{ //order smith to make tools
                            System.out.println(Name + " is am making some tools");
                            try {
                                // 50% chance to successfully make a tool
                                if(generateRandomNumber(0,9) != 0) {
                                    makeTool();
                                } else {
                                    System.out.println(this.getName() + " failed to make a tool");
                                }
                                Thread.sleep(3000); //time it takes to attempt to smith a tool
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }
    }
