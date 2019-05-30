package com.mygdx.fourxgame.maptiles;

import com.mygdx.fourxgame.mainclasses.GameplayConstants;

import java.util.Random;

public class Army extends MapTile {
    private int archersAmount;
    private int footmansAmount;
    private int cavalryAmount;

    private int moveDistanceLeft;
    private boolean isFighting;


    public Army(int x, int y, String owner, int archersAmount, int footmansAmount, int cavalryAmount) {
        super(x, y, owner, "armyTexture.png");
        this.archersAmount = archersAmount;
        this.footmansAmount = footmansAmount;
        this.cavalryAmount = cavalryAmount;
        moveDistanceLeft = calculateRange();
    }

    public void newTurnUpdate() {
        resetMovement();
    }

    public void move(int newX, int newY, int distanceMoved) {

        if (moveDistanceLeft <= 0) {
            return;
        }
        moveDistanceLeft = moveDistanceLeft - distanceMoved;
        this.x = newX;
        this.y = newY;

    }

    public int calculateRange() {
        if (archersAmount >= 1 || footmansAmount >= 1) {
            return GameplayConstants.archersAndFootmansSpeed;
        } else {
            return GameplayConstants.cavalrySpeed;
        }
    }

    public void addArmy(int archersToAdd, int footmansToAdd, int cavalryToAdd) {
        archersAmount += archersToAdd;
        footmansAmount += footmansToAdd;
        cavalryAmount += cavalryToAdd;
    }

    public void removeUnitsFromArmy(int archersAmountToRemove, int footmansAmountToRemove, int cavalryAmountToRemove) {
        archersAmount -= archersAmountToRemove;
        footmansAmount -= footmansAmountToRemove;
        cavalryAmount -= cavalryAmountToRemove;
    }

    public boolean archersAttacksCavalry(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 5) {
            return true;
        } else {
            archersEliminated();
            return false;
        }
    }

    public boolean archersAttacksArchers(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 4) {
            return true;
        } else {
            archersEliminated();
            return false;
        }
    }

    public boolean archersAttacksInfantry(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 3) {
            return true;
        } else {
            archersEliminated();
            return false;
        }
    }

    public boolean infantryAttacksArchers(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 5) {
            return true;
        } else {
            infantryEliminated();
            return false;
        }
    }

    public boolean infantryAttacksInfantry(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 4) {
            return true;
        } else {
            infantryEliminated();
            return false;
        }
    }

    public boolean infantryAttacksCavalry(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 4) {
            return true;
        } else {
            infantryEliminated();
            return false;
        }
    }

    public boolean cavalryAttacksArchers(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 2) {
            return true;
        } else {
            cavalryEliminated();
            return false;
        }
    }

    public boolean cavalryAttacksInfantry(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 3) {
            return true;
        } else {
            cavalryEliminated();
            return false;
        }
    }

    public boolean cavalryAttacksCavalry(Random k6dice, int modifier) {
        int k6Value = k6dice.nextInt(6) + 1 + modifier;
        if (k6Value + modifier >= 4) {
            return true;
        } else {
            cavalryEliminated();
            return false;
        }
    }

    public void archersEliminated() {
        archersAmount--;
    }

    public void infantryEliminated() {
        footmansAmount--;
    }

    public void cavalryEliminated() {
        cavalryAmount--;
    }

    private void resetMovement() {
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

    public boolean isArmyEmpty() {
        if (archersAmount <= 0 && footmansAmount <= 0 && cavalryAmount <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getMoveDistanceLeft() {
        return moveDistanceLeft;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Army)) {
            return false;
        }

        Army army = (Army) obj;

        if (x == army.x && y == army.y && archersAmount == army.archersAmount
                && footmansAmount == army.footmansAmount && cavalryAmount == army.cavalryAmount && getOwner().equals(army.getOwner())) {
            return true;
        } else {
            return false;
        }
    }
}
