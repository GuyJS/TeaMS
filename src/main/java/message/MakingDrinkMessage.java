package message;

import backend.NameGenerator;

public class MakingDrinkMessage implements Message {

    private String name;

    public MakingDrinkMessage(String name){
        this.name = NameGenerator.getName(name);
    }

    @Override
    public String getMessage() {
        return name + " is making a drink!";
    }

    public String getName(){
        return name;
    }
}
