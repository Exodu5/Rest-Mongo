/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.projetstageafpaalteca2016;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cfremont
 */

public class Produit implements Serializable{
    
    
    private String nom;
    
 
    private String lieu;
    
    
    private int quantite;

    
    public Produit() {
    }
    
    
    public Produit(String nom, String lieu, int quantite) {
        this.nom = nom;
        this.lieu = lieu;
        this.quantite = quantite;
    }
    
    

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    
         
    
    
}
