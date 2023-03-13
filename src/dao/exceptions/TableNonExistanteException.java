package dao.exceptions;

import java.util.List;

/**
 *
 * @author RAKOTOARISOA
 */
public class TableNonExistanteException extends Exception{
    public TableNonExistanteException(String table){
        super("Aucune table ne correspond à "+table);
    }
    
    public static void controlTable(String table, List<String>colonnes) throws TableNonExistanteException{
        if(colonnes.isEmpty()){
            throw new TableNonExistanteException(table);
        }
    }
    
}
