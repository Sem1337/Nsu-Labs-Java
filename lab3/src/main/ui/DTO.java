package main.ui;

public class DTO {

    public DTO(String description, String... args){
        this.description = description;
        this.args = args;
    }

    public DTO(DTO another) {
        this.description = another.description;
        this.args = another.args;
    }


    public String getDescription() {
        return description;
    }

    public String[] getArgs() {
        return args;
    }

    String description;
    String[] args;

}
