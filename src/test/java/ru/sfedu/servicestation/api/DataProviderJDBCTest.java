package ru.sfedu.servicestation.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Test;
import ru.sfedu.servicestation.beans.ChassisPart;
import ru.sfedu.servicestation.beans.ElectricityPart;
import ru.sfedu.servicestation.beans.EnginePart;
import ru.sfedu.servicestation.utils.ConfigurationUtil;
import ru.sfedu.servicestation.utils.Constants;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataProviderJDBCTest extends TestBase{

    public DataProviderJDBCTest() throws IOException, JAXBException {
    }



    @Test
    void calculateMarkupSuccess() throws JAXBException, IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        //log.debug(jdbcInstance.calculateMarkup(order1.getOrderID()));
        assertEquals(jdbcInstance.calculateMarkup(order1.getOrderID()), 250.0);
        dropAll();
    }

    @Test
    void calculateMarkupFail() throws JAXBException, IOException {
        assertNull(jdbcInstance.calculateMarkup(30L));
    }


    @Test
    void calculateIndividualMarkupSuccess() throws IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        assertEquals(jdbcInstance.calculateIndividualMarkup(order1), 250.0);
        dropAll();
    }

    @Test
    void calculateIndividualMarkupFail() throws IOException, JAXBException, SQLException {
        assertNull(jdbcInstance.calculateIndividualMarkup(jdbcInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculateCompanyMarkupSuccess() throws IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        assertEquals(jdbcInstance.calculateCompanyMarkup(order1), 500.0);
        dropAll();
    }

    @Test
    void calculateCompanyMarkupFail() throws IOException, JAXBException, SQLException {
        assertNull(jdbcInstance.calculateCompanyMarkup(jdbcInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculateIncomeSuccess() throws JAXBException, IOException, SQLException {
        jdbcInstance.insertOrder(test_order);
        test_order.setTotalServiceIncome(144.4);
        test_order.setTotalEmployeeIncome(900.0);
        test_order.setTotalMarkup(0.0);
        log.debug(jdbcInstance.calculateIncome(order1.getOrderID()));
        assertEquals(jdbcInstance.calculateIncome(order1.getOrderID()), test_order);
        dropAll();
    }

    @Test
    void calculateIncomeFail() throws JAXBException, IOException {
        assertNull(jdbcInstance.calculateIncome(3L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculatePartsIncomeSuccess() throws IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        assertEquals(jdbcInstance.calculatePartsIncome(order1), 44.400000000000006);
        dropAll();
    }

    @Test
    void calculatePartsIncomeFail() throws IOException, JAXBException, SQLException {
        assertNull(jdbcInstance.calculatePartsIncome(jdbcInstance.getOrderByID(3L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
    }


    @Test
    void calculateEmployeeIncomeSuccess() throws IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        assertEquals(jdbcInstance.calculateEmployeeIncome(order1), 100.0);
        dropAll();
    }

    @Test
    void calculateEmployeeIncomeFail() throws IOException, JAXBException, SQLException {
        assertNull(jdbcInstance.calculateEmployeeIncome(jdbcInstance.getOrderByID(4L)));
        dropAll();
    }

    void dropAll() throws SQLException {
        jdbcInstance.dropTables();
    }


}