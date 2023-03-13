package dao.connector;
/**
 *
 * @author RAKOTOARISOA
 */
public enum Bdd {
    oracle("Oracle","oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@HOST:1521:SERVER"),
    postgresql("PostgreSQL","org.postgresql.Driver","jdbc:postgresql://HOST:5432/SERVER"),
    mysql("MySQL","com.mysql.jdbc.Driver", "jdbc:mysql://HOST:3306/SERVER");
//    SQLServer("","");

    private final String sgbd;
    private final String driverClass;
    private final String url;
    
    private Bdd(String sgbd,String driver, String url){
        this.sgbd=sgbd;
        this.driverClass=driver;
        this.url=url;
    }

    public String getSgbd(){
        return this.sgbd;
    }
    public String getDriverClass(){
        return driverClass;
    }
    public String getUrl(){
        return url;
    }
    
}
