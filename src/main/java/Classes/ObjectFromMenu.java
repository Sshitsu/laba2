package Classes;

import java.time.LocalTime;


public abstract class ObjectFromMenu {
    private LocalTime timeToCook;
    private String name;
    private double cost;


    ObjectFromMenu(String name, double cost, int hours, int minutes) {
        this.name = name;
        this.cost = cost;
        this.timeToCook = LocalTime.of(hours, minutes);
    }


    public LocalTime getTimeToCook() {
        return timeToCook;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void setTimeToCook(int hours, int minutes) {
        this.timeToCook = LocalTime.of(hours,minutes);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


}