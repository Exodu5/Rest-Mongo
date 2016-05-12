/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.projetstageafpaalteca2016;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


/**
 *RestFull acces to mongoDB Database
 * @author cfremont
 */
@LocalBean
@Path("/produit")
public class ProduitService {
    
    
    private static String alerte = "Ce produit est introuvable";
    private static String host = "172.30.40.116";
    private static int port = 27017;
    private static String NOM_DATABASE_RAM = "RAM";
    
    
    
    
    
    /**
     *Méthode de get permettant d'obtenir tout les produits en base
     * @return List<Produit> list
     **/
    @GET
    @Path("liste")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Produit> getAllProduit(){
        

        MongoDBAcces dbSingleton = MongoDBAcces.getInstance();
        DB db = dbSingleton.getTestdb();
        DBCollection collection = db.getCollection("produit"); 
        DBCursor cursor = collection.find().sort(new BasicDBObject("nom", 1));

        List<Produit> list = new ArrayList<Produit>();
        
        while (cursor.hasNext()) { 

            DBObject o = cursor.next();

            Produit produit = new Produit();

            produit.setNom((String) o.get("nom"));   

            produit.setLieu((String) o.get("lieu"));

            produit.setQuantite((Integer) o.get("quantite"));

            list.add(produit);
        }
    
        return list;
        
    }
    
  
    
    
    /**
     *Methode de recherche par nom
     * @param String nom
     * @return Document
     **/
    @GET
    @Path("get/{nom}")
    @Produces({MediaType.APPLICATION_JSON})
    public Document rechercher(@PathParam("nom") String nom){
        
        MongoClient client = new MongoClient(host, port);
        MongoDatabase database = client.getDatabase(NOM_DATABASE_RAM);
        MongoCollection mongoCollection = database.getCollection("produit");
        
        
        Document d = new Document("nom", nom);
        
        FindIterable<Document> iterable = mongoCollection.find(new Document("nom", nom));

        
        return iterable.first();
        
    }
    
    /**
     *TODO 
     * /!\
     * Methode d'insert de produit
     * @param String nom, int quantite, String lieu
     * @return null
     **/
    @POST
    @Path("insert/{nom}/{quantite}/{lieu}")
    public String insertProduit(@PathParam("nom") String nom, @PathParam("quantite") int quantite, @PathParam("lieu") String lieu){

        MongoClient client = new MongoClient(host, port);
        MongoDatabase database = client.getDatabase(NOM_DATABASE_RAM);
        MongoCollection mongoCollection = database.getCollection("produit");

        Document document = new Document();
        
        mongoCollection.insertOne(
                document
                .append("nom", nom)
                .append("quantite", quantite)
                
                
        );
                
        System.out.println(document.toString());
        
        return document.getString("nom") + " a ete enregistre";
       
    }
    
    /**
     *Methode d'insert d'objet en JSson
     *@Consumes Json
     *@return String
     **/

    @POST
    @Path("biginsert/{values}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String bigInsert(@PathParam("values") String values) throws JSONParseException{
        
        MongoClient client = new MongoClient(host, port);
        MongoDatabase database = client.getDatabase(NOM_DATABASE_RAM);
        MongoCollection mongoCollection = database.getCollection("produit");
        
        
        Document document = Document.parse(values);
        
        FindIterable<Document> iterable = mongoCollection.find(document);
        
        if(iterable.first() == null){
            
            mongoCollection.insertOne(document);
            
        }
        
        mongoCollection.insertOne(document);
        
        ObjectId objId = document.getObjectId(document);
        
        String id = objId.toString();
        
        return id;
    }
    
        
    /**
     * 
     *Methode de modification du produit
     * @param String nom, int quantite
     * @return String "Message d'alerte" + String nom + int quantite
     **/
    @PUT
    @Path("modif/{nom}/{quantite}")
    public String updateQuantiteProduit(@PathParam("nom") String nom, @PathParam("quantite") int quantite){
        
        MongoClient client = new MongoClient(host, port);
        MongoDatabase database = client.getDatabase(NOM_DATABASE_RAM);
        MongoCollection mongoCollection = database.getCollection("produit");

        Document doc1 = new Document("nom", nom);
        Document doc2 = new Document("$set", new Document("quantite", quantite));

        UpdateResult result =  mongoCollection.updateOne(doc1, doc2);

        System.out.println("UpdateResult: " + result.toString());

        
        
        return nom + " " + "a bien ete modifié(e) la quantite est de "+ quantite;
    }
    
    @PUT
    @Path("newvalue/{nom}/{key}/{value}")
    public String insertNewValueKey(@PathParam("nom") String nom, @PathParam("key") String key, @PathParam("value") String value) {
        
        MongoClient client = new MongoClient(host, port);
        MongoDatabase database = client.getDatabase(NOM_DATABASE_RAM);
        MongoCollection mongoCollection = database.getCollection("produit");

        Document doc1 = new Document("nom", nom);
        Document doc2 = new Document("$set", new Document(key, value));
        
        UpdateResult result =  mongoCollection.updateOne(doc1, doc2);

        System.out.println("UpdateResult: " + result.toString());
        
        return "Hello";
    }
    
    
    /**
     *
     * Methode de suppression par nom
     * 
     * @throws MongoWriteException
     * @param String nom
     * @return String nom + texte
     * 
     **/
    @DELETE
    @Path("supprimer/{nom}")
    public String supp(@PathParam("nom") String nom) throws MongoWriteException{
        
        MongoClient client = new MongoClient(host, port);
        MongoDatabase database = client.getDatabase(NOM_DATABASE_RAM);
        MongoCollection mongoCollection = database.getCollection("produit");
        
        mongoCollection.deleteOne(new Document("nom", nom));
        
        
        
        return nom + " a ete supprime(e)";
    }
    
      /**
     *Ancienne Méthode de recherche
     *Méthode de recherche en base mongoDB par nom
     * @param {nom}, String nom
     * @return List<Produit> list
     **/
//    @GET
//    @Path("recherche/{nom}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<Produit> getProdByName(@PathParam("nom") String nom) {
//
//        MongoDBAcces dbSingleton = MongoDBAcces.getInstance();
//
//        DB db = dbSingleton.getTestdb();
//
//        DBCollection collection = db.getCollection("produit");             
//
//        BasicDBObject query = new BasicDBObject();
//
//        query.put("nom", nom);
//
//        DBCursor cursor = collection.find(query);
//
//        Produit p = new Produit();
//            
//        List<Produit> list = new ArrayList<Produit>();
//        
//            while (cursor.hasNext()) { 
//                
//                DBObject o = cursor.next();
//                
//                if(o.get("nom") == nom){
//                   
//                    p.setNom((String) o.get("nom"));
//                    p.setLieu((String) o.get("lieu"));
//                    p.setQuantite((Integer) o.get("quantite"));
//                
//                list.add(p);
//                }
//                
//            }
//          
//            
//        return list;
//    }
    
}
