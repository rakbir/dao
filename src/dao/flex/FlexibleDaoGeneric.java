package dao.flex;

import dao.utils.BddUtil;
import dao.enums.TableType;
import dao.exceptions.OperationNonPermiseException;
import dao.flex.querying.ColumnField;
import dao.flex.querying.Table;
import dao.querying.CriteriaCondition;
import dao.querying.Ordering;
import dao.basedao.BaseDao;
import entites.modele.BaseModele;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author RAKOTOARISOA
 */
public class FlexibleDaoGeneric implements BaseDao{
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
   
    public FlexibleDaoGeneric(Connection bdd)throws Exception{
       this.setConnection(bdd);
    }
    
    public void controloperation(Table table)throws OperationNonPermiseException{
        if(table.getType().compareTo(TableType.VIEW)==0){
            throw new OperationNonPermiseException();
        }
    }
    
    public void save(BaseModele obj)throws Exception{
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(obj.getClass());
            this.controloperation(table);
            List<ColumnField>colonnesToInsert=table.getNonAutogeneratedColumns();
            String sql=table.generateInsertQuery(colonnesToInsert);
            pst=this.getConnection().prepareStatement(sql);
            BddUtil.setColumnFieldValues(pst, obj, colonnesToInsert);
            pst.execute();
        }catch(OperationNonPermiseException ex){
            throw new Exception("Les insertions dans les views ne sont pas permises");
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
        }
    }
    
    public void update(BaseModele obj)throws Exception{
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(obj.getClass());
            this.controloperation(table);
            String sql=table.generateUpdateQuery();
            pst=this.getConnection().prepareStatement(sql);
            List<ColumnField>colonnes=table.getColonnesfield();
            colonnes.addAll(table.getUniqueKeys());
            BddUtil.setColumnFieldValues(pst, obj, colonnes);
            pst.execute();
        }catch(OperationNonPermiseException ex){
            throw new Exception("Les updates dans les views ne sont pas permises");
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
        }
    }
    
    public void delete(BaseModele obj)throws Exception{
         PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(obj.getClass());
            this.controloperation(table);
            String sql=table.generateDeleteQuery();
            pst=this.getConnection().prepareStatement(sql);
            BddUtil.setColumnFieldValues(pst, obj, table.getUniqueKeys());
            pst.execute();
        }catch(OperationNonPermiseException ex){
            throw new Exception("Les suppressions dans les views ne sont pas permises");
        }catch(Exception ex){
            throw ex;
        }finally{
            if(pst!=null){
                pst.close();
            }
        }
    }
    
    public <T>List<T> findAll(Class<T> classe)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(classe);
            String sql=table.generateSelectionQuery();
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            return BddUtil.makelistFromResultset(res, classe, table.getColonnesfield());
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
    
    public <T>List<T>findOrderedList(Class<T> classe, Ordering ordering)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(classe);
            String sql=table.generateSelectionQuery()+
                        table.orderBy(ordering);
            pst=this.getConnection().prepareStatement(sql);
            System.out.println(pst.toString());
            res=pst.executeQuery();            
            return BddUtil.makelistFromResultset(res,classe, table.getColonnesfield());
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
    
     public <T>List<T>findByCriteria(Class<T> classe, List<CriteriaCondition>conditions)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(classe);
            String sql=table.generateSelectionQuery();
            if(!conditions.isEmpty()){
                sql=sql+" where "+table.whereAnd(conditions);
            }
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            return BddUtil.makelistFromResultset(res, classe, table.getColonnesfield());
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
    
    public int countAvailablePages(Class classe, Integer nbLignes)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(classe);
            if(nbLignes==null){
                nbLignes=Table.LIST_ROWS;
            }
            String sql=table.countAllRowsQuery();
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            res.next();
            return res.getInt("COMPTE")/nbLignes;
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
    
    public <T>List<T>pagination(Class<T> classe, Integer nbLignes , int page)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(classe);
            String sql=table.generateSelectionQuery()+table.paginate(nbLignes, page);
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();
            return BddUtil.makelistFromResultset(res, classe, table.getColonnesfield());
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
    
    public <T>List<T>findOrderedListPageByCriteria(Class<T> classe, List<CriteriaCondition>conditions, Integer nbLignes, int page, Ordering ordering)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(classe);
            String sql=table.generateSelectionQuery()+
                        " where "+table.whereAnd(conditions)+
                        table.orderBy(ordering)+
                        table.paginate(nbLignes, page);
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();            
            return BddUtil.makelistFromResultset(res,classe, table.getColonnesfield());
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
    
    
    public <T>List<T>findOrderedListPageByCriteria(Class<T> classe, List<CriteriaCondition>conditions, Integer nbLignes, int page, List<Ordering> orderings)throws Exception{
        ResultSet res=null;
        PreparedStatement pst=null;
        try{
            Table table=BddUtil.findAnnotatedClassCorrespondingTable(classe);
            String sql=table.generateSelectionQuery()+
                        " where "+table.whereAnd(conditions)+
                        table.orderBy(orderings)+
                        table.paginate(nbLignes, page);
            pst=this.getConnection().prepareStatement(sql);
            res=pst.executeQuery();            
            return BddUtil.makelistFromResultset(res, classe, table.getColonnesfield());
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
