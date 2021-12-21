package ru.sfedu.servicestation.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestBase {
    public Logger log = LogManager.getLogger(TestBase.class);

    public DataProviderCSV csvInstance = new DataProviderCSV();
    public DataProviderXML xmlInstance = new DataProviderXML();
    public DataProviderJDBC jdbcInstance = new DataProviderJDBC();

    //Objects examples

    public Car car1 = createCar(1L, "Toyota", "Camry", 2018, "V6");
    public Car car2 = createCar(2L, "Honda", "Civic", 2001, "RB20DET");
    public Car car2_upd = createCar(3L, "Honda", "Civic", 2003, "RB20DET");


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



    //public Order order1 = createOrder(14L, client1, enginePart1, chassisPart1, electricityPart1, employee1, 1000.0);


    public Order order1 = createOrder(15L, client1, generatePartsIDList(), employee1, 1000.0);
    public Order test_order = order1;

    public Long corruptedOrderID = 323L;
    public Double expectedIndividualMarkup = 250.0;
    public Double expectedCompanyMarkup = 500.0;
    public Double expectedPartsIncome = 44.400000000000006;
    public Double expectedEmployeeIncome = 100.0;
    public Double expectedTotalServiceIncome = 144.4;
    public Double expectedTotalEmployeeIncome = 900.0;

    public TestBase() throws IOException, JAXBException {
    }

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

    public List<Long> generatePartsIDList(){
        List<Long> partIDlist = new ArrayList<>();
        partIDlist.add(enginePart1.getPartID());
        partIDlist.add(chassisPart1.getPartID());
        partIDlist.add(electricityPart1.getPartID());
        return partIDlist;
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

    public void createPartsList(){
        List list = new ArrayList<>();
        list.add(enginePart1);
        list.add(chassisPart1);
        list.add(electricityPart1);
        log.info(list);

    }

    // Clear files after testing

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
