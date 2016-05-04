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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ws.rs.Consumes;
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


/**
 *
 * @author cfremont
 */

@LocalBean
@Path("/produit")
public class ProduitService {
    
    
    public static String alerte = "Ce produit est introuvable";
    
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
     *Méthode de recherche en base mongoDB par nom
     * @param {nom}, String nom
     * @return List<Produit> list
     **/
    @GET
    @Path("recherche/{nom}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Produit> getProdByName(@PathParam("nom") String nom) {

        MongoDBAcces dbSingleton = MongoDBAcces.getInstance();

        DB db = dbSingleton.getTestdb();

        DBCollection collection = db.getCollection("produit");             

        BasicDBObject query = new BasicDBObject();

        query.put("nom", nom);

        DBCursor cursor = collection.find(query);

        Produit p = new Produit();
            
        List<Produit> list = new ArrayList<Produit>();
        
            while (cursor.hasNext()) { 
                
                DBObject o = cursor.next();
                
                if(o.get("nom") == nom){
                   
                    p.setNom((String) o.get("nom"));
                    p.setLieu((String) o.get("lieu"));
                    p.setQuantite((Integer) o.get("quantite"));
                
                list.add(p);
                }
                
            }
          
            
        return list;
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
    @Produces({MediaType.APPLICATION_JSON})
    public String insertProduit(@PathParam("nom") String nom, @PathParam("quantite") int quantite, @PathParam("lieu") String lieu){
        
        MongoDBAcces dbSingleton = MongoDBAcces.getInstance();

        DB db = dbSingleton.getTestdb();

        DBCollection collection = db.getCollection("produit");        
        
        DBObject document  = new BasicDBObject();
        
        
        DBCursor bCursor = (DBCursor) collection.findOne(document);
        
        String mess = new String();
        
        while (bCursor.hasNext()) { 
            
            BasicDBObject query = new BasicDBObject();
            query = (BasicDBObject) bCursor.next();
            
            if(query.get("nom").equals(nom)){
                
                document.put("nom", nom);
                document.put("quantite", quantite);
                document.put("lieu", lieu);
                    
                collection.insert(document);
                
                mess = "a bien été enregistré";
                
            } else{
                
                mess = "n'a pas été enregistré";
                
            }

        }
        
        return nom + " " + mess;
                
      
        
        

       
    }
    
    
    
    /**
     *Methode de modification du produit
     * @param String nom, int quantite
     * @return String "Message d'alerte"
     **/
    @PUT
    @Path("modif/{nom}/{quantite}")
    public String updateQuantiteProduit(@PathParam("nom") String nom, @PathParam("quantite") int quantite){
        
        MongoDBAcces dbSingleton = MongoDBAcces.getInstance();

        DB db = dbSingleton.getTestdb();

        DBCollection collection = db.getCollection("produit");             

        
        BasicDBObject query = new BasicDBObject();
        
        BasicDBObject updateQuantite = new BasicDBObject();
        
        Document document = new Document();
        
        query.put("nom", nom);
        updateQuantite.put("quantite", quantite);
        System.out.println("QUERY "+ query);
        System.out.println("update "+ updateQuantite);
        
        DBCursor cursor = collection.find(query);

            while (cursor.hasNext()) { 
                
                DBObject o = cursor.next();
                
                if ( o.get("nom").equals(nom)) {
                    
                    o.put("quantite", quantite);
                    
                    collection.update(o, updateQuantite);
                                        
                }else{
                    
                    throw new WebApplicationException(Response.Status.NOT_FOUND);
                    
                }
  
            }
        return nom + " " + "a bien été modifié(e) la quantité est maintenant de "+ quantite;
    }
    
    
}
