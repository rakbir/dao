package dao.basedao;

import entites.modele.BaseModele;
import java.util.List;

/**
 *
 * @author RAKOTOARISOA
 */
public interface BaseDao{
    public void save(BaseModele obj)throws Exception;
    public void update(BaseModele obj)throws Exception;
    public void delete(BaseModele obj) throws Exception;    
    public <T>List<T>findAll(Class<T> classe)throws Exception;
}
