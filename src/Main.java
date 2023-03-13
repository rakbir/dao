import dao.connector.DbConnector;
import dao.flex.FlexibleDaoGeneric;
import dao.querying.CriteriaCondition;
import dao.querying.Ordering;
import entites.Personne;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author RAKOTOARISOA
 */
public class Main {
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) throws Exception {
        Connection conn=null;
        try{
           FileInputStream fis=new FileInputStream("src/DbProperties.properties");
           DbConnector db=DbConnector.fromPropertiesStream(fis); // récupération des propriétés de connexion à la bdd
           conn=db.connect(); //établissement de la connexion à la base de données
           
           FlexibleDaoGeneric dao=new FlexibleDaoGeneric(conn); //Instanciation du DAO
           Ordering ordering=new Ordering("id","asc"); // ORDER BY ID, ASC
           List<CriteriaCondition>criteres= Arrays.asList(new CriteriaCondition("id",">=","1")); // liste de critères de recherche: WHERE ID >= 1
           List<Personne>pers=dao.findByCriteria(Personne.class,criteres); // requête dans la base de données pour une liste de personnes verifiant les conditions du dessus
           for(Personne personne: pers){
               System.out.println(personne.toString());
           }
           
           pers.get(0).setNom("Rajo"); //Changement du nom du premier element de la liste
           dao.update(pers.get(0));  // update de l'element correspondant dans la base de données 
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(conn!=null){
                conn.close();
            }
        }
    }
}
