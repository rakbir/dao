package dao.conv.querying;

import dao.querying.CriteriaCondition;
import dao.querying.Ordering;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author RAKOTOARISOA
 */public class Table {
    private String nom;
    private List<String>colonnes;
    private String type; // view , table
            
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<String> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<String> colonnes) {
        this.colonnes = colonnes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public Table(String n, List<String>col){
        this.setNom(n);
        this.setColonnes(col);
    }
    
    // n'insérer que les attributs non null de l'objet 
    public String generateInsertQuery()throws Exception{
        try{
            String sql="insert into "+this.getNom()+" %c values %v";
            List<String>cols=this.getColonnes();
            cols.remove("id");
            
            String colonnes=cols.toString()
                .replace('[','(')
                .replace(']',')');
            
            StringTokenizer st = new StringTokenizer(colonnes, ",");
            String values=colonnes;
            while (st.hasMoreTokens()){
                values=values.replace(st.nextToken(), "?");
            }
            values="("+values+")";            
            sql=sql.replace("%c", colonnes);
            sql=sql.replace("%v", values);            
            return sql;
        }catch(Exception ex){
            throw ex;
        }
    }
    
    //update basique sur seul critère id de l'objet
    public String generateUpdateQuery(List<String>colonnesToUpdate){
        String sql="update "+this.getNom()+" set %s where id=?";
        String settings=colonnesToUpdate.toString();
        settings=settings.replace(",", "=?, ")
                .replace("]", "=?")
                .replace('[', ' ');

        return sql.replace(" %s", settings);
    }
    
    //delete sur seul critère id de l'objet
    public String generateDeleteQuery(){
        String sql="delete from "+this.getNom()+" where id=?";
        return sql;
    }
    
    public String generateSelectionQuery(List<String>colonnesRequises)throws Exception{
        String sql="select * from "+this.getNom();
        if(!colonnesRequises.isEmpty()){
            String cols=colonnesRequises.toString();
            cols=cols.substring(1, cols.length()-1);
            sql=sql.replace("*",cols);
        }
        return sql;
    }
    
    public String whereAnd(List<CriteriaCondition>criteres){
        String condition=criteres.toString();
        condition=condition.replace(","," and");
        return condition.substring(1, condition.length()-1);
    }

//    public String whereOr(List conditions){
//        String condition="";
//        return null;
//    }
    
    public String countAllRowsQuery(){
        return "select count(*) COMPTE from "+this.getNom();
    }
    
    public String paginate(int nbResultats, int page){
        return "limit "+nbResultats+" offset "+(page-1)*nbResultats;
    }
    
    public String orderBy(Ordering ordering){
        return "order by "+ordering.toString(); 
    }
    
    public static String orderBy(List<Ordering>orderings){
        String orders=orderings.toString()
                .replace('[',' ')
                .replace(']', ' ');
        return " order by"+orders;
    }
}