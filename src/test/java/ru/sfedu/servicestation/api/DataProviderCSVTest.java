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
    void calculateMarkupSuccess() throws JAXBException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvInstance.createOrder(order1);
        assertEquals(csvInstance.calculateMarkup(order1.getOrderID()), 250.0);
        csvInstance.deleteOrderByID(1L);
    }

    @Test
    void calculateMarkupFail() throws JAXBException, IOException {
        assertNull(csvInstance.calculateMarkup(30L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculateIndividualMarkupSuccess() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvInstance.createOrder(order1);
        assertEquals(csvInstance.calculateIndividualMarkup(order1), 250.0);
        csvInstance.deleteOrderByID(1L);
    }

    @Test
    void calculateIndividualMarkupFail() throws IOException, JAXBException {
        assertNull(csvInstance.calculateIndividualMarkup(csvInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculateCompanyMarkupSuccess() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvInstance.createOrder(order1);
        assertEquals(csvInstance.calculateCompanyMarkup(order1), 500.0);
        csvInstance.deleteOrderByID(1L);
    }

    @Test
    void calculateCompanyMarkupFail() throws IOException, JAXBException {
        assertNull(csvInstance.calculateCompanyMarkup(csvInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculateIncomeSuccess() throws JAXBException, IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvInstance.createOrder(test_order);
        test_order.setTotalServiceIncome(144.4);
        test_order.setTotalEmployeeIncome(900.0);
        assertEquals(csvInstance.calculateIncome(order1.getOrderID()), test_order);
        csvInstance.deleteOrderByID(1L);
    }

    @Test
    void calculateIncomeFail() throws JAXBException, IOException {
        assertNull(csvInstance.calculateIncome(3L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculatePartsIncomeSuccess() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvInstance.createOrder(order1);
        assertEquals(csvInstance.calculatePartsIncome(order1), 44.400000000000006);
        csvInstance.deleteOrderByID(1L);
    }

    @Test
    void calculatePartsIncomeFail() throws IOException, JAXBException {
        assertNull(csvInstance.calculatePartsIncome(csvInstance.getOrderByID(3L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculateEmployeeIncomeSuccess() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        csvInstance.createOrder(order1);
        assertEquals(csvInstance.calculateEmployeeIncome(order1), 100.0);
        csvInstance.deleteOrderByID(1L);
    }

    @Test
    void calculateEmployeeIncomeFail() throws IOException, JAXBException {
        assertNull(csvInstance.calculateEmployeeIncome(csvInstance.getOrderByID(4L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


}