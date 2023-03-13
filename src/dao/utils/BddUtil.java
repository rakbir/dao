package dao.utils;

import dao.flex.annotations.RefColumn;
import dao.flex.annotations.RefTable;
import dao.flex.querying.ColumnField;
import dao.exceptions.TableNonExistanteException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.Reflexive;

/**
 *
 * @author RAKOTOARISOA
 */
public class BddUtil {
    //même principe que pour setValues 
    public static void setColumnFieldValues(PreparedStatement pst, Object obj, List<ColumnField>attributsToSet) throws Exception{
        int e=1;
        try{
            for(ColumnField colonne : attributsToSet){
                Method m=Reflexive.getAccessor("get", colonne.getAttribut(), obj.getClass());
                Object valeur=m.invoke(obj);
                pst.setObject(e, valeur);
                e++;
            }
        }catch(Exception ex){
            throw ex;
        }
    }    
    
    //Passage des valeurs attributs de l'objet au PreparedStatement selon la liste des valeurs à passer 
    public static void setValues(PreparedStatement pst, Object obj, List<String>attributsToSet) throws Exception{
        try{
            for(String colonne : attributsToSet){
                Method m=Reflexive.getAccessor("get", colonne, obj.getClass());
                pst.setObject(attributsToSet.indexOf(colonne)+1, m.invoke(obj));
            }
        }catch(Exception ex){
            throw ex;
        }
    }
    
    // Rassemblement des lignes de resultsets en une liste d'objet de la classe annotée via ColumnField représentant les colonnes et les fields correspondants
    public static <T>List<T> makelistFromResultset(ResultSet res, Class<T> classe, List<ColumnField>colonnes)throws Exception{
        try{
            List<T>liste=new ArrayList();
            while(res.next()){
                T obj=classe.newInstance();
                for(ColumnField colonne : colonnes){
                    Method m=Reflexive.getAccessor("set", colonne.getAttribut(), classe);
                    m.invoke(obj, res.getObject(colonne.getColonne()));
                }
                liste.add(obj);
            }
            return liste;
        }catch(Exception ex){
            throw ex;
        }
    }
    
    //même principe que pour makelistFromresultset avec simple liste des colonnes en String
    public static <T>List<T> makelistFromResultset(ResultSet res, List<String>colonnes, Class<T> classe)throws Exception{
        try{
            List<T>liste=new ArrayList();
            while(res.next()){
                T obj=classe.newInstance();
                for(String colonne : colonnes){
                    Method m=Reflexive.getAccessor("set", colonne, classe);
                    m.invoke(obj, res.getObject(colonne));
                }
                liste.add(obj);
            }
            return liste;
        }catch(Exception ex){
            throw ex;
        }
    }
    
    //Recherche de la table correspondante à la classe annotée par RefTable et RefColumn
    public static dao.flex.querying.Table findAnnotatedClassCorrespondingTable(Class classe)throws Exception{
        List<ColumnField>colonnes=new ArrayList();
        String nomtable=null;
        RefTable reftable=(RefTable)classe.getAnnotation(RefTable.class);
        nomtable=reftable.name();
        if(nomtable==null){
            throw new Exception("La classe "+classe.getName()+" n'est pas annotée du nom de table");
        }

        RefColumn[] annotationSurType=null;
        if(classe.isAnnotationPresent(RefColumn.class)){
            annotationSurType=(RefColumn[]) classe.getDeclaredAnnotationsByType(RefColumn.class);
        }
        for(RefColumn colonne: annotationSurType){
            colonnes.add(new ColumnField(colonne.name(),
                    Reflexive.getFieldIgnoreCase(colonne.field(), classe),
                    colonne.unique(),
                    colonne.autoGenerating()));
        }

        Field[]fields=classe.getDeclaredFields();
        for(Field field:fields){
            if(field.isAnnotationPresent(RefColumn.class)){
                RefColumn ref=field.getDeclaredAnnotation(RefColumn.class);
                colonnes.add(new ColumnField(ref.name(),
                                            field,
                                            ref.unique(),
                                            ref.autoGenerating()));
            }
        }
        return new dao.flex.querying.Table(nomtable, colonnes, reftable.type());
    }
    
    //recherche de la table correspondante à la classe NON annotée
    //utilisation connexion à la base de données pour y récolter les données nécessaires concernant la table référencée par la classe via le nom
    public static dao.conv.querying.Table findClassCorrespondingTable(Connection bdd, Class classe)throws Exception{
        ResultSet res=null;
        try{
            DatabaseMetaData dmd= bdd.getMetaData();
            String e=classe.getName();
            String tablename=e.substring(e.lastIndexOf(".")+1, e.length()).toLowerCase();
            res=dmd.getColumns("", "", tablename,"");
            List<String>colonnes=new ArrayList();
            while(res.next()){
               colonnes.add(res.getString("COLUMN_NAME"));
            }
            TableNonExistanteException.controlTable(tablename, colonnes);
            return new dao.conv.querying.Table(tablename, colonnes);
        }catch(TableNonExistanteException | SQLException ex){
            throw ex;
        }finally{
            if(res!=null){
                res.close();
            }
        }
    }
    
    //recherche de la table correspondante à l'
    //même principe que pour findClassCorrespondingTable
    public static dao.conv.querying.Table findObjectCorrespondingTable(Connection bdd, Object obj)throws Exception{
        ResultSet res=null;
        try{
            DatabaseMetaData dmd= bdd.getMetaData();
            String tablename=Reflexive.toStringOnlyClass(obj).toLowerCase();
            res=dmd.getColumns("", "", tablename,"");
            List<String>colonnes=new ArrayList();
            while(res.next()){
               colonnes.add(res.getString("COLUMN_NAME"));
            }
            TableNonExistanteException.controlTable(tablename, colonnes);
            return new dao.conv.querying.Table(tablename, colonnes);
        }catch(TableNonExistanteException | SQLException ex){
            throw ex;
        }finally{
            if(res!=null){
                res.close();
            }
        }
    }
    
    //recherche de la table correspondante au nom
    //même principe que pour findClassCorrespondingTable et findObjectCorresondingTable
    public static dao.conv.querying.Table findTable(Connection bdd, String entite)throws Exception{
        ResultSet res=null;
        try{
            DatabaseMetaData dmd= bdd.getMetaData();
            res=dmd.getColumns("", "", entite.toLowerCase() ,"");
            List<String>colonnes=new ArrayList();
            while(res.next()){
               colonnes.add(res.getString("COLUMN_NAME"));
            }
            TableNonExistanteException.controlTable(entite, colonnes);
            return new dao.conv.querying.Table(entite, colonnes);
        }catch(TableNonExistanteException | SQLException ex){
            throw ex;
        }finally{
            if(res!=null){
                res.close();
            }
        }
    }
    
}
