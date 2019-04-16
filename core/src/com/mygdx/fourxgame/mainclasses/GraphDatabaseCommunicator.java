package com.mygdx.fourxgame.mainclasses;

import org.neo4j.driver.v1.*;


public class GraphDatabaseCommunicator {
    public void test(){
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "0000"));

        Session session = driver.session();

        StatementResult result = session.run("MATCH(e:EmptyField) RETURN e.x, e.y, e.owner");
        while(result.hasNext()){
            Record record = result.next();
            String x = record.get("e.x").asString();
            String y = record.get("e.y").asString();
            String string = record.get("e.owner").asString();
            System.out.println("x: " + x + " y: " + y+ " Owner: " + string);
        }
        session.close();
        driver.close();
    }
}
