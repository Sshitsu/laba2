package Classes;



public  class Drinks extends ObjectFromMenu {

    private int milliliters;

    public Drinks(String name, double cost, int hours, int minutes, int milliliters) {
        super(name, cost, hours, minutes);
        this.milliliters = milliliters;
    }

    public int getMilliliters() {
        return milliliters;
    }

    @Override
    public String toString() {
        return "It's a drink " + getName() + ", it cost " + getCost() + ", and time to cook " + getHours() + getMinutes() + ", and " + getMilliliters() + " milliliters ";
    }
}