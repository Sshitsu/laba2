package Main;

import Classes.Food;
import Classes.Drinks;
import Classes.ObjectFromMenu;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = Main.class.getResourceAsStream("/menu.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: menu.json");
        }

        JsonNode rootNode = mapper.readTree(inputStream);


        List<ObjectFromMenu> menuItems = new ArrayList<>();


        for (JsonNode node : rootNode) {
            String type = node.get("type").asText();
            String name = node.get("name").asText();
            double cost = node.get("cost").asDouble();
            int hours = node.get("hours").asInt();
            int minutes = node.get("minutes").asInt();


            if ("dish".equals(type)) {
                int grams = node.get("grams").asInt();
                menuItems.add(new Food(name,cost,hours, minutes, grams));
            } else if ("drinks".equals(type)) {
                int milliliters = node.get("milliliters").asInt();
                menuItems.add(new Drinks(name,cost,hours,minutes, milliliters));
            }
        }


        for (Object item : menuItems) {
            System.out.println(item.toString());
        }
    }
}
