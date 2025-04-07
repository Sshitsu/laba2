package Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DrinksTest {
    @Test
    void test(){
        Drinks drinks = new Drinks();
        drinks.setName("name");
        Assertions.assertEquals("name", drinks.getName());
    }

}
