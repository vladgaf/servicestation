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

    //Objects examples

    public Car car1 = createCar(1L, "Toyota", "Camry", 2018, "V6");
    public Car car2 = createCar(2L, "Honda", "Civic", 2001, "RB20DET");
    public Car car2_upd = createCar(2L, "Honda", "Civic", 2003, "RB20DET");


    public Client client1 = createClient(1L, "EgorDeadInside1000-7", ClientType.INDIVIDUAL, car1);
    public Client client2 = createClient(2L, "Oleg", ClientType.EMPLOYEE, car2);

    public Employee employee1 = createEmployee(1L, "Sergay");
    public Employee employee2 = createEmployee(2L, "Denis");

    public EnginePart enginePart1 = createEnginePart(1L, "Air filter", 199, true, 90, "Gasoline",
            923532, 2.5F);
    public EnginePart enginePart2 = createEnginePart(2L, "Nozzle", 19, false, 100, "Gasoline",
            654546, 1.6F);

    public ChassisPart chassisPart1 = createChassisPart(3L, "Spring", 20, true, 100, "L", "Independent");
    public ChassisPart chassisPart2 = createChassisPart(4L, "Rail", 9, true, 95, "L", "Independent");

    public ElectricityPart electricityPart1 = createElectricityPart(5L, "Fuse", 3, true, 1.6F, 12.0F);
    public ElectricityPart electricityPart2 = createElectricityPart(6L, "Cleat", 10, true, 1.6F, 12.0F);


    public Order order1 = createOrder(1L, client1, enginePart1, chassisPart1, electricityPart1, employee1, 1000.0);
    public Order test_order = order1;


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
        employee.setName(name);
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

    public Order createOrder(Long orderID, Client client, EnginePart enginePart, ChassisPart chassisPart, ElectricityPart electricityPart,
                             Employee employee, Double employeeSalary){

        Order order = new Order();

        List <EnginePart> engineParts = new ArrayList<>();
        List <ChassisPart> chassisParts = new ArrayList<>();
        List <ElectricityPart> electricityParts = new ArrayList<>();
        engineParts.add(enginePart);
        chassisParts.add(chassisPart);
        electricityParts.add(electricityPart);

        order.setOrderID(orderID);
        order.setClient(client);
        order.setEngineParts(engineParts);
        order.setChassisParts(chassisParts);
        order.setElectricityParts(electricityParts);
        order.setEmployee(employee);
        order.setEmployeeSalary(employeeSalary);

        return order;
    }


    // Clear files after testing

    public void clearData(String path){
        try {
            for (File myFile : new File(path).listFiles())
                if (myFile.isFile()) myFile.delete();
        }  catch (Exception e) {
            log.error(e);
        }
    }
}