package com.mygdx.fourxgame.maptiles;

import com.mygdx.fourxgame.mainclasses.GameplayConstants;

public class Army extends MapTile {
    private int archersAmount;
    private int footmansAmount;
    private int cavalryAmount;

    private int moveDistanceLeft;


    public Army(int x, int y, String owner, int archersAmount, int footmansAmount, int cavalryAmount) {
        super(x, y, owner, "armyTexture.png");
        this.archersAmount = archersAmount;
        this.footmansAmount = footmansAmount;
        this.cavalryAmount = cavalryAmount;
        moveDistanceLeft = calculateRange();
    }

    public void newTurnUpdate(){
        resetMovement();
    }

    public void move(int newX, int newY, int distanceMoved) {

        if(moveDistanceLeft<=0){
            return;
        }
        moveDistanceLeft = moveDistanceLeft - distanceMoved;
        this.x = newX;
        this.y = newY;

    }

    public int calculateRange() {
        if (archersAmount >=1 || footmansAmount>=1){
            return GameplayConstants.archersAndFootmansSpeed;
        }else {
            return GameplayConstants.cavalrySpeed;
        }
    }
    private void resetMovement(){
        moveDistanceLeft = calculateRange();
    }

    public int getArchersAmount() {
        return archersAmount;
    }

    public int getFootmansAmount() {
        return footmansAmount;
    }

    public int getCavalryAmount() {
        return cavalryAmount;
    }

    public void armyResetAtNewTurn(){
        moveDistanceLeft = calculateRange();
    }

}
