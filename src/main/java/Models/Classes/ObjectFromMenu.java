package Models.Classes;

import java.sql.Time;

public abstract class ObjectFromMenu {

    private String name;
    private double cost;
    private Time timeToCook;

    ObjectFromMenu(String name, double cost, Time timeToCook) {
        this.name = name;
        this.cost = cost;
        this.timeToCook = timeToCook;
    }

    public Time getTimeToCook() {
        return timeToCook;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

}