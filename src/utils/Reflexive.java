package utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author RAKOTOARISOA
 */
public class Reflexive{
        //retourne une chaine du nom de la classe sans le package
        public static String toStringOnlyClass(Object obj){
            String e=obj.getClass().getName();
            return e.substring(e.lastIndexOf(".")+1, e.length());
        }
        
        //Recherche d'un field jusque dans la première classe mère
        public static Field getFieldIgnoreCase(String attribut, Class classe)throws Exception{
            Field toReturn=null;
            try{
                Field[]fields=classe.getDeclaredFields();
                for(Field field : fields){
                    if(attribut.equalsIgnoreCase(field.getName())){
                        toReturn=field;
                    }
                }
                if(toReturn==null){
                    toReturn=getFieldIgnoreCase(attribut, classe.getSuperclass()); 
                }
            }catch(NullPointerException ex){
                throw new Exception("L'attribut "+attribut+" n'existe pas");
            }
            return toReturn;
        }        
        
        //invocation de l'accesseur get ou set 
        //type = set / get 
        public static Method getAccessor(String type, String attribut, Class classe)throws Exception{
            Field att=getFieldIgnoreCase(attribut, classe);
            String f=type.concat(Character.toUpperCase(att.getName().charAt(0))+att.getName().substring(1));
            if(type.equals("set")){
                return classe.getMethod(f, att.getType());
            }
            return classe.getMethod(f);
        }
        
        
        public static Method getAccessor(String type, Field attribut, Class classe)throws Exception{
            String f=type.concat(Character.toUpperCase(attribut.getName().charAt(0))+attribut.getName().substring(1));
            if(type.equals("set")){
                return classe.getMethod(f, attribut.getType());
            }
            return classe.getMethod(f);
        }
        
}
