package View;

import Models.FIleReaderWriter.FileReaderWriter;
import Presenter.Presenter;

import Models.Classes.Drinks;
import Models.Classes.Food;
import Models.Classes.ObjectFromMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;



public class GUI {

    private List<ObjectFromMenu> menuItems;
    private String pathToFile = null;
    private Presenter presenter;

    public GUI(List<ObjectFromMenu> menuItems) {
        this.menuItems = menuItems;
        this.presenter =  new Presenter(menuItems, this);
    }
    public GUI() {
        this.menuItems = new ArrayList<>();
        this.presenter =  new Presenter(menuItems, this, new FileReaderWriter());
    }

    public void initialize() {

        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 400);


        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Type");
        tableModel.addColumn("Name");
        tableModel.addColumn("Cost");
        tableModel.addColumn("Grams");
        tableModel.addColumn("Milliliters ");
        tableModel.addColumn("Time to cook");


        JTable table = new JTable(tableModel);
        table.setCellSelectionEnabled(true);
        JScrollPane scrollPane = new JScrollPane(table);


        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());


        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Dish", "Drinks"});
        panel.add(typeComboBox);

        JButton addButton = new JButton("Add object");
        addButton.addActionListener(e -> {
            presenter.addItem(frame, tableModel, typeComboBox);
        });

        JButton deleteButton = new JButton("Delete object");
        deleteButton.addActionListener(e ->{
            int selectedRow = table.getSelectedRow();
            presenter.deleteItem(frame, tableModel,selectedRow);
        });

        JButton clearTable = new JButton("Clear table");
        clearTable.addActionListener(e -> {
            presenter.clearTable(frame,tableModel);
            menuItems.clear();
        });

        JButton openFileButton = new JButton("Open File");
        openFileButton.addActionListener(e ->{
            presenter.openFieldOnClick(frame, tableModel);
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            presenter.saveDataToFile(menuItems, pathToFile, frame);
        });

        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(saveButton);
        panel.add(clearTable);
        panel.add(openFileButton);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void updateTable(List<ObjectFromMenu> newMenuItems, DefaultTableModel tableModel, String pathToFile) {

        tableModel.setRowCount(0);

        this.menuItems.clear();
        this.menuItems.addAll(newMenuItems);
        this.pathToFile = pathToFile;

        for (ObjectFromMenu item : newMenuItems) {
            if (item instanceof Food) {
                Food food = (Food) item;
                tableModel.addRow(new Object[]{
                        "Dish", food.getName(), food.getCost(), food.getGrams(), null, food.getTimeToCook()
                });
            } else if (item instanceof Drinks) {
                Drinks drink = (Drinks) item;
                tableModel.addRow(new Object[]{
                        "Drinks", drink.getName(), drink.getCost(), null, drink.getMilliliters(), drink.getTimeToCook()
                });
            }
        }
    }

}
