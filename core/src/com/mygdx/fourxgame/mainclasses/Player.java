package com.mygdx.fourxgame.mainclasses;

import com.badlogic.gdx.Game;
import com.mygdx.fourxgame.maptiles.*;

import java.util.ArrayList;

public class Player {
    public String playerName;

    private ArrayList<MapTile> tilesOwned;
    private ArrayList<Army> armyOwned;


    private int amountOfWood;
    private int amountOfIron;
    private int amountOfGold;
    private int population;

    public Player(String playerName, int amountOfWood, int amountOfIron, int amountOfGold, int population) {
        this.playerName = playerName;
        this.tilesOwned = new ArrayList<>();
        this.armyOwned = new ArrayList<>();
        this.amountOfWood = amountOfWood;
        this.amountOfIron = amountOfIron;
        this.amountOfGold = amountOfGold;
        this.population = population;

    }

    public void newTurnUpdate() {
        if (tilesOwned != null) {
            for (MapTile tile : tilesOwned) {
                tile.newTurnUpdate();
            }
            for (Army army : armyOwned) {
                army.newTurnUpdate();
            }
            addResourcesOnNewTurn();
        }
    }

    private void addResourcesOnNewTurn() {
        int woodIncome = 0;
        int ironIncome = 0;
        int goldIncome = 0;

        for (MapTile resourceTile : tilesOwned) {
            if (resourceTile.getClass().getSimpleName().equals("WoodTile")) {
                woodIncome+=GameplayConstants.woodIncome;
                if(((WoodTile)resourceTile).isLumbermillBuilt()){
                    woodIncome+=GameplayConstants.woodIncome;
                }
            }else if(resourceTile.getClass().getSimpleName().equals("IronTile")){
                ironIncome+=GameplayConstants.ironIncome;
                if(((IronTile)resourceTile).isIronMineBuilt()){
                    ironIncome+=GameplayConstants.ironIncome;
                }
            }else if (resourceTile.getClass().getSimpleName().equals("GoldTile")){
                goldIncome+=GameplayConstants.goldIncome;
                if(((GoldTile)resourceTile).isGoldMineBuilt()){
                    goldIncome+=GameplayConstants.goldIncome;
                }
            }
        }
        amountOfWood+=woodIncome;
        amountOfIron+=ironIncome;
        amountOfGold+=goldIncome;
    }

    public boolean build(TownTile townWhereToDoIt, int typeOfBuilding) {
        int woodCost, ironCost, goldCost;

        if (typeOfBuilding == GameplayConstants.castleIndex) {
            if (townWhereToDoIt.getCastle() >= GameplayConstants.castleMaxLvl) {
                return false;
            }
            woodCost = GameplayConstants.woodCastleBasicCost + (GameplayConstants.woodCastleBasicCost / 4) * townWhereToDoIt.getCastle();
            ironCost = GameplayConstants.ironCastleBasicCost + (GameplayConstants.ironCastleBasicCost / 4) * townWhereToDoIt.getCastle();
            goldCost = GameplayConstants.goldCastleBasicCost + (GameplayConstants.goldCastleBasicCost / 4) * townWhereToDoIt.getCastle();
        } else if (typeOfBuilding == GameplayConstants.townhallIndex) {
            if (townWhereToDoIt.getTownHall() >= GameplayConstants.townhallMaxLvl) {
                return false;
            }
            woodCost = GameplayConstants.woodTownhallBasicCost + (GameplayConstants.woodTownhallBasicCost / 4) * townWhereToDoIt.getTownHall();
            ironCost = GameplayConstants.ironTownhallBasicCost + (GameplayConstants.ironTownhallBasicCost / 4) * townWhereToDoIt.getTownHall();
            goldCost = GameplayConstants.goldTownhallBasicCost + (GameplayConstants.goldTownhallBasicCost / 4) * townWhereToDoIt.getTownHall();
        } else if (typeOfBuilding == GameplayConstants.barracksIndex) {
            if (townWhereToDoIt.getBarrack() >= GameplayConstants.barracksMaxLvl) {
                return false;
            }
            woodCost = GameplayConstants.woodBarracksBasicCost + (GameplayConstants.woodBarracksBasicCost / 4) * townWhereToDoIt.getBarrack();
            ironCost = GameplayConstants.ironBarracksBasicCost + (GameplayConstants.ironBarracksBasicCost / 4) * townWhereToDoIt.getBarrack();
            goldCost = GameplayConstants.goldBarracksBasicCost + (GameplayConstants.goldBarracksBasicCost / 4) * townWhereToDoIt.getBarrack();
        } else if (typeOfBuilding == GameplayConstants.stablesIndex) {
            if (townWhereToDoIt.getStable() >= GameplayConstants.stablesMaxLvl) {
                return false;
            }
            woodCost = GameplayConstants.woodStablesBasicCost + (GameplayConstants.woodStablesBasicCost / 4) * townWhereToDoIt.getStable();
            ironCost = GameplayConstants.ironStablesBasicCost + (GameplayConstants.ironStablesBasicCost / 4) * townWhereToDoIt.getStable();
            goldCost = GameplayConstants.goldStablesBasicCost + (GameplayConstants.goldStablesBasicCost / 4) * townWhereToDoIt.getStable();
        } else if (typeOfBuilding == GameplayConstants.housesIndex) {
            if (townWhereToDoIt.getHouses() >= GameplayConstants.housesMaxLvl) {
                return false;
            }
            woodCost = GameplayConstants.woodHousesBasicCost + (GameplayConstants.woodHousesBasicCost / 4) * townWhereToDoIt.getHouses();
            ironCost = GameplayConstants.ironHousesBasicCost + (GameplayConstants.ironHousesBasicCost / 4) * townWhereToDoIt.getHouses();
            goldCost = GameplayConstants.goldHousesBasicCost + (GameplayConstants.goldHousesBasicCost / 4) * townWhereToDoIt.getHouses();
        } else if (typeOfBuilding == GameplayConstants.wallIndex) {
            if (townWhereToDoIt.getWall() >= GameplayConstants.wallMaxLvl) {
                return false;
            }
            woodCost = GameplayConstants.woodWallBasicCost + (GameplayConstants.woodWallBasicCost / 4) * townWhereToDoIt.getWall();
            ironCost = GameplayConstants.ironWallBasicCost + (GameplayConstants.ironWallBasicCost / 4) * townWhereToDoIt.getWall();
            goldCost = GameplayConstants.goldWallBasicCost + (GameplayConstants.goldWallBasicCost / 4) * townWhereToDoIt.getWall();
        } else if (typeOfBuilding == GameplayConstants.bankIndex) {
            if (townWhereToDoIt.getBank() >= GameplayConstants.bankMaxLvl) {
                return false;
            }
            woodCost = GameplayConstants.woodBankBasicCost + (GameplayConstants.woodBankBasicCost / 4) * townWhereToDoIt.getBank();
            ironCost = GameplayConstants.ironBankBasicCost + (GameplayConstants.ironBankBasicCost / 4) * townWhereToDoIt.getBank();
            goldCost = GameplayConstants.goldBankBasicCost + (GameplayConstants.ironBankBasicCost / 4) * townWhereToDoIt.getBank();
        } else {
            return false;
        }

        if (amountOfWood - woodCost >= 0 && amountOfIron - ironCost >= 0 && amountOfGold - goldCost >= 0) {
            boolean wasDone = townWhereToDoIt.addToBuild(typeOfBuilding);
            if (wasDone) {
                amountOfWood -= woodCost;
                amountOfIron -= ironCost;
                amountOfGold -= goldCost;
                return true;
            }
        }
        return false;

    }

