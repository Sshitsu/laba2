package Main;

import Classes.Food;
import Classes.Drinks;
import Classes.ObjectFromMenu;
import GUI.GUI;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;


import javax.swing.*;
import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;




public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        logger.info("Application started");

        JsonNode rootNode = readFile();
        List<ObjectFromMenu> menuList = parseFile(rootNode);


        GUI gui = new GUI();
        SwingUtilities.invokeLater(
                () -> gui.createGUI(menuList)
        );

    }


    public static JsonNode readFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        InputStream inputStream = Main.class.getResourceAsStream("/menu.json");

        if (inputStream == null) {
            logger.log(Level.WARN, "Resource not found");
            throw new FileNotFoundException("Resource not found: menu.json");
        }

        return mapper.readTree(inputStream);
    }



    public static List<ObjectFromMenu> parseFile(JsonNode rootNode) {

        List<ObjectFromMenu> menuItems = new ArrayList<>();

        for (JsonNode node : rootNode) {
            if(node.get("type") == null){
                logger.log(Level.WARN, "Invalid type of json value");
                continue;
            }
            String type = node.get("type").asText();

            if ("Dish".equalsIgnoreCase(type)) {
                if (node.get("name") == null || node.get("cost") == null  || node.get("Grams") == null || node.get("Time to cook") == null) {
                    logger.log(Level.WARN, "Invalid type of json value or don't all values matter or they have incorrect value");
                    continue;
                }

                String name = node.get("name").asText();
                double cost = node.get("cost").asDouble();

                int grams = node.get("Grams").asInt();
                Time timeToCook = Time.valueOf(node.get("Time to cook").asText());
                menuItems.add(new Food(name, cost, grams, timeToCook));
                //logger.log(Level.INFO, "Add new object" + new Food(name, cost, grams, timeToCook).toString());

            } else if ("Drinks".equalsIgnoreCase(type)) {
                if (node.get("name") == null || node.get("cost") == null || node.get("Milliliters") == null || node.get("Time to cook") == null) {
                    logger.log(Level.WARN, "Invalid type of json value or don't all values matter or they have incorrect value");
                    continue;
                }
                String name = node.get("name").asText();
                double cost = node.get("cost").asDouble();

                int milliliters = node.get("Milliliters").asInt();
                Time timeToCook = Time.valueOf(node.get("Time to cook").asText());
                menuItems.add(new Drinks(name, cost, milliliters, timeToCook));
                //logger.log(Level.INFO, "Add new object" + new Drinks(name, cost, milliliters, timeToCook).toString());
            }
        }
        return menuItems;
    }


    public static void saveToJson(List<ObjectFromMenu> menuItems) throws IOException {
        ObjectMapper mapper = new ObjectMapper();


        List<JsonNode> jsonItems = new ArrayList<>();
        for (ObjectFromMenu item : menuItems) {


            ObjectNode itemNode = mapper.createObjectNode();


            String type = item instanceof Food ? "Dish" : "Drinks";
            itemNode.put("type", type);
            itemNode.put("name", item.getName());
            itemNode.put("cost", item.getCost());

            itemNode.put("Time to cook", item.getTimeToCook().toString());


            if (item instanceof Food) {
                Food foodItem = (Food) item;
                itemNode.put("Grams", foodItem.getGrams());
            } else if (item instanceof Drinks) {
                Drinks drinkItem = (Drinks) item;
                itemNode.put("Milliliters", drinkItem.getMilliliters());
            }

            jsonItems.add(itemNode);
        }


        String path = "src/main/resources/menu.json";
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(path))) {
            mapper.writeValue(writer, jsonItems);
        }
    }
}
