package Classes;



import java.sql.Time;


public  class Food extends ObjectFromMenu {

    private int grams;

    public int getGrams() {
        return grams;
    }

    public Food(){}

    public Food(String name, double cost,int grams, Time timeToCook) {
        super(name, cost,timeToCook);
        this.grams = grams;

    }

    @Override
    public String toString() {
        return "It's a dish " + getName() + ", it cost=" + getCost() + ", and time to cook " + getTimeToCook().toString() + ", and " + getGrams() + " grams ";
    }
}