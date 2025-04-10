package Models.FIleReaderWriter;

import Main.Main;
import Models.Classes.Drinks;
import Models.Classes.Food;
import Models.Classes.ObjectFromMenu;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FileReaderWriter {
    private static final Logger logger = Logger.getLogger(FileReaderWriter.class);

    private String pathToFile ="src/main/resources/menu.json";
    public FileReaderWriter() {
    }
    public FileReaderWriter(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public List<ObjectFromMenu> readMenuFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode rootNode = mapper.readTree(file);
        return parseFile(rootNode);
    }

    public JsonNode readFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = Main.class.getResourceAsStream(pathToFile);

        if (inputStream == null) {
            logger.log(Level.ERROR, "Resource not found");
            throw new FileNotFoundException("Resource not found");
        }
        return mapper.readTree(inputStream);
    }
    public void saveToJson(List<ObjectFromMenu> menuItems, String pathToFile) throws IOException {
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
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(pathToFile))) {
            mapper.writeValue(writer, jsonItems);
        }
    }

    public  List<ObjectFromMenu> parseFile(JsonNode rootNode) {
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
                }else if(node.get("cost").asInt() <= 0 || node.get("Grams").asDouble() <= 0){
                    logger.log(Level.WARN, "Invalid type of json value");
                    continue;
                } else if (node.get("Time to cook").asText().length() > 8) {
                    logger.log(Level.WARN, "Invalid type of time value");
                    continue;
                }
                String name = node.get("name").asText();

                String timeString = node.get("Time to cook").asText();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Time timeToCook = null;
                try {
                    Date parsedDate = sdf.parse(timeString);
                    timeToCook = new Time(parsedDate.getTime());
                } catch (Exception ex) {
                    logger.log(org.apache.log4j.Level.WARN, "Invalid time value");
                    continue;
                }


                double cost = node.get("cost").asDouble();
                int grams = node.get("Grams").asInt();
                menuItems.add(new Food(name, cost, grams, timeToCook));


            } else if ("Drinks".equalsIgnoreCase(type)) {
                if (node.get("name") == null || node.get("cost") == null || node.get("Milliliters") == null || node.get("Time to cook") == null) {
                    logger.log(Level.WARN, "Invalid type of json value or don't all values matter or they have incorrect value");
                    continue;
                }else if(node.get("cost").asDouble() <= 0 || node.get("Milliliters").asInt() <= 0){
                    logger.log(Level.WARN, "Invalid type of json value");
                    continue;
                }else if (node.get("Time to cook").asText().length() > 8) {
                    logger.log(Level.WARN, "Invalid type of time value");
                    continue;
                }
                String name = node.get("name").asText();
                double cost = node.get("cost").asDouble();

                int milliliters = node.get("Milliliters").asInt();
                Time timeToCook = Time.valueOf(node.get("Time to cook").asText());
                menuItems.add(new Drinks(name, cost, milliliters, timeToCook));
                if(milliliters <= 0 || cost <= 0 ) {
                    logger.log(Level.WARN, "Not correct value");
                    throw new NumberFormatException("Not correct value");
                }
            }
        }
        return menuItems;
    }

}
