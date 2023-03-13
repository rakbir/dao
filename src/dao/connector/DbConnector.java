package dao.connector;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 *
 * @author RAKOTOARISOA
 */
public class DbConnector {
    private String sgbd; 
    private String host; // addresse du serveur de la base de données
    private String database; //database, server
    private String user; //role, user
    protected String password;
    
    public DbConnector(){}
    
    public DbConnector(String sgbd,String host,String db, String usr, String pass){
        this.setSgbd(sgbd);
        this.setHost(host);
        this.setDatabase(db);
        this.setUser(usr);
        this.setPassword(pass);
    }
    
    public static DbConnector fromPropertiesStream(InputStream input) throws Exception{
        final Properties prop = new Properties();
        String sgbd=null, host=null, db=null, usr=null, pass=null;
        try{
            prop.load(input);
            sgbd=prop.getProperty("sgbd").trim();
            host=prop.getProperty("host").trim();
            db=prop.getProperty("database").trim();
            usr=prop.getProperty("user").trim();
            pass=prop.getProperty("password").trim();
            return new DbConnector(sgbd, host, db, usr, pass);
        }catch(IOException ex){
            throw ex;
        }finally{
            input.close();
        }
    }
    
    public void setSgbd(String sgbd){
        this.sgbd=sgbd;
    }
    public String getSgbd(){
        return this.sgbd;
    }
    
    public void setHost(String host){
        this.host=host;
    }
    public String getHost(){
        return this.host;
    }
    
    public void setDatabase(String db){
        this.database=db;
    }
    public String getDatabase(){
        return this.database;
    }
    
    public void setPassword(String p){
        this.password=p;
    }
    public String getPassword(){
        return this.password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public String toString(){
        return this.getDatabase()+" "+this.getHost()+" "+this.getSgbd()+" "+this.getPassword();
    }
    
    public Connection connect()throws Exception{
        Connection connection=null;
        Bdd bdd=null;
        try{
            bdd=Bdd.valueOf(this.getSgbd());
            String url=bdd.getUrl().replace("HOST", this.getHost())
                                    .replace("SERVER",this.getDatabase());
            Class.forName(bdd.getDriverClass());
            connection=DriverManager.getConnection(url, this.getUser(), this.getPassword());
        }catch(IllegalArgumentException ex){
            throw new Exception("SGBD non gérée: "+this.getSgbd());
        }catch(ClassNotFoundException ex){
            throw new Exception("Classe driver absente: "+bdd.getDriverClass());
        }catch(SQLException ex){
            throw new Exception("Impossible de se connecter à la base de données");
        }
        return connection;
    }
}