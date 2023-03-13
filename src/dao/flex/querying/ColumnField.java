package dao.flex.querying;

import java.lang.reflect.Field;

/**
 *
 * @author RAKOTOARISOA
 */
public class ColumnField {
    String colonne;
    Field attribut;
    boolean unique; //primary key
    boolean autogenerating; //génération de la colonne gérée par la BDD

    public String getColonne() {
        return colonne;
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public Field getAttribut() {
        return attribut;
    }

    public void setAttribut(Field attribut) {
        this.attribut = attribut;
    }
    public void setUnique(boolean unique){
        this.unique=unique;
    }
    
    public boolean isUnique(){
        return this.unique;
    }
    
    public void setAutogenerating(boolean auto){
        this.autogenerating=auto;
    }
    public boolean isAutogenerating(){
        return this.autogenerating;
    }
    
    @Override
    public String toString(){
        return this.getColonne();
    }
    
    public ColumnField(String c, Field a, boolean unique, boolean auto){
        this.setColonne(c);
        this.setAttribut(a);
        this.setUnique(unique);
        this.setAutogenerating(auto);
    }
}
