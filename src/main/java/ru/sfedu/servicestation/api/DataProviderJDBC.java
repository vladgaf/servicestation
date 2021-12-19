package ru.sfedu.servicestation.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.sfedu.servicestation.beans.*;
import ru.sfedu.servicestation.beans.Employee;
import ru.sfedu.servicestation.utils.ConfigurationUtil;
import ru.sfedu.servicestation.utils.Constants;
import ru.sfedu.servicestation.utils.JDBCConnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataProviderJDBC extends AbstractDataProvider {

    private static final Logger log = LogManager.getLogger(DataProviderJDBC.class);
    MongoDBDataProvider mongoDBDataProvider=new MongoDBDataProvider();

    private Connection connection=null;
    private Statement statement=null;
    private final String PATH_TO_SQL= ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_SQL);

    public DataProviderJDBC() throws IOException {
    }

    private String getClassName(){
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }
    private String getMethodName(){
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    private void initConnection() throws SQLException {
        connection = JDBCConnect.getConnection();
        statement = connection.createStatement();
    }
    private void closeConnection() throws SQLException {
        connection.close();
        statement.close();
    }
    public void initDataSource() throws IOException, SQLException {
        File file=new File(PATH_TO_SQL);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();
        String query = new String(data, StandardCharsets.UTF_8);
        initConnection();
        statement.executeUpdate(query);
        closeConnection();
    }

    public void dropTables() throws SQLException {
        initConnection();
        statement.executeUpdate(Constants.DROP_ALL_TABLES);
        closeConnection();
    }

    //DROP TABLES METHODS


    //RESULTSET CONVERTERS

    public List<Employee> getEmployeeListFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        initDataSource();
        initConnection();
        List<Employee> employeeList = new ArrayList<>();
        while (resultSet.next()) {
            Employee employee = new Employee();
            employee.setEmployeeID(resultSet.getLong(1));
            employee.setEmployeeName(resultSet.getString(2));
            employeeList.add(employee);
        }
        closeConnection();
        return employeeList;
    }

    public List<Car> getCarListFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        initDataSource();
        initConnection();
        List<Car> carList = new ArrayList<>();
        while (resultSet.next()) {
            Car car = new Car();
            car.setCarID(resultSet.getLong(1));
            car.setBrand(resultSet.getString(2));
            car.setModel(resultSet.getString(3));
            car.setYear(resultSet.getInt(4));
            car.setEngine(resultSet.getString(5));
            carList.add(car);
        }
        closeConnection();
        return carList;
    }
    //LISTS CONVERTERS

    public List<Employee> getEmployeeList() throws SQLException, IOException {
        initDataSource();
        initConnection();
        ResultSet rs = statement.executeQuery(Constants.SELECT_EMPLOYEE);
        List<Employee> employeeList=getEmployeeListFromResultSet(rs);
        closeConnection();
        return employeeList;
    }

    public List<Car> getCarList() throws SQLException, IOException {
        initDataSource();
        initConnection();
        ResultSet rs = statement.executeQuery(Constants.SELECT_CAR);
        List<Car> carList=getCarListFromResultSet(rs);
        closeConnection();
        return carList;
    }


    //CRUD


    //EMPLOYEE METHODS

    public Employee getEmployeeById(Long id) throws IOException, SQLException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Employee> employeeList = getEmployeeList();
        Employee employee=employeeList.stream()
                .filter(x-> id.equals(x.getEmployeeID()))
                .findAny()
                .orElse(null);
        if(employee == null){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_EMPLOYEE_NOT_FOUND);
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee,Constants.EMPLOYEE,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        return employee;
    }

    public void updateEmployee(Employee employee) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        initDataSource();
        initConnection();
        statement.executeUpdate(String.format(Constants.UPDATE_EMPLOYEE_BY_ID,
                employee.getEmployeeID(),
                employee.getEmployeeName()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.EMPLOYEE, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    public void insertEmployee(Employee employee) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        initDataSource();
        initConnection();
        try {
            statement.executeUpdate(String.format(Constants.CREATE_EMPLOYEE,
                    employee.getEmployeeID(),
                    employee.getEmployeeName()));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.EMPLOYEE, className, methodName), Constants.MONGODB_TEST_SERVER);
        } catch (JdbcSQLIntegrityConstraintViolationException e){
        } finally {
            closeConnection();
        }
    }

    public void deleteEmployee(Employee employee) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        initDataSource();
        initConnection();
        statement.executeUpdate(String.format(Constants.DELETE_EMPLOYEE,employee.getEmployeeID()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.EMPLOYEE, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    //CAR METHODS


    public Car getCarById(Long id) throws IOException, SQLException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Car> carList = getCarList();
        Car car=carList.stream()
                .filter(x-> id.equals(x.getCarID()))
                .findAny()
                .orElse(null);
        if(car == null){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_CAR_NOT_FOUND);
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car,Constants.CAR,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        return car;
    }

    public void updateCar(Car car) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        initDataSource();
        initConnection();
        statement.executeUpdate(String.format(Constants.UPDATE_CAR_BY_ID,
                car.getCarID(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getEngine()
        ));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CAR, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    public void insertCar(Car car) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        initDataSource();
        initConnection();
        try {
            statement.executeUpdate(String.format(Constants.CREATE_CAR,
                    car.getCarID(),
                    car.getBrand(),
                    car.getModel(),
                    car.getYear(),
                    car.getEngine()
            ));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CAR, className, methodName), Constants.MONGODB_TEST_SERVER);
        } catch (JdbcSQLIntegrityConstraintViolationException e){
        } finally {
            closeConnection();
        }
    }

    public void deleteCar(Car car) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        initDataSource();
        initConnection();
        statement.executeUpdate(String.format(Constants.DELETE_CAR,car.getCarID()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CAR, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    //USE CASE METHODS


}
