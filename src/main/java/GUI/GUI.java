package GUI;

import Classes.Drinks;
import Classes.Food;
import Classes.ObjectFromMenu;
import Main.Main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GUI {

    public void createGUI(List<ObjectFromMenu> menuItems) {
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);

        // Создание таблицы для отображения объектов меню
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Type");
        tableModel.addColumn("Name");
        tableModel.addColumn("Cost");
        tableModel.addColumn("Grams");
        tableModel.addColumn("Milliliters ");
        tableModel.addColumn("Time to cook");



        for (ObjectFromMenu item : menuItems) {
            if (item instanceof Food) {
                Food food = (Food) item;
                tableModel.addRow(new Object[]{
                        "Dish", food.getName(), food.getCost(), food.getGrams(), null, food.getTimeToCook()
                });
            } else if (item instanceof Drinks) {
                Drinks drink = (Drinks) item;
                tableModel.addRow(new Object[]{
                        "Drinks", drink.getName(), drink.getCost(), null, drink.getMilliliters(),drink.getTimeToCook()
                });
            }
        }


        JTable table = new JTable(tableModel);
        table.setCellSelectionEnabled(true);
        JScrollPane scrollPane = new JScrollPane(table);


        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());


        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Dish", "Drinks"});
        panel.add(typeComboBox);


        JButton addButton = new JButton("Add object");
        addButton.addActionListener(e -> {
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
                    String name = nameField.getText();
                    double cost = Double.parseDouble(costField.getText());

                    String timeString = timeToCookField.getText();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Time timeToCook = null;

                    try {
                        Date parsedDate = sdf.parse(timeString);
                        timeToCook = new Time(parsedDate.getTime());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid time format! Please use HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if ("Dish".equals(type)) {
                        int grams = Integer.parseInt(gramsField.getText());
                        menuItems.add(new Food(name, cost,  grams, timeToCook));
                        tableModel.addRow(new Object[]{
                                "Dish", name, cost, grams, null, timeToCook
                        });
                    } else if ("Drinks".equals(type)) {
                        int milliliters = Integer.parseInt(millilitersField.getText());
                        menuItems.add(new Drinks(name, cost, milliliters, timeToCook));
                        tableModel.addRow(new Object[]{
                                "Drinks", name, cost,null, milliliters, timeToCook
                        });
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JButton deleteButton = new JButton("Delete object");
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                menuItems.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(frame, "Choose an object to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Main.saveToJson(menuItems);
                JOptionPane.showMessageDialog(frame, "Data was saved", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving data", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(saveButton);


        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);


        frame.setVisible(true);
    }
}
