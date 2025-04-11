package Main;


import Models.FIleReaderWriter.FileReaderWriter;
import Presenter.Presenter;
import View.GUI;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;

import java.io.*;
import java.util.ArrayList;




public class Main {
    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        FileReaderWriter fileReaderWriter = new FileReaderWriter();
        GUI gui = new GUI();
        Presenter presenter = new Presenter(new ArrayList<>(), gui, fileReaderWriter);
        presenter.initializeGui();

    }
}
