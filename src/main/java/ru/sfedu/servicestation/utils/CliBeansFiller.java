package ru.sfedu.servicestation.utils;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.api.DataProviderCSV;
import ru.sfedu.servicestation.api.DataProviderJDBC;
import ru.sfedu.servicestation.api.DataProviderXML;
import ru.sfedu.servicestation.beans.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CliBeansFiller {

    private static final Logger log = LogManager.getLogger(CliBeansFiller.class);
    public DataProviderCSV csvInstance = new DataProviderCSV();
    public DataProviderXML xmlInstance = new DataProviderXML();
    public DataProviderJDBC jdbcInstance = new DataProviderJDBC();

    public CliBeansFiller() throws IOException, JAXBException {}

    //Objects examples

    public Car car1 = createCar(1L, "Toyota", "Camry", 2018, "V6");
    public Car car2 = createCar(2L, "Honda", "Civic", 2001, "RB20DET");


    public Client client1 = createClient(4L, "Vladimir", ClientType.INDIVIDUAL, car1);
    public Client client2 = createClient(5L, "Oleg", ClientType.EMPLOYEE, car2);

    public Employee employee1 = createEmployee(6L, "Sergay");
    public Employee employee2 = createEmployee(7L, "Denis");

    public EnginePart enginePart1 = createEnginePart(8L, "Air filter", 199, true, 90, "Gasoline",
            923532, 2.5F);
    public EnginePart enginePart2 = createEnginePart(9L, "Nozzle", 19, false, 100, "Gasoline",
            654546, 1.6F);

    public ChassisPart chassisPart1 = createChassisPart(10L, "Spring", 20, true, 100, "L", "Independent");
    public ChassisPart chassisPart2 = createChassisPart(11L, "Rail", 9, true, 95, "L", "Independent");

    public ElectricityPart electricityPart1 = createElectricityPart(12L, "Fuse", 3, true, 1.6F, 12.0F);
    public ElectricityPart electricityPart2 = createElectricityPart(13L, "Cleat", 10, true, 1.6F, 12.0F);


    public Order order1 = createOrder(15L, client1, generatePartsIDList(), employee1, 1000.0);

    private List<Long> generatePartsIDList() {
        List<Long> partIDlist = new ArrayList<>();
        partIDlist.add(enginePart1.getPartID());
        partIDlist.add(chassisPart1.getPartID());
        partIDlist.add(electricityPart1.getPartID());
        return partIDlist;
    }

    public Order test_order = order1;


    //Class constructors

    public Client createClient(Long clientID, String name, ClientType clientType, Car car){
        Client client = new Client();
        client.setClientID(clientID);
        client.setName(name);
        client.setClientType(clientType);
        client.setCar(car);
        return client;
    }

    public Car createCar(Long carID, String brand, String model, Integer year, String engine){
        Car car = new Car();
        car.setCarID(carID);
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);
        car.setEngine(engine);
        return car;
    }

    public Employee createEmployee(Long employeeID, String name){
        Employee employee = new Employee();
        employee.setEmployeeID(employeeID);
        employee.setEmployeeName(name);
        return employee;
    }

    public EnginePart createEnginePart(Long partID, String name, Integer price, Boolean availability,
                                       Integer condition, String fuel, Integer serialNumber, Float volume){
        EnginePart enginePart = new EnginePart();
        enginePart.setPartID(partID);
        enginePart.setName(name);
        enginePart.setPrice(price);
        enginePart.setAvailability(availability);
        enginePart.setCondition(condition);
        enginePart.setFuel(fuel);
        enginePart.setSerialNumber(serialNumber);
        enginePart.setVolume(volume);
        return enginePart;
    }

    public ChassisPart createChassisPart(Long partID, String name, Integer price, Boolean availability,
                                         Integer condition, String side, String chassisType){
        ChassisPart chassisPart = new ChassisPart();
        chassisPart.setPartID(partID);
        chassisPart.setName(name);
        chassisPart.setPrice(price);
        chassisPart.setAvailability(availability);
        chassisPart.setCondition(condition);
        chassisPart.setSide(side);
        chassisPart.setChassisType(chassisType);
        return chassisPart;
    }

    public ElectricityPart createElectricityPart(Long partID, String name, Integer price, Boolean availability, Float engineVolume, Float power){
        ElectricityPart electricityPart = new ElectricityPart();
        electricityPart.setPartID(partID);
        electricityPart.setName(name);
        electricityPart.setPrice(price);
        electricityPart.setAvailability(availability);
        electricityPart.setEngineVolume(engineVolume);
        electricityPart.setPower(power);
        return electricityPart;
    }

    private Order createOrder(Long orderID, Client client, List<Long> partIDList, Employee employee, Double employeeSalary) {
        Order order = new Order();
        order.setOrderID(orderID);
        order.setClient(client);
        order.setPartIDList(partIDList);
        order.setEmployee(employee);
        order.setEmployeeSalary(employeeSalary);
        return order;
    }


    public void defaultBeansXML() throws IOException, JAXBException {
        xmlInstance.createChassisPart(chassisPart1);
        xmlInstance.createEnginePart(enginePart1);
        xmlInstance.createElectricityPart(electricityPart1);
        xmlInstance.createCar(car1);
        xmlInstance.createClient(client1);
        xmlInstance.createEmployee(employee1);
        xmlInstance.createOrder(order1);
        log.info(Constants.BEANS_FILLED);
    }

    public void defaultBeansCSV() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        csvInstance.createChassisPart(chassisPart1);
        csvInstance.createEnginePart(enginePart1);
        csvInstance.createElectricityPart(electricityPart1);
        csvInstance.createCar(car1);
        csvInstance.createClient(client1);
        csvInstance.createEmployee(employee1);
        csvInstance.createOrder(order1);
        log.info(Constants.BEANS_FILLED);

    }

    public void defaultBeansJDBC() throws IOException, SQLException {
        jdbcInstance.dropTables();
        jdbcInstance.insertChassisPart(chassisPart1);
        jdbcInstance.insertEnginePart(enginePart1);
        jdbcInstance.insertElectricityPart(electricityPart1);
        jdbcInstance.insertOrder(order1);
    }


    //CLEAR DATA

    public void dropBeansXML() throws IOException {
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
        log.info(Constants.XML_TABLES_DPOPPED);
    }

    public void dropBeansCSV() throws IOException {
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV));
        log.info(Constants.CSV_TABLES_DPOPPED);
    }

    public void dropBeansJDBC() throws SQLException {
        jdbcInstance.dropTables();
    }

    public void clearData(String path){
        try {
            for (File myFile : new File(path).listFiles())
                if (myFile.isFile())
                    myFile.delete();
        }  catch (Exception e) {
            log.error(e);
        }
    }

}
