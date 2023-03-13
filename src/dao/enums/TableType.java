package dao.enums;

/**
 *
 * @author RAKOTOARISOA
 */
public enum TableType {
    TABLE("table",true, true, true),
    VIEW("view",false, false, false);
    
    public final String name;
    public final boolean createability;
    public final boolean updateability;
    public final boolean deleteability;
    
    
    private TableType(String name, boolean create, boolean update, boolean delete){
        this.name=name;
        this.createability=create;
        this.updateability=update;
        this.deleteability=delete;
    }
    
    public String getName(){
        return this.name;
    }
    
    public boolean isUpdatable(){
        return this.updateability;
    }
    public boolean isCreateable(){
        return this.createability;
    }
    public boolean isDeleteable(){
        return this.deleteability;
    }
}
