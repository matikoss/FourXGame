package com.mygdx.fourxgame.controllers;
import com.mygdx.fourxgame.maptiles.EmptyTile;
import com.mygdx.fourxgame.maptiles.MapTile;

import com.mygdx.fourxgame.maptiles.TownTile;
import com.mygdx.fourxgame.maptiles.WoodTile;
import org.neo4j.driver.v1.*;

import java.util.ArrayList;

public class WorldMap {
    private ArrayList<MapTile> mapOfWorld;

    public WorldMap(){
        mapOfWorld = new ArrayList<>();
        loadMapFromDatabase();
    }

    public void addToMap(MapTile mapTile){
        mapOfWorld.add(mapTile);
    }

    public ArrayList<MapTile> getMapOfWorld() {
        return mapOfWorld;
    }

    private void loadMapFromDatabase(){
        Driver driver = null;
        try {
            driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "0000"));
            Session session = driver.session();
            StatementResult result = session.run("MATCH(e:EmptyField) RETURN e.x, e.y, e.owner");
            while(result.hasNext()){
                Record record = result.next();
                int x = record.get("e.x").asInt();
                int y = record.get("e.y").asInt();
                String owner = record.get("e.owner").asString();
                addToMap(new EmptyTile(x,y,owner));
                System.out.println(x+ " " + y + " " + " " + owner);
            }
            result = session.run("MATCH(t:City) RETURN t.x, t.y, t.owner");
            while(result.hasNext()){
                Record record = result.next();
                int x = record.get("t.x").asInt();
                int y = record.get("t.y").asInt();
                String owner = record.get("t.owner").asString();
                addToMap(new TownTile(x,y,owner));
            }

            result = session.run("MATCH(w:Forest) RETURN w.x, w.y, w.owner");
            while(result.hasNext()){
                Record record = result.next();
                int x = record.get("w.x").asInt();
                int y = record.get("w.y").asInt();
                String owner = record.get("w.owner").asString();
                addToMap(new WoodTile(x,y,owner));
            }


            session.close();

        }catch (Exception e){

        }finally {
            if(driver!=null){
                driver.close();
            }
        }

        System.out.println(mapOfWorld);
    }
}
