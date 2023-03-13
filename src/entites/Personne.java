/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

import dao.flex.annotations.RefColumn;
import dao.flex.annotations.RefTable;
import entites.modele.BaseModele;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "personne")
@RefTable(name="personne")
@RefColumn(name="id", field="id",autoGenerating=true, unique=true)
@AttributeOverride(name="id", column = @Column(name="id"))
public class Personne extends BaseModele{
    @Column(name="nom")
    @RefColumn(name="nom")
    private String nom;
    
    @Column(name="prenom")
    @RefColumn(name="prenom")
    private String prenom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
     @Override
    public String toString(){
        return this.getNom()+"-"+this.getPrenom();
    }
    
    public Personne(){}
    
    public Personne(String n, String p){
        this.setNom(n);
        this.setPrenom(p);
    }
}
