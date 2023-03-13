package dao.daoHibernate;

import dao.basedao.BaseDao;
import entites.modele.BaseModele;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;

/**
 *
 * @author RAKOTOARISOA
 */
public class DaoHibernate implements BaseDao{
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    
    public DaoHibernate(SessionFactory sessionFactoraka){
        this.setSessionFactory(sessionFactoraka);
    }
    
    public void save(BaseModele obj)throws Exception{
        Session session=null;
        Transaction tr=null;
        try{
            session=this.getSessionFactory().openSession();
            tr=session.beginTransaction();
            session.save(obj);
            tr.commit();
        }catch(HibernateException ex){
            if(tr!=null){
                tr.rollback();
            }
            throw ex;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }
    
    public void update(BaseModele obj)throws Exception{
        Session session=null;
        Transaction tr=null;
        try{
            session=this.getSessionFactory().openSession();
            tr=session.beginTransaction();
            session.saveOrUpdate(tr);
            tr.commit();
        }catch(HibernateException ex){
            if(tr!=null){
                tr.rollback();
            }
            throw ex;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }
    
    public void delete(BaseModele obj)throws Exception{
        Session session=null;
        Transaction tr=null;
        try{
            session.delete(obj);
            tr.commit();
        }catch(HibernateException ex){
            if(tr!=null){
                tr.rollback();
            }
            throw ex;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }
    
    public <T>List<T> findAll(Class<T>classe)throws Exception{
        Session session=null;
        try{
            session=this.getSessionFactory().openSession();
            Criteria criteria=session.createCriteria(classe);
            return criteria.list();
        }catch(Exception ex){
            throw ex;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }
    
    
    public List<BaseModele> findAll(BaseModele obj)throws Exception{
        Session session=null;
        try{
            session=this.getSessionFactory().openSession();
            Criteria criteria=session.createCriteria(obj.getClass());
            Example example=Example.create(obj);
            criteria.add(example);
            return criteria.list();
        }catch(Exception ex){
            throw ex;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }
    
    public void findById(BaseModele obj)throws Exception{
        Session session=null;
        try{
            session=this.getSessionFactory().openSession();
            if(obj.getId() != null){
//                session.load(obj, obj.getId());
            }
        }catch(Exception ex){
            throw ex;
        }finally{
            if(session!=null){
                session.close();
            }
        }
    }

}
