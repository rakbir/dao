package dao.querying;

/**
 *
 * @author RAKOTOARISOA
 */
public class Ordering {
    private String colonne;
    private String sens; //desc ou asc
 
    public String getColonne(){
        return colonne;
    }
    public void setColonne(String col){
        this.colonne=col;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sns) {
        this.sens = sns;
    }
    
    public Ordering(String col, String sns){
        this.setColonne(col);
        this.setSens(sns);
    }
    
    @Override
    public String toString(){
        return this.getColonne()+" "+this.getSens();
    }
}
