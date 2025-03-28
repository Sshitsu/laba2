package Classes;

import java.time.LocalTime;

public  class Food extends ObjectFromMenu {

    private int grams;

    public int getGrams() {
        return grams;
    }

    public Food(String name, double cost, int hours, int minutes, int grams) {
        super(name, cost, hours, minutes);
        this.grams = grams;
    }

    @Override
    public String toString() {
        return "It's a dish " + getName() + ", it cost=" + getCost() + ", and time to cook " + getTimeToCook() + ", and " + getGrams() + " grams ";
    }
}