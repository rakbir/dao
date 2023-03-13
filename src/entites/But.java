package entites;

import dao.flex.annotations.RefColumn;
import dao.flex.annotations.RefTable;
import entites.modele.BaseModele;

/**
 *
 * @author RAKOTOARISOA
 */

@RefTable(name="but")
@RefColumn(name="idbut", field="id", unique=true, autoGenerating=true)
public class But extends BaseModele{    
    @RefColumn(name="idmatche")
    public int idMatche;
    
    @RefColumn(name="idequipe")
    public int idEquipe;

    public int getIdMatche() {
        return idMatche;
    }

    public void setIdMatche(int idMatche) {
        this.idMatche = idMatche;
    }

    public int getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }
    
    public String toString(){
        return this.getId()+"-"+this.getIdEquipe()+"-"+this.getIdMatche();
    }
    
    public But(){
    }
    
    public But(int id, int idmatche, int idEquipe){
        this.setId(id);
        this.setIdMatche(idmatche);
        this.setIdEquipe(idEquipe);
    }
}
