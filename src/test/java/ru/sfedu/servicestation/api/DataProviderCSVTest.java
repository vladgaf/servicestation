package ru.sfedu.servicestation.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import org.junit.jupiter.api.Test;
import ru.sfedu.servicestation.utils.ConfigurationUtil;
import ru.sfedu.servicestation.utils.Constants;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataProviderCSVTest extends TestBase{

    public DataProviderCSVTest() throws IOException, JAXBException {
    }

    @Test
    void createCar() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createCar(car1);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void deleteCarByID() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createCar(car2);
        csvInstance.deleteCarByID(2L);
        assertNull(csvInstance.getCarByID(2L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }

    @Test
    void updateCarByID() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createCar(car2);
        csvInstance.updateCarByID(car2_upd);
        assertEquals(csvInstance.getCarByID(2L).getYear(), 2003);
    }

    @Test
    void testCreateClient() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createClient(client1);
        //clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }

    @Test
    void getClientByID() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createClient(client1);
        assertEquals(csvInstance.getClientByID(1L), client1);
    }

    @Test
    void deleteClientByID() {
    }

    @Test
    void testCreateEmployee() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createEmployee(employee1);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }

    @Test
    void getEmployeeByID() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createEmployee(employee1);
        csvInstance.getEmployeeByID(1L);
    }

    @Test
    void deleteEmployeeByID() {
    }

    @Test
    void testCreateEnginePart() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, JAXBException {
        csvInstance.createEnginePart(enginePart1);
    }

    @Test
    void testCreateOrder() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createOrder(order1);
        csvInstance.getOrderByID(1L);
    }

    @Test
    void testCreateEnginePart1() {
    }
}