package ru.sfedu.servicestation.api;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DataProviderJDBCTest2 extends TestBase{

    public DataProviderJDBCTest2() throws IOException, JAXBException {
    }

    @Test
    void calculateMarkupSuccess() throws JAXBException, IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        log.debug(jdbcInstance.calculateMarkup(order1.getOrderID()));
        assertEquals(jdbcInstance.calculateMarkup(order1.getOrderID()), 250.0);
        jdbcInstance.getOrderByID(order1.getOrderID());
        jdbcInstance.deleteOrder(order1);

    }

    @Test
    void calculateIncomeSuccess() throws JAXBException, IOException, SQLException {
        jdbcInstance.insertOrder(test_order);
        test_order.setTotalServiceIncome(144.4);
        test_order.setTotalEmployeeIncome(900.0);
        log.debug(jdbcInstance.calculateIncome(order1.getOrderID()));
        assertEquals(jdbcInstance.calculateIncome(order1.getOrderID()), test_order);
        jdbcInstance.deleteOrder(test_order);
    }

    @Test
    void crudOrder() throws IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        jdbcInstance.getOrderByID(order1.getOrderID());
        //jdbcInstance.deleteOrder(order1);
    }

    @Test
    void updateOrder() throws IOException, SQLException {
        jdbcInstance.insertOrder(order1);
        Double totalServiceIncome = 14.19;
        order1.setTotalServiceIncome(totalServiceIncome);
        order1.setTotalEmployeeIncome(24.4);
        order1.setTotalMarkup(34.4);
        jdbcInstance.updateOrder(order1);
        log.debug(order1.getTotalServiceIncome());
        jdbcInstance.getOrderByID(order1.getOrderID());

    }

    @Test
    void dropAll() throws SQLException {
        jdbcInstance.dropTables();
    }
}