    public boolean recruit(TownTile townWhereToDoIt, int archersToAdd, int footmansToAdd, int cavalryToAdd) {
        if (townWhereToDoIt.getBarrack() <= 0) {
            return false;
        }
        if(townWhereToDoIt.getStable() <= 0){
            cavalryToAdd=0;
        }
        int woodForArchers = archersToAdd * GameplayConstants.archersWoodCost;
        int ironForArchers = archersToAdd * GameplayConstants.archersIronCost;
        int goldForArchers = archersToAdd * GameplayConstants.archersGoldCost;

        int woodForFootmans = footmansToAdd * GameplayConstants.footmansWoodCost;
        int ironForFootmans = footmansToAdd * GameplayConstants.footmansIronCost;
        int goldForFootmans = footmansToAdd * GameplayConstants.footmansGoldCost;

        int woodForCavalry = cavalryToAdd * GameplayConstants.cavalryWoodCost;
        int ironForCavalry = cavalryToAdd * GameplayConstants.cavalryIronCost;
        int goldForCavalry = cavalryToAdd * GameplayConstants.cavalryGoldCost;

        int woodTotalCost = woodForArchers + woodForFootmans + woodForCavalry;
        int ironTotalCost = ironForArchers + ironForFootmans + ironForCavalry;
        int goldTotalCost = goldForArchers + goldForFootmans + goldForCavalry;

        int totalPopulationCost = 1 * archersToAdd + 1 * footmansToAdd + 2 * cavalryToAdd;

        if (amountOfWood - woodTotalCost >= 0 && amountOfIron - ironTotalCost >= 0 && amountOfGold - goldTotalCost >= 0 && population - totalPopulationCost >= 0) {
            boolean wasDone = townWhereToDoIt.addToRecruit(archersToAdd, footmansToAdd, cavalryToAdd);
            if (wasDone) {
                amountOfWood -= woodTotalCost;
                amountOfIron -= ironTotalCost;
                amountOfGold -= goldTotalCost;
                population -= totalPopulationCost;
                return true;
            }
        }
        return false;
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

    public boolean addTileToPlayer(MapTile tileToAdd) {
        tilesOwned.add(tileToAdd);
        return true;
    }

    public boolean addMultipleTilesToPlayer(ArrayList<MapTile> multipleTilesToAdd) {
        tilesOwned.addAll(multipleTilesToAdd);
        return true;
    }

    public boolean addArmyToPlayer(Army armyToAdd) {
        armyOwned.add(armyToAdd);
        return true;
    }

    public ArrayList<Army> getArmyOwned() {
        return armyOwned;
    }

    public ArrayList<MapTile> getTilesOwned() {
        return tilesOwned;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void removeArmy(Army armyToRemove) {
        armyOwned.remove(armyToRemove);
    }
}
