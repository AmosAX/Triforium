package com.example.triforium;

public class Storage {
    int food;
    int ironOre;
    int tools;

    public Storage(int food, int iron, int tools){
        this.food = food;
        this.ironOre = iron;
        this.tools = tools;
    }

    public int getIronOre() {
        return ironOre;
    }

    public int getFood() {
        return food;
    }

    public int getTools() {
        return tools;
    }
}
