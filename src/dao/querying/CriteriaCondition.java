package dao.querying;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author RAKOTOARISOA
 */

public class CriteriaCondition {
    private static final List<String>OPERATEURS=Arrays.asList("=","!=","<",">","<=",">=");
    
    private String colonne;
    private String operateur;
    private Object valeur;

    public String getColonne() {
        return colonne;
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur)throws Exception{
        if(!OPERATEURS.contains(operateur)){
            throw new Exception("L'opérateur "+operateur+" n'est pas valide");
        }
        this.operateur = operateur;
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }
    
   
    @Override
    public String toString(){
       return this.getColonne()+this.getOperateur()+"'"+this.getValeur()+"'";
    }
    
    public CriteriaCondition(String col, String op, Object val)throws Exception{
        this.setColonne(col);
        this.setOperateur(op);
        this.setValeur(val);
    }
}
