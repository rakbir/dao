package entites;

import dao.flex.annotations.RefColumn;
import dao.flex.annotations.RefTable;
import entites.modele.BaseModele;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 *
 * @author RAKOTOARISOA
 */
@Entity
@Table(name = "equipe")
@RefTable(name="equipe")
@RefColumn(name="idequipe", field="id",autoGenerating=true, unique=true)
@AttributeOverride(name="id", column = @Column(name="idequipe"))
public class Equipe extends BaseModele{
    
    @Column(name="nomequipe")
    @RefColumn(name="nomequipe")
    private String nomEquipe;
    
    @Column
    @RefColumn(name="abbreviation")
    private String abbreviation;
    
    public String getNomEquipe() {
        return nomEquipe;
    }

    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    
    @Override
    public String toString(){
        return this.getNomEquipe()+"-"+this.getAbbreviation();
    }
    
    public Equipe(int id, String n, String ab){
        super(id);
        this.setNomEquipe(n);
        this.setAbbreviation(ab);
    }
    
    public Equipe(){
    }
}
