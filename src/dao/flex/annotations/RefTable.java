package dao.flex.annotations;

import dao.enums.TableType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author RAKOTOARISOA
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RefTable {
    public String name();
    public TableType type() default TableType.TABLE;    
}
