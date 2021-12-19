package ru.sfedu.servicestation.api;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DataProviderJDBCTest extends TestBase{

    public DataProviderJDBCTest() throws IOException, JAXBException {
    }

    @Test
    void insertEmployee() throws IOException, SQLException {
        jdbcInstance.insertEmployee(employee1);
        jdbcInstance.deleteEmployee(employee1);
    }


    @Test
    void getEmployeeById() throws IOException, SQLException {
        jdbcInstance.insertEmployee(employee2);
        jdbcInstance.getEmployeeById(2L);
        jdbcInstance.deleteEmployee(employee2);
    }

    @Test
    void updateEmployee() {

    }

    @Test
    void deleteEmployee() throws IOException, SQLException {
        jdbcInstance.insertEmployee(employee1);
        jdbcInstance.deleteEmployee(employee1);
    }

    @Test
    void getCarById() throws IOException, SQLException {
        jdbcInstance.insertCar(car1);
        jdbcInstance.getCarById(1L);
        //jdbcInstance.deleteCar(car1);
    }



    @Test
    void insertCar() throws IOException, SQLException {
        jdbcInstance.insertCar(car2);
        jdbcInstance.deleteCar(car2);

    }

}