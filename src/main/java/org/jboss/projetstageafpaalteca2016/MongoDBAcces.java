/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.projetstageafpaalteca2016;

/**
 *
 * @author cfremont
 */
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBAcces {
	 
        private static MongoDBAcces acces;

        private static MongoClient mongoClient;

        private static DB db ;


        private static final String dbHost = "172.30.40.116";
        private static final int dbPort = 27017;
        private static final String dbName = "RAM";

        private MongoDBAcces(){};

        public static MongoDBAcces getInstance(){

            if(acces == null){

                acces = new MongoDBAcces();
            }
         return acces;
        } 
	  
        public DB getTestdb(){

                 if(mongoClient == null){

                       mongoClient = new MongoClient(dbHost , dbPort);

                 }


                 if(db == null){

                       db = mongoClient.getDB(dbName);
                       System.out.println("On y est");
                 }				  

                 return db;
        }

    public static MongoDBAcces getAcces() {
        return acces;
    }

    public static void setAcces(MongoDBAcces acces) {
        MongoDBAcces.acces = acces;
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static void setMongoClient(MongoClient mongoClient) {
        MongoDBAcces.mongoClient = mongoClient;
    }

    public static DB getDb() {
        return db;
    }

    public static void setDb(DB db) {
        MongoDBAcces.db = db;
    }
        
    
}

