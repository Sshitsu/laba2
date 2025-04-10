package Main;

import Models.FIleReaderWriter.FileReaderWriter;
import Presenter.Presenter;
import View.GUI;
import org.apache.log4j.PropertyConfigurator;
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
