package Models;

import java.sql.Time;



public abstract class ObjectFromMenu {


    private String name;
    private double cost;

    private Time timeToCook;


    public ObjectFromMenu() {

    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


}