package ru.sfedu.servicestation.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import ru.sfedu.servicestation.beans.*;
import ru.sfedu.servicestation.utils.ConfigurationUtil;
import ru.sfedu.servicestation.utils.Constants;
import ru.sfedu.servicestation.utils.XMLList;

public class DataProviderXML extends AbstractDataProvider{

    private static final Logger log = LogManager.getLogger(DataProviderXML.class);

    private static final MongoDBDataProvider mongoDBDataProvider = new MongoDBDataProvider();

    private final String PATH_TO_XML = ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML);
    private final String XML_FILE_EXTENSION = ConfigurationUtil.getConfigurationEntry(Constants.XML_FILE_EXTENSION);

    private FileReader fr;

    private final JAXBContext jaxbContext = JAXBContext.newInstance(XMLList.class);
    private final Marshaller marshaller = jaxbContext.createMarshaller();
    private final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

    public DataProviderXML() throws IOException, JAXBException {}

    private String getClassName(){

        return Thread.currentThread().getStackTrace()[2].getClassName();
    }
    private String getMethodName(){

        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }


    public void initDataSource(String string) throws IOException {
        string = PATH_TO_XML + string + XML_FILE_EXTENSION;
        File file = new File(string);
        if (!file.exists()) {
            Path dirPath = Paths.get(PATH_TO_XML);
            Files.createDirectories(dirPath);
            file.createNewFile();
        }
    }

    private File initFile(String string) throws IOException {
        initDataSource(string);
        String PATH= ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML) +
                string + ConfigurationUtil.getConfigurationEntry(Constants.XML_FILE_EXTENSION);
        return new File(PATH);
    }

    private void initReader(String string) throws IOException {
        this.fr = new FileReader(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML)+
                string + ConfigurationUtil.getConfigurationEntry(Constants.XML_FILE_EXTENSION));
    }
    
    private void closeReader() throws IOException {
        if (fr != null){
            fr.close();
            fr = null;
        }
    }

    private void findPart(Long partID){

    }

    //Client methods

    private void clientsToXML(List<Client> clList) throws JAXBException, IOException {
        try{
            initReader(Constants.CLIENT);
            XMLList clientList=new XMLList();
            clientList.setClients(clList);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(clientList,initFile(Constants.CLIENT));
            closeReader();
        } catch(JAXBException e) {
            log.error(e);
            log.info(Constants.ERROR_FILE);
        }
    }

    private List<Client> clientsFromXML() throws JAXBException {
        try{
            XMLList clientList=(XMLList) unmarshaller.unmarshal(initFile(Constants.CLIENT));
            List<Client> clList=clientList.getClients();
            return clList;
        } catch (Exception e){
            return new ArrayList<Client>();
        }
    }

    private List<Client> sortClientList(List<Client> clientList) throws IOException {
        clientList=clientList.stream().sorted((o1, o2)->o1.getClientID().compareTo(o2.getClientID())).collect(Collectors.toList());
        return clientList;
    }

    public List<Client> getClientList() throws JAXBException, IOException {
        List<Client> clientList=clientsFromXML();
        clientList=sortClientList(clientList);
        return clientList;
    }

    public void createClient(Client client) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Client> clientList=clientsFromXML();
        clientList.add(client);
        clientList=sortClientList(clientList);
        clientsToXML(clientList);

        saveToLog(mongoDBDataProvider.initHistoryContentTrue(client, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Client getClientByID(Long id) throws JAXBException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Client> clientList=clientsFromXML();

        Client client=clientList.stream().filter(x-> id.equals(x.getClientID())).findAny().orElse(null);

        if(client == null){
            log.error(Constants.ERROR_CLIENT_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            return null;
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(client,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
            return client;
        }
    }

    public void deleteClientByID(Long id) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Client> clientList=clientsFromXML();

        try{
            Client client = clientList.stream().filter(x-> id.equals(x.getClientID())).findAny().get();
            clientList=clientList.stream().filter(x-> !id.equals(x.getClientID())).collect(Collectors.toList());
            clientsToXML(clientList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(client,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e) {
            log.error(Constants.ERROR_CLIENT_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }



    }

    public void updateClientByID(Client client) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();


        List<Client> clientList=clientsFromXML();
        try{
            client.getClientID().equals(clientList.stream().filter(x -> client.getClientID().equals(x.getClientID())).findFirst().get().getClientID());
            clientList=clientList.stream().filter(x-> !client.getClientID().equals(x.getClientID())).collect(Collectors.toList());
            clientsToXML(clientList);
            clientList.add(client);
            clientList=sortClientList(clientList);
            clientsToXML(clientList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(client,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            log.error(Constants.ERROR_CLIENT_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }

    //Car methods

    private void carsToXML(List<Car> clList) throws JAXBException, IOException {
        try{
            initReader(Constants.CAR);
            XMLList carList=new XMLList();
            carList.setCars(clList);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(carList,initFile(Constants.CAR));
            closeReader();
        } catch(JAXBException e) {
            log.error(e);
            log.error(Constants.ERROR_FILE);
        }
    }

    private List<Car> carsFromXML() throws JAXBException {
        try{
            XMLList carList=(XMLList) unmarshaller.unmarshal(initFile(Constants.CAR));
            List<Car> clList=carList.getCars();
            return clList;
        } catch (Exception e){
            return new ArrayList<Car>();
        }
    }

    private List<Car> sortCarList(List<Car> carList) throws IOException {
        carList=carList.stream().sorted((o1, o2)->o1.getCarID().compareTo(o2.getCarID())).collect(Collectors.toList());
        return carList;
    }

    public List<Car> getCarList() throws JAXBException, IOException {
        List<Car> carList=carsFromXML();
        carList=sortCarList(carList);
        return carList;
    }

    public void createCar(Car car) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Car> carList=carsFromXML();
        carList.add(car);
        carList=sortCarList(carList);
        carsToXML(carList);

        saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Car getCarByID(Long id) throws JAXBException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Car> carList=carsFromXML();

        Car car=carList.stream().filter(x-> id.equals(x.getCarID())).findAny().orElse(null);

        if(car == null){
            log.error(Constants.ERROR_CAR_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            return null;
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
            return car;
        }
    }

    public void deleteCarByID(Long id) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Car> carList=carsFromXML();

        try{
            Car car = carList.stream().filter(x-> id.equals(x.getCarID())).findAny().get();
            carList=carList.stream().filter(x-> !id.equals(x.getCarID())).collect(Collectors.toList());
            carsToXML(carList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e) {
            log.error(Constants.ERROR_CAR_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }

    public void updateCarByID(Car car) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();


        List<Car> carList=carsFromXML();
        try{
            car.getCarID().equals(carList.stream().filter(x -> car.getCarID().equals(x.getCarID())).findFirst().get().getCarID());
            carList=carList.stream().filter(x-> !car.getCarID().equals(x.getCarID())).collect(Collectors.toList());
            carsToXML(carList);
            carList.add(car);
            carList=sortCarList(carList);
            carsToXML(carList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            log.error(Constants.ERROR_CAR_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }

    //Order methods

    private void ordersToXML(List<Order> clList) throws JAXBException, IOException {
        try{
            initReader(Constants.ORDER);
            XMLList orderList=new XMLList();
            orderList.setOrders(clList);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(orderList,initFile(Constants.ORDER));
            closeReader();
        } catch(JAXBException e) {
            log.error(e);
            log.error(Constants.ERROR_FILE);
        }
    }

    private List<Order> ordersFromXML() throws JAXBException {
        try{
            XMLList orderList=(XMLList) unmarshaller.unmarshal(initFile(Constants.ORDER));
            List<Order> clList=orderList.getOrders();
            return clList;
        } catch (Exception e){
            return new ArrayList<Order>();
        }
    }

    private List<Order> sortOrderList(List<Order> orderList) throws IOException {
        orderList=orderList.stream().sorted((o1, o2)->o1.getOrderID().compareTo(o2.getOrderID())).collect(Collectors.toList());
        return orderList;
    }

    public List<Order> getOrderList() throws JAXBException, IOException {
        List<Order> orderList=ordersFromXML();
        orderList=sortOrderList(orderList);
        return orderList;
    }

    public void createOrder(Order order) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Order> orderList=ordersFromXML();
        orderList.add(order);
        orderList=sortOrderList(orderList);
        ordersToXML(orderList);

        saveToLog(mongoDBDataProvider.initHistoryContentTrue(order, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Order getOrderByID(Long id) throws JAXBException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Order> orderList=ordersFromXML();

        Order order=orderList.stream().filter(x-> id.equals(x.getOrderID())).findAny().orElse(null);

        if(order == null){
            log.error(Constants.ERROR_ORDER_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            return null;
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(order,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
            return order;
        }
    }

    public void deleteOrderByID(Long id) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Order> orderList=ordersFromXML();

        try{
            Order order = orderList.stream().filter(x-> id.equals(x.getOrderID())).findAny().get();
            orderList=orderList.stream().filter(x-> !id.equals(x.getOrderID())).collect(Collectors.toList());
            ordersToXML(orderList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(order,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e) {
            log.error(Constants.ERROR_ORDER_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }

    public void updateOrderByID(Order order) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();


        List<Order> orderList=ordersFromXML();
        try{
            order.getOrderID().equals(orderList.stream().filter(x -> order.getOrderID().equals(x.getOrderID())).findFirst().get().getOrderID());
            orderList=orderList.stream().filter(x-> !order.getOrderID().equals(x.getOrderID())).collect(Collectors.toList());
            ordersToXML(orderList);
            orderList.add(order);
            orderList=sortOrderList(orderList);
            ordersToXML(orderList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(order,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            log.error(Constants.ERROR_ORDER_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }

    //Employee methods

    private void employeesToXML(List<Employee> clList) throws JAXBException, IOException {
        try{
            initReader(Constants.CAR);
            XMLList employeeList=new XMLList();
            employeeList.setEmployees(clList);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(employeeList,initFile(Constants.CAR));
            closeReader();
        } catch(JAXBException e) {
            log.error(e);
            log.error(Constants.ERROR_FILE);
        }
    }

    private List<Employee> employeesFromXML() throws JAXBException {
        try{
            XMLList employeeList=(XMLList) unmarshaller.unmarshal(initFile(Constants.CAR));
            List<Employee> clList=employeeList.getEmployees();
            return clList;
        } catch (Exception e){
            return new ArrayList<Employee>();
        }
    }

    private List<Employee> sortEmployeeList(List<Employee> employeeList) throws IOException {
        employeeList=employeeList.stream().sorted((o1, o2)->o1.getEmployeeID().compareTo(o2.getEmployeeID())).collect(Collectors.toList());
        return employeeList;
    }

    public List<Employee> getEmployeeList() throws JAXBException, IOException {
        List<Employee> employeeList=employeesFromXML();
        employeeList=sortEmployeeList(employeeList);
        return employeeList;
    }

    public void createEmployee(Employee employee) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Employee> employeeList=employeesFromXML();
        employeeList.add(employee);
        employeeList=sortEmployeeList(employeeList);
        employeesToXML(employeeList);

        saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Employee getEmployeeByID(Long id) throws JAXBException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Employee> employeeList=employeesFromXML();

        Employee employee=employeeList.stream().filter(x-> id.equals(x.getEmployeeID())).findAny().orElse(null);

        if(employee == null){
            log.error(Constants.ERROR_CLIENT_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            return null;
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
            return employee;
        }
    }

    public void deleteEmployeeByID(Long id) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<Employee> employeeList=employeesFromXML();

        try{
            Employee employee = employeeList.stream().filter(x-> id.equals(x.getEmployeeID())).findAny().get();
            employeeList=employeeList.stream().filter(x-> !id.equals(x.getEmployeeID())).collect(Collectors.toList());
            employeesToXML(employeeList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e) {
            log.error(Constants.ERROR_CLIENT_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }

    public void updateEmployeeByID(Employee employee) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();


        List<Employee> employeeList=employeesFromXML();
        try{
            employee.getEmployeeID().equals(employeeList.stream().filter(x -> employee.getEmployeeID().equals(x.getEmployeeID())).findFirst().get().getEmployeeID());
            employeeList=employeeList.stream().filter(x-> !employee.getEmployeeID().equals(x.getEmployeeID())).collect(Collectors.toList());
            employeesToXML(employeeList);
            employeeList.add(employee);
            employeeList=sortEmployeeList(employeeList);
            employeesToXML(employeeList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            log.error(Constants.ERROR_CLIENT_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }


    //ChassisPart methods

    private void chassisPartsToXML(List<ChassisPart> clList) throws JAXBException, IOException {
        try{
            initReader(Constants.CHASSIS_PART);
            XMLList chassisPartList=new XMLList();
            chassisPartList.setChassisParts(clList);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(chassisPartList,initFile(Constants.CHASSIS_PART));
            closeReader();
        } catch(JAXBException e) {
            log.error(e);
            log.error(Constants.ERROR_FILE);
        }
    }

    private List<ChassisPart> chassisPartsFromXML() throws JAXBException {
        try{
            XMLList chassisPartList=(XMLList) unmarshaller.unmarshal(initFile(Constants.CHASSIS_PART));
            List<ChassisPart> clList=chassisPartList.getChassisParts();
            return clList;
        } catch (Exception e){
            return new ArrayList<ChassisPart>();
        }
    }

    private List<ChassisPart> sortChassisPartList(List<ChassisPart> chassisPartList) throws IOException {
        chassisPartList=chassisPartList.stream().sorted((o1, o2)->o1.getPartID().compareTo(o2.getPartID())).collect(Collectors.toList());
        return chassisPartList;
    }

    public List<ChassisPart> getChassisPartList() throws JAXBException, IOException {
        List<ChassisPart> chassisPartList=chassisPartsFromXML();
        chassisPartList=sortChassisPartList(chassisPartList);
        return chassisPartList;
    }

    public void createChassisPart(ChassisPart chassisPart) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<ChassisPart> chassisPartList=chassisPartsFromXML();
        if((getChassisPartByID(chassisPart.getPartID()) == null) &&
                (getElectricityPartByID(chassisPart.getPartID()) == null) &&
                (getEnginePartByID(chassisPart.getPartID()) == null)) {
            chassisPartList.add(chassisPart);
            chassisPartList = sortChassisPartList(chassisPartList);
            chassisPartsToXML(chassisPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart,Constants.CHASSIS_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        } else {
            log.error(Constants.ERROR_ID_IS_TAKEN);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }

    }

    public ChassisPart getChassisPartByID(Long id) throws JAXBException {
        String methodName = getMethodName();
        String className = getClassName();

        List<ChassisPart> chassisPartList=chassisPartsFromXML();

        ChassisPart chassisPart=chassisPartList.stream().filter(x-> id.equals(x.getPartID())).findAny().orElse(null);
        if(chassisPart == null){
            log.error(Constants.ERROR_CHP_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            return null;
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart,Constants.CHASSIS_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
            return chassisPart;
        }
    }

    public void deleteChassisPartByID(Long id) throws JAXBException, IOException {
        String methodName = getMethodName();
        String className = getClassName();

        List<ChassisPart> chassisPartList=chassisPartsFromXML();
        chassisPartList=chassisPartList.stream().filter(x-> !id.equals(x.getPartID())).collect(Collectors.toList());
        chassisPartsToXML(chassisPartList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(getChassisPartByID(id),Constants.CHASSIS_PART,className,methodName),
                Constants.MONGODB_TEST_SERVER);
    }

    public void updateChassisPartByID(ChassisPart chassisPart) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<ChassisPart> chassisPartList=chassisPartsFromXML();
        try{
            chassisPart.getPartID().equals(chassisPartList.stream().filter(x -> chassisPart.getPartID().equals(x.getPartID())).findFirst().get().getPartID());
            chassisPartList=chassisPartList.stream().filter(x-> !chassisPart.getPartID().equals(x.getPartID())).collect(Collectors.toList());
            chassisPartsToXML(chassisPartList);
            chassisPartList.add(chassisPart);
            chassisPartList=sortChassisPartList(chassisPartList);
            chassisPartsToXML(chassisPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart,Constants.CHASSIS_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.error(Constants.ERROR_CHP_NOT_FOUND);
        }
    }

    //ElectricityPart methods

    private void electricityPartsToXML(List<ElectricityPart> clList) throws JAXBException, IOException {
        try{
            initReader(Constants.ELECTRICITY_PART);
            XMLList electricityPartList=new XMLList();
            electricityPartList.setElectricityParts(clList);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(electricityPartList,initFile(Constants.ELECTRICITY_PART));
            closeReader();
        } catch(JAXBException e) {
            log.error(e);
            log.error(Constants.ERROR_FILE);
        }
    }

    private List<ElectricityPart> electricityPartsFromXML() throws JAXBException {
        try{
            XMLList electricityPartList=(XMLList) unmarshaller.unmarshal(initFile(Constants.ELECTRICITY_PART));
            List<ElectricityPart> clList=electricityPartList.getElectricityParts();
            return clList;
        } catch (Exception e){
            return new ArrayList<ElectricityPart>();
        }
    }

    private List<ElectricityPart> sortElectricityPartList(List<ElectricityPart> electricityPartList) throws IOException {
        electricityPartList=electricityPartList.stream().sorted((o1, o2)->o1.getPartID().compareTo(o2.getPartID())).collect(Collectors.toList());
        return electricityPartList;
    }

    public List<ElectricityPart> getElectricityPartList() throws JAXBException, IOException {
        List<ElectricityPart> electricityPartList=electricityPartsFromXML();
        electricityPartList=sortElectricityPartList(electricityPartList);
        return electricityPartList;
    }

    public void createElectricityPart(ElectricityPart electricityPart) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<ElectricityPart> electricityPartList=electricityPartsFromXML();
        if((getChassisPartByID(electricityPart.getPartID()).getPartID().equals(electricityPart.getPartID())) ||
                (getElectricityPartByID(electricityPart.getPartID()).getPartID().equals(electricityPart.getPartID())) ||
                (getEnginePartByID(electricityPart.getPartID()).getPartID().equals(electricityPart.getPartID()))) {
            electricityPartList.add(electricityPart);
            electricityPartList=sortElectricityPartList(electricityPartList);
            electricityPartsToXML(electricityPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart,Constants.ELECTRICITY_PART,className,methodName),
                    Constants.MONGODB_TEST_SERVER);
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.error(Constants.ERROR_ID_IS_TAKEN);
        }

    }

    public ElectricityPart getElectricityPartByID(Long id) throws JAXBException {

        String methodName = getMethodName();
        String className = getClassName();

        List<ElectricityPart> electricityPartList=electricityPartsFromXML();

        ElectricityPart electricityPart=electricityPartList.stream().filter(x-> id.equals(x.getPartID())).findAny().orElse(null);
        if(electricityPart == null){
            log.error(Constants.ERROR_ELP_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            return null;
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart,Constants.ELECTRICITY_PART,className,methodName),
                    Constants.MONGODB_TEST_SERVER);
            return electricityPart;
        }
    }

    public void deleteElectricityPartByID(Long id) throws JAXBException, IOException {
        String methodName = getMethodName();
        String className = getClassName();

        List<ElectricityPart> electricityPartList=electricityPartsFromXML();
        electricityPartList=electricityPartList.stream().filter(x-> !id.equals(x.getPartID())).collect(Collectors.toList());
        electricityPartsToXML(electricityPartList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(getElectricityPartByID(id),Constants.ELECTRICITY_PART,className,methodName),
                Constants.MONGODB_TEST_SERVER);
    }

    public void updateElectricityPartByID(ElectricityPart electricityPart) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<ElectricityPart> electricityPartList=electricityPartsFromXML();
        try{
            electricityPart.getPartID().equals(electricityPartList.stream().filter(x -> electricityPart.getPartID().equals(x.getPartID())).findFirst().get().getPartID());
            electricityPartList=electricityPartList.stream().filter(x-> !electricityPart.getPartID().equals(x.getPartID())).collect(Collectors.toList());
            electricityPartsToXML(electricityPartList);
            electricityPartList.add(electricityPart);
            electricityPartList=sortElectricityPartList(electricityPartList);
            electricityPartsToXML(electricityPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart,Constants.ELECTRICITY_PART,className,methodName),
                    Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            log.error(Constants.ERROR_ELP_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }

    //EnginePart methods

    private void enginePartsToXML(List<EnginePart> clList) throws JAXBException, IOException {
        try{
            initReader(Constants.ENGINE_PART);
            XMLList enginePartList=new XMLList();
            enginePartList.setEngineParts(clList);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(enginePartList,initFile(Constants.ENGINE_PART));
            closeReader();
        } catch(JAXBException e) {
            log.error(e);
            log.error(Constants.ERROR_FILE);
        }
    }

    private List<EnginePart> enginePartsFromXML() throws JAXBException {
        try{
            XMLList enginePartList=(XMLList) unmarshaller.unmarshal(initFile(Constants.ENGINE_PART));
            List<EnginePart> clList=enginePartList.getEngineParts();
            return clList;
        } catch (Exception e){
            return new ArrayList<EnginePart>();
        }
    }

    private List<EnginePart> sortEnginePartList(List<EnginePart> enginePartList) throws IOException {
        enginePartList=enginePartList.stream().sorted((o1, o2)->o1.getPartID().compareTo(o2.getPartID())).collect(Collectors.toList());
        return enginePartList;
    }

    public List<EnginePart> getEnginePartList() throws JAXBException, IOException {
        List<EnginePart> enginePartList=enginePartsFromXML();
        enginePartList=sortEnginePartList(enginePartList);
        return enginePartList;
    }

    public void createEnginePart(EnginePart enginePart) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<EnginePart> enginePartList=enginePartsFromXML();
        if((getChassisPartByID(enginePart.getPartID()).getPartID().equals(enginePart.getPartID())) ||
                (getElectricityPartByID(enginePart.getPartID()).getPartID().equals(enginePart.getPartID())) ||
                (getEnginePartByID(enginePart.getPartID()).getPartID().equals(enginePart.getPartID()))) {
            enginePartList.add(enginePart);
            enginePartList=sortEnginePartList(enginePartList);
            enginePartsToXML(enginePartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart,Constants.ELECTRICITY_PART,className,methodName),
                    Constants.MONGODB_TEST_SERVER);
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.error(Constants.ERROR_ID_IS_TAKEN);
        }


    }

    public EnginePart getEnginePartByID(Long id) throws JAXBException {

        String methodName = getMethodName();
        String className = getClassName();

        List<EnginePart> enginePartList=enginePartsFromXML();

        EnginePart enginePart=enginePartList.stream().filter(x-> id.equals(x.getPartID())).findAny().orElse(null);
        if(enginePart == null){
            log.error(Constants.ERROR_ENP_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            return null;
        } else {
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart,Constants.ELECTRICITY_PART,className,methodName),
                    Constants.MONGODB_TEST_SERVER);
            return enginePart;
        }
    }

    public void deleteEnginePartByID(Long id) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<EnginePart> enginePartList=enginePartsFromXML();
        enginePartList=enginePartList.stream().filter(x-> !id.equals(x.getPartID())).collect(Collectors.toList());
        enginePartsToXML(enginePartList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(getEnginePartByID(id),Constants.ELECTRICITY_PART,className,methodName),
                Constants.MONGODB_TEST_SERVER);
    }

    public void updateEnginePartByID(EnginePart enginePart) throws JAXBException, IOException {

        String methodName = getMethodName();
        String className = getClassName();

        List<EnginePart> enginePartList=enginePartsFromXML();
        try{
            enginePart.getPartID().equals(enginePartList.stream().filter(x -> enginePart.getPartID().equals(x.getPartID())).findFirst().get().getPartID());
            enginePartList=enginePartList.stream().filter(x-> !enginePart.getPartID().equals(x.getPartID())).collect(Collectors.toList());
            enginePartsToXML(enginePartList);
            enginePartList.add(enginePart);
            enginePartList=sortEnginePartList(enginePartList);
            enginePartsToXML(enginePartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart,Constants.ELECTRICITY_PART,className,methodName),
                    Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.error(Constants.ERROR_ENP_NOT_FOUND);
        }
    }


    // USE CASE METHODS


    public Double calculateMarkup(Long orderID) throws JAXBException, IOException {
        try{
            Order order = getOrderByID(orderID);
            ClientType clientType = order.getClient().getClientType();
            switch (clientType){
                case INDIVIDUAL:
                    order.setTotalMarkup(calculateIndividualMarkup(order));
                    updateOrderByID(order);
                    log.info(Constants.LOG_MARKUP + order.getTotalMarkup());
                    return calculateIndividualMarkup(order);
                case COMPANY:
                    order.setTotalMarkup(calculateCompanyMarkup(order));
                    updateOrderByID(order);
                    log.info(Constants.LOG_MARKUP + order.getTotalMarkup());
                    return calculateCompanyMarkup(order);
                default:
                    order.setTotalMarkup(Constants.DOUBLE_ZERO);
                    updateOrderByID(order);
                    log.info(Constants.LOG_MARKUP + order.getTotalMarkup());
                    return order.getEmployeeSalary();
            }
        } catch (NullPointerException e){
            log.error(Constants.ERROR_MARKUP_NF);
            return null;
        }

    }

    public Double calculateIndividualMarkup(Order order){
        try {
            Double individualMarkup = order.getEmployeeSalary() * Constants.INDIVIDUAL_RATIO;
            //log.info(Constants.LOG_MARKUP + individualMarkup);
            return individualMarkup;
        } catch (NullPointerException e){
            log.error(Constants.ERROR_MARKUP_NF);
            return null;
        }

    }

    public Double calculateCompanyMarkup(Order order){
        try{
            Double companyMarkup = order.getEmployeeSalary() * Constants.COMPANY_RATIO;
            //log.info(Constants.LOG_MARKUP + companyMarkup);
            return companyMarkup;
        } catch (NullPointerException e){
            log.error(Constants.ERROR_MARKUP_NF);
            return null;
        }

    }

    public Order calculateIncome(Long orderID) throws JAXBException, IOException {
        try {
            Order order = getOrderByID(orderID);
            order.setTotalServiceIncome(calculatePartsIncome(order) + calculateEmployeeIncome(order));
            order.setTotalEmployeeIncome(order.getEmployeeSalary() - calculateEmployeeIncome(order));
            updateOrderByID(order);
            log.info(Constants.LOG_SERVICE_INCOME + order.getTotalServiceIncome() + Constants.LOG_EMPLOYEE_INCOME + order.getTotalEmployeeIncome());
            return order;
        } catch (NullPointerException e){
            log.error(Constants.ERROR_INCOME_NF);
            return null;
        }

    }

    public Double calculatePartsIncome(Order order){
        try{
            Integer enginePartsTotal = order.getEngineParts().stream().mapToInt(Part::getPrice).sum();
            Integer chassisPartsTotal = order.getChassisParts().stream().mapToInt(Part::getPrice).sum();
            Integer electricityPartsTotal = order.getElectricityParts().stream().mapToInt(Part::getPrice).sum();

            Double partsIncome = (enginePartsTotal+chassisPartsTotal+electricityPartsTotal) * Constants.PARTS_INCOME_RATIO;
            log.info(Constants.LOG_PARTS_INCOME_FS + partsIncome);

            return partsIncome;
        } catch (NullPointerException e){
            log.error(Constants.ERROR_INCOME_NF);
            return null;
        }

    }

    public Double calculateEmployeeIncome(Order order){
        try{
            Double employeeIncome = order.getEmployeeSalary() * Constants.EMPLOYEE_INCOME_RATIO;
            log.info(Constants.LOG_EMPLOYEE_INCOME_FS + employeeIncome);
            return employeeIncome;
        } catch (NullPointerException e){
            log.error(Constants.ERROR_INCOME_NF);
            return null;
        }

    }
}
