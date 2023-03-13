package dao.conv;

import dao.utils.BddUtil;
import dao.querying.CriteriaCondition;
import dao.querying.Ordering;
import dao.conv.querying.Table;
import entites.modele.BaseModele;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author RAKOTOARISOA
 */
public class DaoGeneric {
    private static final int LIST_ROWS=20;
    private Connection connection;

    public Connection getConnection(){
        return connection;
    }

    public void setConnection(Connection connection)throws Exception{
        try{
            if(connection.isClosed()){
                throw new NullPointerException();
            }
            this.connection = connection;
        }catch(NullPointerException ex){
            throw new Exception("Veuillez-ouvrir une nouvelle connection à la base de données");
        }
    }
   
    public DaoGeneric(Connection bdd)throws Exception{
       this.setConnection(bdd);
    }
            
    public void create(Object obj)throws Exception{
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), obj.getClass());
            String sql=table.generateInsertQuery();
            pst=this.getConnection().prepareStatement(sql);
            BddUtil.setValues(pst, obj, table.getColonnes());
            pst.execute();
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
        }
    }
    
    public void update(BaseModele obj, List<String>colonnesToUpdate)throws Exception{        
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), obj.getClass());
            String sql=table.generateUpdateQuery(colonnesToUpdate);
            pst=this.getConnection().prepareStatement(sql);
            BddUtil.setValues(pst, obj, colonnesToUpdate);
            pst.setObject(colonnesToUpdate.size()+1, obj.getId());
            pst.execute();
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
        }
    }
    
    public void delete(BaseModele obj) throws Exception{
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), obj.getClass());
            String sql=table.generateDeleteQuery();
            pst=this.getConnection().prepareStatement(sql);
            pst.setObject(1, obj.getId());
            pst.execute();
        }catch(Exception ex){
            throw ex;
        }
        finally{
            if(pst!=null){
                pst.close();
            }
        }
    }
    
    public <T>List<T> findAll(Class<T> classe, List<String>colonnesRequises)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), classe);
            String sql=table.generateSelectionQuery(colonnesRequises);
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            if(colonnesRequises.isEmpty()){
                colonnesRequises=table.getColonnes();
            }
            return BddUtil.makelistFromResultset(res, colonnesRequises, classe);
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
            if(res!=null){
                res.close();
            }
        }
    }
    
    //Pourra servir de findAll en donnant une liste vide de conditions
    public <T>List<T>findByCriteria(Class<T> classe, List<String>colonnesRequises, List<CriteriaCondition>conditions)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), classe);
            String sql=table.generateSelectionQuery(colonnesRequises);
            if(!conditions.isEmpty()){
                sql=sql+" where "+table.whereAnd(conditions);
            }
            if(colonnesRequises.isEmpty()){
                colonnesRequises=table.getColonnes();
            }
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            
            return BddUtil.makelistFromResultset(res, colonnesRequises, classe);
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
            if(res!=null){
                res.close();
            }
        }
    }
    
    public int countAvailablePages(Class classe)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), classe);
            String sql=table.countAllRowsQuery();
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            res.next();
            return res.getInt("COMPTE")/LIST_ROWS;
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
            if(res!=null){
                res.close();
            }
        }
    }
    
    public <T>List<T>pagination(Class<T> classe , List<String>colonnesRequises, int page)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), classe);
            String sql=table.generateSelectionQuery(colonnesRequises)+table.paginate(LIST_ROWS, page);
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            return BddUtil.makelistFromResultset(res, colonnesRequises, classe);
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
            if(res!=null){
                res.close();
            }
        }
    }
    
    public <T>List<T>findOrderedListPageByCriteria(Class<T> classe , List<String>colonnesRequises, List<CriteriaCondition>conditions, int page, Ordering ordering)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), classe);
            String sql=table.generateSelectionQuery(colonnesRequises)+
                        " where "+table.whereAnd(conditions)+
                        table.orderBy(ordering)+
                        table.paginate(LIST_ROWS, page);
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();            
            return BddUtil.makelistFromResultset(res, colonnesRequises, classe);
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
            if(res!=null){
                res.close();
            }
        }
    }
    
    
    public <T>List<T>findOrderedListPageByCriteria(Class<T> classe, List<String>colonnesRequises, List<CriteriaCondition>conditions, int page, List<Ordering> orderings)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findClassCorrespondingTable(this.getConnection(), classe);
            String sql=table.generateSelectionQuery(colonnesRequises)+
                        " where "+table.whereAnd(conditions)+
                        table.orderBy(orderings)+
                        table.paginate(LIST_ROWS, page);
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();            
            return BddUtil.makelistFromResultset(res, colonnesRequises, classe);
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
            if(res!=null){
                res.close();
            }
        }
    }
    
}
