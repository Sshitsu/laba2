package Classes;

import java.time.LocalTime;


public abstract class ObjectFromMenu {

    private int hours;
    private int minutes;
    private String name;
    private double cost;


    ObjectFromMenu(String name, double cost, int hours, int minutes) {
        this.name = name;
        this.cost = cost;
        this.hours = hours;
        this.minutes = minutes;
    }


    public int getHours(){
        return hours;
    }
    public int getMinutes(){
        return minutes;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void setTimeToCook(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


}