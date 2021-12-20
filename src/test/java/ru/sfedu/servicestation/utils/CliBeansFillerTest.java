package ru.sfedu.servicestation.utils;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CliBeansFillerTest {

    @Test
    void defaultBeansXML() throws IOException, JAXBException {
        CliBeansFiller cliBeansFiller = new CliBeansFiller();
        cliBeansFiller.defaultBeansXML();
    }

    @Test
    void defaultBeansCSV() throws IOException, JAXBException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        CliBeansFiller cliBeansFiller = new CliBeansFiller();
        cliBeansFiller.defaultBeansCSV();
    }

    @Test
    void defaultBeansJDBC() throws CsvRequiredFieldEmptyException, IOException, JAXBException, SQLException {
        CliBeansFiller cliBeansFiller = new CliBeansFiller();
        cliBeansFiller.defaultBeansJDBC();
    }

    @Test
    void dropBeansCSV() throws IOException, JAXBException {
        CliBeansFiller cliBeansFiller = new CliBeansFiller();
        cliBeansFiller.dropBeansCSV();
    }
}