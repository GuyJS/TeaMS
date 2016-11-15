package message;

import backend.NameGenerator;

public class FinishedMakingDrinkMessage implements Message {

    private String name;

    public FinishedMakingDrinkMessage(String name){
        this.name = NameGenerator.getName(name);
    }

    @Override
    public String getMessage() {
        return name;
    }

    public String getName(){
        return name;
    }
}
