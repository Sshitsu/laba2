package Presenter;



import Models.Classes.Drinks;

import Models.Classes.Dish;
import Models.Classes.ObjectFromMenu;

import Models.FIleReaderWriter.FileReaderWriter;
import View.GUI;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Presenter {
    private static final Logger logger = Logger.getLogger(Presenter.class);

    private List<ObjectFromMenu> menuItems;
    private GUI gui;

    private FileReaderWriter fileReaderWriter;

    public FileReaderWriter getFileReaderWriter() {
        return fileReaderWriter;
    }

    public Presenter(List<ObjectFromMenu> menuItems, GUI gui) {
        this.menuItems = menuItems;
        this.gui = gui;
    }

    public Presenter(List<ObjectFromMenu> menuItems, GUI gui, FileReaderWriter fileReaderWriter) {
        this.menuItems = menuItems;
        this.gui = gui;
        this.fileReaderWriter = fileReaderWriter;
    }

    private boolean isValidName(String name) {

        for (char letter : name.toCharArray()) {
            if (!Character.isLetter(letter)) {
                return false;
            }
        }
        return true;
    }

    private Time parseTime(String timeString, JFrame frame) {



        if (timeString.length() > 8) {
            logger.log(Level.WARN, "Invalid time format");
            JOptionPane.showMessageDialog(frame, "Invalid time format! Please use HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date parsedDate = sdf.parse(timeString);
            return new Time(parsedDate.getTime());
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            logger.log(Level.WARN, "Invalid time format!");
            JOptionPane.showMessageDialog(frame, "Invalid time format! Please use HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    public void addItem(JFrame frame, DefaultTableModel tableModel, JComboBox<String> typeComboBox) {

        String type = (String) typeComboBox.getSelectedItem();
        JTextField nameField = new JTextField(10);
        JTextField costField = new JTextField(10);
        JTextField gramsField = new JTextField(10);
        JTextField millilitersField = new JTextField(10);
        JTextField timeToCookField = new JTextField(10);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Cost:"));
        inputPanel.add(costField);
        inputPanel.add(new JLabel("Time to cook:"));
        inputPanel.add(timeToCookField);

        if ("Dish".equals(type)) {
            inputPanel.add(new JLabel("Grams:"));
            inputPanel.add(gramsField);
        } else if ("Drinks".equals(type)) {
            inputPanel.add(new JLabel("Milliliters:"));
            inputPanel.add(millilitersField);
        }
        int option = JOptionPane.showConfirmDialog(frame, inputPanel, "Enter object details", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String costString = costField.getText().trim();
                String timeString = timeToCookField.getText().trim();

                //проверка на пустые поля
                if (name.isEmpty() || costString.isEmpty() || timeString.isEmpty() ||
                        ("Dish".equals(type) && gramsField.getText().trim().isEmpty()) ||
                        ("Drinks".equals(type) && millilitersField.getText().trim().isEmpty())) {
                    logger.log(Level.WARN, "The fields were not filled in!");
                    JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //проверка на то что, name состоит только из букв
                if (!isValidName(name)) {
                    logger.log(org.apache.log4j.Level.WARN, "Invalid name value");
                    JOptionPane.showMessageDialog(frame, "Invalid name value! Please use only letters.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // проверка на коректнный вод формата времени
                Time timeToCook = parseTime(timeString, frame);
                if(timeToCook == null) return;

                //Проверка на коректнный вод cost and grams or milliliters
                if ("Dish".equals(type)) {
                    int grams = Integer.parseInt(gramsField.getText().trim());
                    double cost = Double.parseDouble(costField.getText().trim());
                    if (grams <= 0 || cost <= 0) {
                        logger.log(Level.WARN, "Invalid numeric values");
                        JOptionPane.showMessageDialog(frame, "Invalid numeric values", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    menuItems.add(new Dish(name, cost, grams, timeToCook));
                    tableModel.addRow(new Object[]{
                            "Dish", name, cost, grams, null, timeToCook
                    });
                } else if ("Drinks".equals(type)) {
                    int milliliters = Integer.parseInt(millilitersField.getText().trim());
                    double cost = Double.parseDouble(costField.getText().trim());
                    if (milliliters <= 0 || cost <= 0) {
                        logger.log(Level.WARN, "Invalid numeric values");
                        JOptionPane.showMessageDialog(frame, "Invalid numeric values", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    menuItems.add(new Drinks(name, cost, milliliters, timeToCook));
                    tableModel.addRow(new Object[]{
                            "Drinks", name, cost, null, milliliters, timeToCook
                    });
                }
            } catch (NumberFormatException ex) {

                System.out.println("Error " + ex.getMessage());
                logger.log(Level.WARN, "Invalid numeric values");
                JOptionPane.showMessageDialog(frame, "Please enter valid numeric values", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void deleteItem(JFrame frame, DefaultTableModel tableModel, int selectedRow){
            if (selectedRow != -1) {
                menuItems.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            } else {
                logger.log(Level.WARN, "Didn't chose any object to delete");
                JOptionPane.showMessageDialog(frame, "Choose an object to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
    }

    public void openFieldOnClick(JFrame frame, DefaultTableModel tableModel){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JSON files", "json"));

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".json")) {
                logger.log(Level.WARN, "Selected item was not the .json");
                JOptionPane.showMessageDialog(frame, "Please select a .json file only!", "Invalid File", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                List<ObjectFromMenu> newMenuItems = fileReaderWriter.readMenuFromFile(selectedFile);
                String path = selectedFile.getAbsolutePath();
                // Обновление таблицы
                gui.updateTable(newMenuItems, tableModel, path);
                JOptionPane.showMessageDialog(frame, "File loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {

                System.out.println("Error " + ex.getMessage());
                logger.log(Level.WARN, "Error reading file");
                JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            logger.log(Level.WARN, "No file selected");
            JOptionPane.showMessageDialog(frame, "No file selected");
        }
    }

    public void clearTable(JFrame frame, DefaultTableModel tableModel){
        tableModel.setRowCount(0);
    }

    public void saveDataToFile(List<ObjectFromMenu> menuItems, String pathToFile, JFrame frame){
        if (pathToFile == null){
            logger.log(Level.WARN, "Don't open any file to save");
            JOptionPane.showMessageDialog(frame, "Don't open any file to save. You must open any file","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            fileReaderWriter.saveToJson(menuItems, pathToFile);
            JOptionPane.showMessageDialog(frame, "Data was save", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {

            System.out.println("Error " + e.getMessage());
            logger.log(Level.ERROR, "Resource not found");
            JOptionPane.showMessageDialog(frame, "Resource not found","Something went wrong", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    public void initializeGui(){
        gui.setPresenter(this);
        SwingUtilities.invokeLater(gui::initialize);
    }

    public void foo(){}

}
