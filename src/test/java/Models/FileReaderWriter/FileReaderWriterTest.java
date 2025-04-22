package Models.FileReaderWriter;

import Models.Classes.Drinks;
import Models.Classes.Dish;
import Models.Classes.ObjectFromMenu;
import Models.FIleReaderWriter.FileReaderWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class FileReaderWriterTest {

    private FileReaderWriter fileReaderWriter;

    @BeforeEach
    void setup(){
        fileReaderWriter = new FileReaderWriter();

    }

    @Test
    void testSaveToJsonAndReadBack() throws IOException {
        Dish dish = new Dish("Burger", 5.5, 200, Time.valueOf("00:15:00"));
        Drinks drink = new Drinks("Pepsi", 2.2, 500, Time.valueOf("00:02:00"));
        List<ObjectFromMenu> list = List.of(dish, drink);

        String testPath = "src/test/resources/test_menu.json";
        fileReaderWriter.saveToJson(list, testPath);
        File file = new File(testPath);
        assertTrue(file.exists());

        List<ObjectFromMenu> result = fileReaderWriter.readMenuFromFile(file);

        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof Dish);
        assertEquals("Burger", result.get(0).getName());

    }

    @Test
    void testParseFileWithValidJsonNode() throws Exception{
        String json = """
                [
                  {
                    "type": "Dish",
                    "name": "Salad",
                    "cost": 3.5,
                    "Grams": 150,
                    "Time to cook": "00:10:00"
                  },
                  {
                    "type": "Drinks",
                    "name": "Tea",
                    "cost": 1.5,
                    "Milliliters": 300,
                    "Time to cook": "00:03:00"
                  }
                ]
                """;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        List<ObjectFromMenu> menuListTest = fileReaderWriter.parseFile(root);

        assertEquals(2, menuListTest.size());
        assertInstanceOf(Dish.class, menuListTest.get(0));
        assertInstanceOf(Drinks.class, menuListTest.get(1));
        assertEquals("Tea", menuListTest.get(1).getName());
    }

    @Test
    void testParseFileWithInvalidJsonNode() throws Exception{
        String json = """
                [
                  {
                    "type": "Dish",
                    "name": "Salad"
                  },
                  {
                    "type": "Drinks",
                    "name": "Tea",
                    "cost": 1.5,
                    "Milliliters": 300,
                    "Time to cook": "00:03:00"
                  }
                ]
                """;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        List<ObjectFromMenu> menuListTest = fileReaderWriter.parseFile(root);

        assertEquals(1, menuListTest.size());
        assertEquals("Tea", menuListTest.get(0).getName());
    }

    @Test
    void testParseFileWithInvalidDataFormatValue() throws Exception{
        String json = """
                [
                  {
                    "type": "Dish",
                    "name": "Salad",
                    "cost": 3.5,
                    "Grams": 100,
                    "Time to cook": "011a0:1a0:00"
                  },
                  {
                    "type": "Drinks",
                    "name": "Tea",
                    "cost": 42141,
                    "Milliliters": 100,
                    "Time to cook": "10:00:00"
                  }
                ]
                """;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        List<ObjectFromMenu> menuListTest = fileReaderWriter.parseFile(root);

        assertEquals(1, menuListTest.size());
        assertEquals("Tea", menuListTest.get(0).getName());
        //Должен вывести в лог сообщение "Invalid type of time value"

    }

}
