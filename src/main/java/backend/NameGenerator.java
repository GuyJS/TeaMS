package backend;

public class NameGenerator {

    public static String getName(String name){
        if(name == null || name.isEmpty()){
            return "A mysterious stranger";
        }
        return name;
    }

}
