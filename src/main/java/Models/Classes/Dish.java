package Models.Classes;

import java.sql.Time;

public  class Dish extends ObjectFromMenu {
    private int grams;

    public Dish(String name, double cost, int grams, Time timeToCook) {
        super(name, cost,timeToCook);
        this.grams = grams;

    }
    public int getGrams() {
        return grams;
    }

    @Override
    public String toString() {
        return "It's a dish " + getName() + ", it cost=" + getCost() + ", and time to cook " + getTimeToCook().toString() + ", and " + getGrams() + " grams ";
    }
}