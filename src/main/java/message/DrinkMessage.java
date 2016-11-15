package message;

import backend.NameGenerator;

public class DrinkMessage implements Message {

    private String message;
    private String name;

    public DrinkMessage(String name, String msg) {
        this.message = msg;
        this.name = NameGenerator.getName(name);
    }

    public String getMessage(){
        return message;
    }

    public String getName(){
        return name;
    }
}