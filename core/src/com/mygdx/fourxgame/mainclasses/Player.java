package com.mygdx.fourxgame.mainclasses;

import com.mygdx.fourxgame.maptiles.Army;
import com.mygdx.fourxgame.maptiles.MapTile;

import java.util.ArrayList;

public class Player {
    public String playerName;

    private ArrayList<MapTile> tilesOwned;
    private ArrayList<Army> armyOwned;


    private int amountOfWood;
    private int amountOfIron;
    private int amountOfGold;
    private int population;

    public Player(String playerName, ArrayList<MapTile> tilesOwned, ArrayList<Army> armyOwned, int amountOfWood, int amountOfIron, int amountOfGold, int population) {
        this.playerName = playerName;
        this.tilesOwned = tilesOwned;
        this.armyOwned = armyOwned;
        this.amountOfWood = amountOfWood;
        this.amountOfIron = amountOfIron;
        this.amountOfGold = amountOfGold;
        this.population = population;
    }

    public int getAmountOfWood() {
        return amountOfWood;
    }

    public void setAmountOfWood(int amountOfWood) {
        this.amountOfWood = amountOfWood;
    }

    public int getAmountOfIron() {
        return amountOfIron;
    }

    public void setAmountOfIron(int amountOfIron) {
        this.amountOfIron = amountOfIron;
    }

    public int getAmountOfGold() {
        return amountOfGold;
    }

    public void setAmountOfGold(int amountOfGold) {
        this.amountOfGold = amountOfGold;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }
}
