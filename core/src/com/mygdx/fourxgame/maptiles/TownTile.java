package com.mygdx.fourxgame.maptiles;

import com.badlogic.gdx.Game;
import com.mygdx.fourxgame.mainclasses.GameplayConstants;

public class TownTile extends MapTile {


    private String townName;
    private int castle;
    private int townHall;
    private int wall;
    private int barrack;
    private int stable;
    private int bank;
    private int houses;

    private int archersInTown;
    private int footmansInTown;
    private int cavalryInTown;

    private int firstBuildingInProgress;
    private int secondBuildingInProgress;

    private int archersToRecruit;
    private int footmansToRecruit;
    private int cavalryToRecruit;


    public TownTile(int x, int y, String owner) {
        super(x, y, owner, "townTexture.png");
        townName = "Town Name";
        castle = 0;
        townHall = 0;
        wall = 0;
        barrack = 0;
        stable = 0;
        bank = 0;
        houses = 0;
        firstBuildingInProgress = 0;
        secondBuildingInProgress = 0;
        archersToRecruit = 0;
        footmansToRecruit = 0;
        cavalryToRecruit = 0;

    }

    public void newTurnUpdate() {
        updateBuildings();
        updateRecruitment();

    }

    public boolean addToBuild(int typeOfBuilding) {
        if (firstBuildingInProgress == 0) {
            firstBuildingInProgress = typeOfBuilding;
            return true;
        } else if (secondBuildingInProgress == 0 && townHall == 3) {
            secondBuildingInProgress = typeOfBuilding;
            return true;
        } else {
            return false;
        }
    }

    public boolean addToRecruit(int archersToAdd, int footmansToAdd, int cavalryToAdd) {
        archersToRecruit += archersToAdd;
        footmansToRecruit += footmansToAdd;
        cavalryToRecruit += cavalryToAdd;
        return true;
    }

    private void updateRecruitment() {
        int archersRecruited = 0;
        int footmansRecruited = 0;
        int cavalryRecruited = 0;
        int amountRecruitedInTurn = 0;
        if (barrack == 1) {
            amountRecruitedInTurn = 2;
        } else if (barrack == 2) {
            amountRecruitedInTurn = 3;
        } else if (barrack == 3) {
            amountRecruitedInTurn = 4;
        }

        while (amountRecruitedInTurn > 0) {
            if (archersToRecruit > 0) {
                archersToRecruit--;
                archersRecruited++;
            }
            if (footmansToRecruit > 0) {
                footmansToRecruit--;
                footmansRecruited++;
            }
            if (cavalryToRecruit > 0) {
                footmansToRecruit--;
                footmansRecruited++;
            }
            amountRecruitedInTurn--;
        }
        archersInTown += archersRecruited;
        footmansInTown += footmansRecruited;
        cavalryInTown += cavalryRecruited;

    }

    private void updateBuildings() {
        boolean wasFirstBuildingUpdated = updateBuildingFromSlot(firstBuildingInProgress);
        if (wasFirstBuildingUpdated) {
            firstBuildingInProgress = 0;
        }
        boolean wasSecondBuildingUpdated = updateBuildingFromSlot(secondBuildingInProgress);
        if (wasSecondBuildingUpdated) {
            secondBuildingInProgress = 0;
        }
    }

    private boolean updateBuildingFromSlot(int buildingInProgress) {
        if (buildingInProgress != 0) {
            if (buildingInProgress == GameplayConstants.castleIndex) {
                castle++;
            } else if (buildingInProgress == GameplayConstants.townhallIndex) {
                townHall++;
            } else if (buildingInProgress == GameplayConstants.barracksIndex) {
                barrack++;
            } else if (buildingInProgress == GameplayConstants.stablesIndex) {
                stable++;
            } else if (buildingInProgress == GameplayConstants.housesIndex) {
                houses++;
            } else if (buildingInProgress == GameplayConstants.wallIndex) {
                wall++;
            } else if (buildingInProgress == GameplayConstants.bankIndex) {
                bank++;
            }
            return true;
        }
        return false;
    }

