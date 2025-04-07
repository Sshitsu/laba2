package Models;




import java.sql.Time;


public  class Drinks extends ObjectFromMenu {

    private int milliliters;

    public Drinks(){}
    public Drinks(String name, double cost,int milliliters, Time timeToCook) {
        super(name, cost, timeToCook);
        this.milliliters = milliliters;
    }

    public int getMilliliters() {
        return milliliters;
    }

    @Override
    public String toString() {
        return "It's a drink " + getName() + ", it cost " + getCost() + ", and time to cook " + getTimeToCook().toString() + getMilliliters() + " milliliters ";
    }
}