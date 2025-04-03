package Main;

import Classes.Food;
import Classes.Drinks;
import Classes.ObjectFromMenu;
import GUI.GUI;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {


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
            throw new FileNotFoundException("Resource not found: menu.json");
        }

        return mapper.readTree(inputStream);
    }



    public static List<ObjectFromMenu> parseFile(JsonNode rootNode) {

        List<ObjectFromMenu> menuItems = new ArrayList<>();

        for (JsonNode node : rootNode) {
            if(node.get("type") == null){
                continue;
            }
            String type = node.get("type").asText();

            if ("Dish".equalsIgnoreCase(type)) {
                if (node.get("name") == null || node.get("cost") == null  || node.get("Grams") == null || node.get("Time to cook") == null) {
                    continue;
                }

                String name = node.get("name").asText();
                double cost = node.get("cost").asDouble();

                int grams = node.get("Grams").asInt();
                Time timeToCook = Time.valueOf(node.get("Time to cook").asText());
                menuItems.add(new Food(name, cost, grams, timeToCook));

            } else if ("Drinks".equalsIgnoreCase(type)) {
                if (node.get("name") == null || node.get("cost") == null || node.get("Milliliters") == null || node.get("Time to cook") == null) {
                    continue;
                }
                String name = node.get("name").asText();
                double cost = node.get("cost").asDouble();

                int milliliters = node.get("Milliliters").asInt();
                Time timeToCook = Time.valueOf(node.get("Time to cook").asText());
                menuItems.add(new Drinks(name, cost,milliliters, timeToCook));
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
