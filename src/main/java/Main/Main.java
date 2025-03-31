package Main;

import Classes.Food;
import Classes.Drinks;
import Classes.ObjectFromMenu;
import GUI.GUI;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        // Загрузка данных из файла
        JsonNode rootNode = readFile();
        List<ObjectFromMenu> menuList = parseFile(rootNode);

        // Запуск GUI
        GUI gui = new GUI();
        SwingUtilities.invokeLater(() -> gui.createGUI(menuList));

    }

    // Метод для чтения файла JSON
    public static JsonNode readFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = Main.class.getResourceAsStream("/menu.json");

        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: menu.json");
        }

        return mapper.readTree(inputStream);
    }

    // Метод для парсинга JSON в список объектов
    public static List<ObjectFromMenu> parseFile(JsonNode rootNode) {
        List<ObjectFromMenu> menuItems = new ArrayList<>();

        for (JsonNode node : rootNode) {
            String type = node.get("type").asText();
            String name = node.get("name").asText();
            double cost = node.get("cost").asDouble();
            int hours = node.get("hours").asInt();
            int minutes = node.get("minutes").asInt();

            if ("Dish".equalsIgnoreCase(type)) {
                int grams = node.get("grams").asInt();
                menuItems.add(new Food(name, cost, hours, minutes, grams));
            } else if ("Drinks".equalsIgnoreCase(type)) {
                int milliliters = node.get("milliliters").asInt();
                menuItems.add(new Drinks(name, cost, hours, minutes, milliliters));
            }
        }

        return menuItems;
    }

    // Метод для сохранения данных в JSON файл
    public static void saveToJson(List<ObjectFromMenu> menuItems) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Создаем список для хранения JsonNode
        List<JsonNode> jsonItems = new ArrayList<>();
        for (ObjectFromMenu item : menuItems) {
            ObjectNode itemNode = mapper.createObjectNode();

            // Определяем тип объекта
            String type = item instanceof Food ? "Dish" : "Drinks";
            itemNode.put("type", type);

            // Заполняем JsonNode с нужными полями
            itemNode.put("name", item.getName());
            itemNode.put("cost", item.getCost());
            itemNode.put("hours", item.getHours());
            itemNode.put("minutes", item.getMinutes());

            // Добавляем дополнительные поля в зависимости от типа
            if (item instanceof Food) {
                Food foodItem = (Food) item;
                itemNode.put("grams", foodItem.getGrams());
            } else if (item instanceof Drinks) {
                Drinks drinkItem = (Drinks) item;
                itemNode.put("milliliters", drinkItem.getMilliliters());
            }

            jsonItems.add(itemNode);
        }

        // Путь к файлу для сохранения
        String path = "src/main/resources/menu.json";  // Путь относительно проекта
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(path))) {
            mapper.writeValue(writer, jsonItems);
        }
    }
}
