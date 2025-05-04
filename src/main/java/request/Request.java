package request;

import classes.SpaceMarine;

import java.io.Serializable;

public class Request implements Serializable {
    private String[] args;
    SpaceMarine spaceMarine = null;
    public Request(String[] args) {
        this.args = args;
    }
    public void addSpaceMarine(SpaceMarine spaceMarine) {
        this.spaceMarine = spaceMarine;
    }
    public String[] getArgs() {
        return args;
    }
    public SpaceMarine getSpaceMarine() {
        return spaceMarine;
    }
}
