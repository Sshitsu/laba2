package GUI;

import Classes.Drinks;
import Classes.Food;
import Classes.ObjectFromMenu;
import Main.Main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
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
        tableModel.addColumn("Time to cook (hours)");
        tableModel.addColumn("Time to cook (minutes)");
        tableModel.addColumn("Grams or milliliters");

        // Заполнение таблицы данными
        for (ObjectFromMenu item : menuItems) {
            if (item instanceof Food) {
                Food food = (Food) item;
                tableModel.addRow(new Object[]{
                        "Dish", food.getName(), food.getCost(), food.getHours(), food.getMinutes(), food.getGrams()
                });
            } else if (item instanceof Drinks) {
                Drinks drink = (Drinks) item;
                tableModel.addRow(new Object[]{
                        "Drinks", drink.getName(), drink.getCost(), drink.getHours(), drink.getMinutes(), drink.getMilliliters()
                });
            }
        }

        // Создание таблицы
        JTable table = new JTable(tableModel);
        table.setCellSelectionEnabled(true);
        JScrollPane scrollPane = new JScrollPane(table);

        // Панель с кнопками
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // JComboBox для выбора типа объекта (Food или Drinks)
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Dish", "Drinks"});
        panel.add(typeComboBox);

        // Кнопка для добавления нового объекта
        JButton addButton = new JButton("Add object");
        addButton.addActionListener(e -> {
            String type = (String) typeComboBox.getSelectedItem();
            JTextField nameField = new JTextField(10);
            JTextField costField = new JTextField(10);
            JTextField hoursField = new JTextField(10);
            JTextField minutesField = new JTextField(10);
            JTextField additionalField = new JTextField(10);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(6, 2));
            inputPanel.add(new JLabel("Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Cost:"));
            inputPanel.add(costField);
            inputPanel.add(new JLabel("Hours:"));
            inputPanel.add(hoursField);
            inputPanel.add(new JLabel("Minutes:"));
            inputPanel.add(minutesField);

            // Добавляем специфичные поля для типа
            if ("Dish".equals(type)) {
                inputPanel.add(new JLabel("Grams:"));
                inputPanel.add(additionalField);
            } else if ("Drinks".equals(type)) {
                inputPanel.add(new JLabel("Milliliters:"));
                inputPanel.add(additionalField);
            }

            int option = JOptionPane.showConfirmDialog(frame, inputPanel, "Enter object details", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText();
                    double cost = Double.parseDouble(costField.getText());
                    int hours = Integer.parseInt(hoursField.getText());
                    int minutes = Integer.parseInt(minutesField.getText());
                    int additional = Integer.parseInt(additionalField.getText());

                    if ("Dish".equals(type)) {
                        menuItems.add(new Food(name, cost, hours, minutes, additional));
                        tableModel.addRow(new Object[]{
                                "Dish", name, cost, hours, minutes, additional
                        });
                    } else if ("Drinks".equals(type)) {
                        menuItems.add(new Drinks(name, cost, hours, minutes, additional));
                        tableModel.addRow(new Object[]{
                                "Drinks", name, cost, hours, minutes, additional
                        });
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Кнопка для удаления выбранного объекта
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

        // Кнопка для сохранения изменений в файл
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Main.saveToJson(menuItems);
                JOptionPane.showMessageDialog(frame, "Data was saved", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving data", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Добавляем кнопки на панель
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(saveButton);

        // Добавляем элементы в окно
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        // Отображаем окно
        frame.setVisible(true);
    }
}
