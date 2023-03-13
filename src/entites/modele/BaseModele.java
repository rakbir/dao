package entites.modele;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author RAKOTOARISOA
 */

@MappedSuperclass
public class BaseModele implements Serializable{
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Object id;
    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
    
    public BaseModele(Object i){
        this.setId(i);
    }
    public BaseModele(){}
    
}