    public int calculateWoodCostOfBuilding(int buildingConstant) {
        int building = 0;
        int woodCost = 0;

        if (buildingConstant == GameplayConstants.castleIndex) {
            building = castle;
            woodCost = GameplayConstants.woodCastleBasicCost;
        } else if (buildingConstant == GameplayConstants.townhallIndex) {
            building = townHall;
            woodCost = GameplayConstants.woodTownhallBasicCost;
        } else if (buildingConstant == GameplayConstants.barracksIndex) {
            building = barrack;
            woodCost = GameplayConstants.woodBarracksBasicCost;
        } else if (buildingConstant == GameplayConstants.stablesIndex) {
            building = stable;
            woodCost = GameplayConstants.woodStablesBasicCost;
        } else if (buildingConstant == GameplayConstants.housesIndex) {
            building = houses;
            woodCost = GameplayConstants.woodHousesBasicCost;
        } else if (buildingConstant == GameplayConstants.wallIndex) {
            building = wall;
            woodCost = GameplayConstants.woodWallBasicCost;
        } else if (buildingConstant == GameplayConstants.bankIndex) {
            building = bank;
            woodCost = GameplayConstants.woodBankBasicCost;
        }
        return woodCost + (woodCost / 4) * building;
    }

    public int calculateIronCostOfBuilding(int buildingConstant) {
        int building = 0;
        int ironCost = 0;

        if (buildingConstant == GameplayConstants.castleIndex) {
            building = castle;
            ironCost = GameplayConstants.ironCastleBasicCost;
        } else if (buildingConstant == GameplayConstants.townhallIndex) {
            building = townHall;
            ironCost = GameplayConstants.ironTownhallBasicCost;
        } else if (buildingConstant == GameplayConstants.barracksIndex) {
            building = barrack;
            ironCost = GameplayConstants.ironBarracksBasicCost;
        } else if (buildingConstant == GameplayConstants.stablesIndex) {
            building = stable;
            ironCost = GameplayConstants.ironStablesBasicCost;
        } else if (buildingConstant == GameplayConstants.housesIndex) {
            building = houses;
            ironCost = GameplayConstants.ironHousesBasicCost;
        } else if (buildingConstant == GameplayConstants.wallIndex) {
            building = wall;
            ironCost = GameplayConstants.ironWallBasicCost;
        } else if (buildingConstant == GameplayConstants.bankIndex) {
            building = bank;
            ironCost = GameplayConstants.ironBankBasicCost;
        }
        return ironCost + (ironCost / 4) * building;
    }

    public int calculateGoldCostOfBuilding(int buildingConstant) {
        int building = 0;
        int goldCost = 0;

        if (buildingConstant == GameplayConstants.castleIndex) {
            building = castle;
            goldCost = GameplayConstants.goldCastleBasicCost;
        } else if (buildingConstant == GameplayConstants.townhallIndex) {
            building = townHall;
            goldCost = GameplayConstants.goldTownhallBasicCost;
        } else if (buildingConstant == GameplayConstants.barracksIndex) {
            building = barrack;
            goldCost = GameplayConstants.goldBarracksBasicCost;
        } else if (buildingConstant == GameplayConstants.stablesIndex) {
            building = stable;
            goldCost = GameplayConstants.goldStablesBasicCost;
        } else if (buildingConstant == GameplayConstants.housesIndex) {
            building = houses;
            goldCost = GameplayConstants.goldHousesBasicCost;
        } else if (buildingConstant == GameplayConstants.wallIndex) {
            building = wall;
            goldCost = GameplayConstants.goldWallBasicCost;
        } else if (buildingConstant == GameplayConstants.bankIndex) {
            building = bank;
            goldCost = GameplayConstants.goldBankBasicCost;
        }
        return goldCost + (goldCost / 4) * building;
    }

    public int getCastle() {
        return castle;
    }

    public int getTownHall() {
        return townHall;
    }

    public int getWall() {
        return wall;
    }

    public int getBarrack() {
        return barrack;
    }

    public int getStable() {
        return stable;
    }

    public int getBank() {
        return bank;
    }

    public int getHouses() {
        return houses;
    }

    public int getArchersInTown() {
        return archersInTown;
    }

    public int getFootmansInTown() {
        return footmansInTown;
    }

    public int getCavalryInTown() {
        return cavalryInTown;
    }

    public int getBuilding(int buildingConstant) {
        if (buildingConstant == GameplayConstants.castleIndex) {
            return castle;
        } else if (buildingConstant == GameplayConstants.townhallIndex) {
            return townHall;
        } else if (buildingConstant == GameplayConstants.barracksIndex) {
            return barrack;
        } else if (buildingConstant == GameplayConstants.stablesIndex) {
            return stable;
        } else if (buildingConstant == GameplayConstants.housesIndex) {
            return houses;
        } else if (buildingConstant == GameplayConstants.wallIndex) {
            return wall;
        } else if (buildingConstant == GameplayConstants.bankIndex) {
            return bank;
        } else {
            return -1;
        }
    }

    public String buildingTotalCostToString(int buildingConstant){
        return "W:" + calculateWoodCostOfBuilding(buildingConstant) + " I:" + calculateIronCostOfBuilding(buildingConstant) + " G:" + calculateGoldCostOfBuilding(buildingConstant);
    }

}
