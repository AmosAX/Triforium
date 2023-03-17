package com.example.triforium;


import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Random;

//This is the base class all population types will inherit from, other
public class Pop implements Runnable {

    String Name;
    Integer CarryAmount;
    Integer CurrentlyCarrying;
    Integer Materials;
    Integer toolCondition;
    Boolean onMyWay;
    Boolean storeRequest;
    Boolean eat;
    Boolean Alive;
    ImageView portrait;
    Text textDisplay;
    Location location;
    Location destination;

    public void rest() {
        System.out.println("I am taking a break,");
    }
    public void walk(){
        System.out.println("I am walking");
        eat();
    }

    public void eat(){
        this.eat = true;
    }
    public void becomeParent(){
        System.out.println(Name +" Has become a parent");
    }

    //When the pop has a full inventory and need to drop off his inventory, check class and put in appropriate variable
    void popDropOff() throws InterruptedException {
        setRequested(true);
        wait();

    }

    @Override
    //At this time it's a bit unsure if we are going to run every individual pop as a thread or every pop-type, aka farmers,smiths etc as one thread
    //All we know is that threads will make it much more interesting
    public void run() {

    }

    public String getName() {
        return Name;
    }

    synchronized void setRequested(boolean bool){
        storeRequest = bool;
    }

    protected void walkAnimation(Pop pop){
        onMyWay = true;
        if (pop.destination != Location.NONE) {
            int speed = 3;
            ImageView temp = pop.portrait;

            Point2D pointTop = new Point2D(265, -190);
            Point2D minePoint = new Point2D(130, -280);
            Point2D ironStoragePoint = new Point2D(565, -225);

            Point2D pointMid = new Point2D(265, 40);
            Point2D farmPoint = new Point2D(630, 150);
            Point2D foodStoragePoint = new Point2D(630, 10);
            Point2D churchPoint = new Point2D(0, 0);

            Point2D pointBot = new Point2D(50, 255);
            Point2D smithPoint = new Point2D(-20, 225);
            Point2D toolStoragePoint = new Point2D(438, 225);

            AnimationTimer timer;

            timer = new AnimationTimer() {
                Location l1 = pop.location; //current location
                Location l2 = pop.destination;  //destination
                int timerCounter = 0; //this integer keeps track on what stage the animation is in

                @Override
                public void handle(long l) {
                    
                    //---------------------------First Animation Step------------------------------------
                    if (timerCounter == 0) {
                        temp.setVisible(true);
                        if (l1 == Location.MINE || l1 == Location.IRONSTORAGE) {
                            temp.setY(temp.getY() + speed);
                            if (temp.getY() >= pointTop.getY()) {
                                timerCounter++;
                            }
                        } else if (l1 == Location.CHURCH || l1 == Location.FOODSTORAGE) {
                            temp.setY(temp.getY() + speed);
                            if (temp.getY() >= pointMid.getY()) {
                                timerCounter++;
                            }
                        } else {
                            temp.setY(temp.getY() + speed);
                            if (temp.getY() >= pointBot.getY()) {
                                timerCounter++;
                            }
                        }
                    }
                    //-------------------------Second Animation Step--------------------------------------
                    if (timerCounter == 1) {
                        if (l1 == Location.MINE || l1 == Location.CHURCH || l1 == Location.SMITH) {
                            temp.setX(temp.getX() + speed);
                            if (temp.getX() >= pointTop.getX()) {
                                timerCounter++;
                            }
                        } else {
                            temp.setX(temp.getX() - speed);
                            if (temp.getX() <= pointMid.getX()) {
                                timerCounter++;
                            }
                        }
                    }
                    //--------------------------Third Animation Step----------------------------------------
                    if (timerCounter == 2) {

                        if (l2 == Location.MINE || l2 == Location.IRONSTORAGE) {
                            temp.setY(temp.getY() - speed);
                            if (temp.getY() <= pointTop.getY()) {
                                timerCounter++;
                            }
                        } else if (l2 == Location.SMITH || l2 == Location.TOOLSTORAGE || l2 == Location.FARM) {
                            temp.setY(temp.getY() + speed);
                            if (temp.getY() >= pointBot.getY()) {
                                timerCounter++;
                            }
                        } else {
                            //If destination is on mid path and current location is below midpath
                            if (temp.getY() >= pointMid.getY()) {
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= pointMid.getY()) {
                                    timerCounter++;
                                }
                            } else { //if destination is on mid path and current location is above mid path
                                temp.setY(temp.getY() + speed);
                                if (temp.getY() >= pointMid.getY()) {
                                    timerCounter++;
                                }
                            }
                        }
                    }
                    //-------------------------------------Fourth Animation Step----------------------------
                    if (timerCounter == 3) {
                        switch (l2) {
                            case CHURCH:
                                temp.setX(temp.getX() - speed);
                                if (temp.getX() <= churchPoint.getX()) {
                                    timerCounter++;
                                }
                                break;
                            case FARM:
                                temp.setX(temp.getX() + speed);
                                if (temp.getX() >= farmPoint.getX()) {
                                    timerCounter++;
                                }
                                break;
                            case MINE:
                                temp.setX(temp.getX() - speed);
                                if (temp.getX() <= minePoint.getX()) {
                                    timerCounter++;
                                }
                                break;
                            case IRONSTORAGE:
                                temp.setX(temp.getX() + speed);
                                if (temp.getX() >= ironStoragePoint.getX()) {
                                    timerCounter++;
                                }
                                break;
                            case FOODSTORAGE:
                                temp.setX(temp.getX() + speed);
                                if (temp.getX() >= foodStoragePoint.getX()) {
                                    timerCounter++;
                                }
                                break;
                            case SMITH:
                                temp.setX(temp.getX() - speed);
                                if (temp.getX() <= smithPoint.getX()) {
                                    timerCounter++;
                                }
                                break;
                            case TOOLSTORAGE:
                                temp.setX(temp.getX() + speed);
                                if (temp.getX() >= toolStoragePoint.getX()) {
                                    timerCounter++;
                                }
                                break;
                            default:
                        }
                    }
                    //------------------------Fifth Animation Step-------------------------
                    if (timerCounter == 4) {
                        switch (l2) {
                            case CHURCH:
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= churchPoint.getY()) {
                                    timerCounter++;
                                    temp.setVisible(false);
                                }
                                break;
                            case FARM:
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= farmPoint.getY()) {
                                    Random random = new Random();
                                    int rng;
                                    for (int i = 0;i<5;i++){
                                        rng = random.nextInt(12 - (-12) + 1) + (-12);
                                        temp.setX(temp.getX() + rng);
                                        rng = random.nextInt(20 - (-20) + 1) + (-20);
                                        temp.setY(temp.getY() + rng);
                                    }
                                    timerCounter++;
                                }
                                break;
                            case MINE:
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= minePoint.getY()) {
                                    timerCounter++;
                                    temp.setVisible(false);
                                }
                                break;
                            case IRONSTORAGE:
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= ironStoragePoint.getY()) {
                                    timerCounter++;
                                    temp.setVisible(false);
                                }
                                break;
                            case FOODSTORAGE:
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= foodStoragePoint.getY()) {
                                    timerCounter++;
                                    temp.setVisible(false);
                                }
                                break;
                            case SMITH:
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= smithPoint.getY()) {
                                    timerCounter++;
                                }
                                break;
                            case TOOLSTORAGE:
                                temp.setY(temp.getY() - speed);
                                if (temp.getY() <= toolStoragePoint.getY()) {
                                    timerCounter++;
                                    temp.setVisible(false);
                                }
                                break;
                            default:
                        }
                    }

                    if (timerCounter > 4) {
                        pop.location = l2;
                        pop.destination = Location.NONE;
                        onMyWay = false;
                        this.stop();
                    }
                }
            };
            walk();
            timer.start();
        }
    }

    public int generateRandomNumber(int low, int high){
        Random rand =  new Random();
        return rand.nextInt(low,high);
    }

    @Override
    public String toString(){
        return this.getName();
    }
}



