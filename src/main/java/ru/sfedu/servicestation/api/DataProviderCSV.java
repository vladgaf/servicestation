package ru.sfedu.servicestation.api;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import ru.sfedu.servicestation.beans.*;

import java.util.stream.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.utils.ConfigurationUtil;
import ru.sfedu.servicestation.utils.Constants;


public class DataProviderCSV extends AbstractDataProvider{

    public DataProviderCSV() throws IOException { }


    private static final Logger log = LogManager.getLogger(DataProviderCSV.class);

    MongoDBDataProvider mongoDBDataProvider=new MongoDBDataProvider();

    private final String PATH_TO_CSV= ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_CSV);
    private final String CSV_FILE_EXTENSION=ConfigurationUtil.getConfigurationEntry(Constants.CSV_FILE_EXTENSION);

    private CSVReader reader;
    private CSVWriter writer;

    private String getClassName(){
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }
    private String getMethodName(){
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    private void initReader(String string) throws IOException {
        initDataSource(string);
        this.reader=new CSVReader(new FileReader(PATH_TO_CSV + string + CSV_FILE_EXTENSION));
    }
    private void initWriter(String string) throws IOException {
        initDataSource(string);
        this.writer=new CSVWriter(new FileWriter(PATH_TO_CSV + string + CSV_FILE_EXTENSION));
    }

    private void close() throws IOException {
        if (reader!=null){this.reader.close();}
        if(writer!=null){this.writer.close();}
    }

    private void initDataSource(String string) throws IOException {
        string = PATH_TO_CSV + string + CSV_FILE_EXTENSION;
        File file = new File(string);
        if (!file.exists()) {
            Path dirPath = Paths.get(PATH_TO_CSV);
            Files.createDirectories(dirPath);
            file.createNewFile();
        }
    }

    //CAR METHODS

    private List<Car> sortCarList(List<Car> carList) {
        carList=carList.stream().sorted((o1, o2)->o1.getCarID().compareTo(o2.getCarID())).collect(Collectors.toList());
        return carList;
    }

    public List<Car> getCarList() throws IOException {
        this.initReader(Constants.CAR);
        CsvToBean<Car> csvToBean=new CsvToBeanBuilder<Car>(this.reader).withType(Car.class).build();
        List<Car> carlist=csvToBean.parse();
        carlist=sortCarList(carlist);
        return carlist;
    }

    private void writeCars (List<Car> cList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        this.initWriter(Constants.CAR);
        StatefulBeanToCsv statefulBeanToCSV = new StatefulBeanToCsvBuilder<Car>(this.writer).withApplyQuotesToAll(false).build();
        statefulBeanToCSV.write(cList);
        this.close();
    }

    public void createCar(Car car) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Car> carList = getCarList();
        carList.add(car);
        carList = sortCarList(carList);
        this.writeCars(carList);
        log.info("car created");
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CAR, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Car getCarByID(Long carID) throws IOException {
        List<Car> carList = getCarList();
        try{
            Optional<Car> found = Optional.empty();
            for (Car el : carList) {
                if (el.getCarID().equals(carID)) {
                    found = Optional.of(el);
                    break;
                }
            }
            Car car = found.get();
            return car;
        } catch (NoSuchElementException e){
            log.error(e);
            return null;
        }
    }

    public void updateCarByID(Car car) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Car> carList = getCarList();
        try{
            car.getCarID()
                    .equals(carList
                            .stream()
                            .filter(x -> car.getCarID().equals(x.getCarID()))
                            .findFirst()
                            .get()
                            .getCarID());
            carList = carList.stream().filter(x-> !car.getCarID().equals(x.getCarID())).collect(Collectors.toList());
            writeCars(carList);
            carList.add(car);
            carList = sortCarList(carList);
            writeCars(carList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car,Constants.CAR,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_CAR_NOT_FOUND);
        }
    }


    public void deleteCarByID(Long id) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Car> carList = getCarList();
        try {
            Car car = carList.stream().filter(x-> id.equals(x.getCarID())).findAny().get();
            carList = carList.stream().filter(x-> !id.equals(x.getCarID())).collect(Collectors.toList());
            this.writeCars(carList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car,Constants.CAR,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        catch (NoSuchElementException e){
            log.info(Constants.ERROR_CAR_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }


    //CLIENT METHODS

    private List<Client> sortClientList(List<Client> clientList) {
        clientList=clientList.stream().sorted((o1, o2)->o1.getClientID().compareTo(o2.getClientID())).collect(Collectors.toList());
        return clientList;
    }

    public List<Client> getClientList() throws IOException {
        this.initReader(Constants.CLIENT);
        CsvToBean<Client> csvToBean=new CsvToBeanBuilder<Client>(this.reader).withType(Client.class).build();
        List<Client> clientlist=csvToBean.parse();
        clientlist=sortClientList(clientlist);
        return clientlist;
    }

    private void writeClients (List<Client> cList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        this.initWriter(Constants.CLIENT);
        StatefulBeanToCsv statefulBeanToCSV = new StatefulBeanToCsvBuilder<Client>(this.writer).withApplyQuotesToAll(false).build();
        statefulBeanToCSV.write(cList);
        this.close();
    }

    public void createClient(Client client) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Client> clientList = getClientList();
        clientList.add(client);
        clientList = sortClientList(clientList);
        this.writeClients(clientList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(client, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Client getClientByID(Long clientID) throws IOException {
        List<Client> clientList = getClientList();
        try{
            Optional<Client> found = Optional.empty();
            for (Client el : clientList) {
                if (el.getClientID().equals(clientID)) {
                    found = Optional.of(el);
                    break;
                }
            }
            Client client = found.get();
            return client;
        } catch (NoSuchElementException e){
            log.error(e);
            return null;
        }
    }

    public void updateClientByID(Client client) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Client> clientList = getClientList();
        try{
            client.getClientID()
                    .equals(clientList
                            .stream()
                            .filter(x -> client.getClientID().equals(x.getClientID()))
                            .findFirst()
                            .get()
                            .getClientID());
            clientList = clientList.stream().filter(x-> !client.getClientID().equals(x.getClientID())).collect(Collectors.toList());
            writeClients(clientList);
            clientList.add(client);
            clientList = sortClientList(clientList);
            writeClients(clientList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(client,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_CLIENT_NOT_FOUND);
        }
    }

    public void deleteClientByID(Long id) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Client> clientList = getClientList();
        try {
            Client client = clientList.stream().filter(x-> id.equals(x.getClientID())).findAny().get();
            clientList = clientList.stream().filter(x-> !id.equals(x.getClientID())).collect(Collectors.toList());
            this.writeClients(clientList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(client,Constants.CLIENT,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        catch (NoSuchElementException e){
            log.info(Constants.ERROR_CLIENT_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }

    //EMPLOYEE METHODS

    private List<Employee> sortEmployeeList(List<Employee> clientList) {
        clientList=clientList.stream().sorted((o1, o2)->o1.getEmployeeID().compareTo(o2.getEmployeeID())).collect(Collectors.toList());
        return clientList;
    }

    public List<Employee> getEmployeeList() throws IOException {
        this.initReader(Constants.EMPLOYEE);
        CsvToBean<Employee> csvToBean=new CsvToBeanBuilder<Employee>(this.reader).withType(Employee.class).build();
        List<Employee> clientlist=csvToBean.parse();
        clientlist=sortEmployeeList(clientlist);
        return clientlist;
    }

    private void writeEmployees (List<Employee> cList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        this.initWriter(Constants.EMPLOYEE);
        StatefulBeanToCsv statefulBeanToCSV = new StatefulBeanToCsvBuilder<Employee>(this.writer).withApplyQuotesToAll(false).build();
        statefulBeanToCSV.write(cList);
        this.close();
    }

    public void createEmployee(Employee employee) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Employee> employeeList = getEmployeeList();
        employeeList.add(employee);
        employeeList = sortEmployeeList(employeeList);
        this.writeEmployees(employeeList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.EMPLOYEE, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Employee getEmployeeByID(Long employeeID) throws IOException {
        List<Employee> employeeList = getEmployeeList();
        try{
            Optional<Employee> found = Optional.empty();
            for (Employee el : employeeList) {
                if (el.getEmployeeID().equals(employeeID)) {
                    found = Optional.of(el);
                    break;
                }
            }
            Employee employee = found.get();
            return employee;
        } catch (NoSuchElementException e){
            log.error(e);
            return null;
        }
    }

    public void updateEmployeeByID(Employee employee) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Employee> employeeList = getEmployeeList();
        try{
            employee.getEmployeeID()
                    .equals(employeeList
                            .stream()
                            .filter(x -> employee.getEmployeeID().equals(x.getEmployeeID()))
                            .findFirst()
                            .get()
                            .getEmployeeID());
            employeeList = employeeList.stream().filter(x-> !employee.getEmployeeID().equals(x.getEmployeeID())).collect(Collectors.toList());
            writeEmployees(employeeList);
            employeeList.add(employee);
            employeeList = sortEmployeeList(employeeList);
            writeEmployees(employeeList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee,Constants.EMPLOYEE,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_EMPLOYEE_NOT_FOUND);
        }
    }

    public void deleteEmployeeByID(Long id) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Employee> employeeList = getEmployeeList();
        try {
            Employee employee = employeeList.stream().filter(x-> id.equals(x.getEmployeeID())).findAny().get();
            employeeList = employeeList.stream().filter(x-> !id.equals(x.getEmployeeID())).collect(Collectors.toList());
            this.writeEmployees(employeeList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee,Constants.EMPLOYEE,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        catch (NoSuchElementException e){
            log.info(Constants.ERROR_EMPLOYEE_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }


    //CHASSISPART METHODS

    private List<ChassisPart> sortChassisPartList(List<ChassisPart> clientList) {
        clientList=clientList.stream().sorted((o1, o2)->o1.getPartID().compareTo(o2.getPartID())).collect(Collectors.toList());
        return clientList;
    }

    public List<ChassisPart> getChassisPartList() throws IOException {
        this.initReader(Constants.CHASSIS_PART);
        CsvToBean<ChassisPart> csvToBean=new CsvToBeanBuilder<ChassisPart>(this.reader).withType(ChassisPart.class).build();
        List<ChassisPart> clientlist=csvToBean.parse();
        clientlist=sortChassisPartList(clientlist);
        return clientlist;
    }

    private void writeChassisParts (List<ChassisPart> cList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        this.initWriter(Constants.CHASSIS_PART);
        StatefulBeanToCsv statefulBeanToCSV = new StatefulBeanToCsvBuilder<ChassisPart>(this.writer).withApplyQuotesToAll(false).build();
        statefulBeanToCSV.write(cList);
        this.close();
    }

    public void createChassisPart(ChassisPart chassisPart) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<ChassisPart> chassisPartList = getChassisPartList();
        chassisPartList.add(chassisPart);
        chassisPartList = sortChassisPartList(chassisPartList);
        this.writeChassisParts(chassisPartList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart, Constants.CHASSIS_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public ChassisPart getChassisPartByID(Long chassisPartID) throws IOException {
        List<ChassisPart> chassisPartList = getChassisPartList();
        try{
            Optional<ChassisPart> found = Optional.empty();
            for (ChassisPart el : chassisPartList) {
                if (el.getPartID().equals(chassisPartID)) {
                    found = Optional.of(el);
                    break;
                }
            }
            ChassisPart chassisPart = found.get();
            return chassisPart;
        } catch (NoSuchElementException e){
            log.error(e);
            return null;
        }
    }

    public void updateChassisPartByID(ChassisPart chassisPart) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<ChassisPart> chassisPartList = getChassisPartList();
        try{
            chassisPart.getPartID()
                    .equals(chassisPartList
                            .stream()
                            .filter(x -> chassisPart.getPartID().equals(x.getPartID()))
                            .findFirst()
                            .get()
                            .getPartID());
            chassisPartList = chassisPartList.stream().filter(x-> !chassisPart.getPartID().equals(x.getPartID())).collect(Collectors.toList());
            writeChassisParts(chassisPartList);
            chassisPartList.add(chassisPart);
            chassisPartList = sortChassisPartList(chassisPartList);
            writeChassisParts(chassisPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart,Constants.CHASSIS_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_CHP_NOT_FOUND);
        }
    }

    public void deleteChassisPartByID(Long id) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<ChassisPart> chassisPartList = getChassisPartList();
        try {
            ChassisPart chassisPart = chassisPartList.stream().filter(x-> id.equals(x.getPartID())).findAny().get();
            chassisPartList = chassisPartList.stream().filter(x-> !id.equals(x.getPartID())).collect(Collectors.toList());
            this.writeChassisParts(chassisPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart,Constants.CHASSIS_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        catch (NoSuchElementException e){
            log.info(Constants.ERROR_CHP_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }


    //ENGINEPART METHODS

    private List<EnginePart> sortEnginePartList(List<EnginePart> clientList) {
        clientList=clientList.stream().sorted((o1, o2)->o1.getPartID().compareTo(o2.getPartID())).collect(Collectors.toList());
        return clientList;
    }

    public List<EnginePart> getEnginePartList() throws IOException {
        this.initReader(Constants.ENGINE_PART);
        CsvToBean<EnginePart> csvToBean=new CsvToBeanBuilder<EnginePart>(this.reader).withType(EnginePart.class).build();
        List<EnginePart> clientlist=csvToBean.parse();
        clientlist=sortEnginePartList(clientlist);
        return clientlist;
    }

    private void writeEngineParts (List<EnginePart> cList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        this.initWriter(Constants.ENGINE_PART);
        StatefulBeanToCsv statefulBeanToCSV = new StatefulBeanToCsvBuilder<EnginePart>(this.writer).withApplyQuotesToAll(false).build();
        statefulBeanToCSV.write(cList);
        this.close();
    }

    public void createEnginePart(EnginePart enginePart) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<EnginePart> enginePartList = getEnginePartList();
        enginePartList.add(enginePart);
        enginePartList = sortEnginePartList(enginePartList);
        this.writeEngineParts(enginePartList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart, Constants.ENGINE_PART, className, methodName),
                Constants.MONGODB_TEST_SERVER);

    }

    public EnginePart getEnginePartByID(Long enginePartID) throws IOException {
        List<EnginePart> enginePartList = getEnginePartList();
        try{
            Optional<EnginePart> found = Optional.empty();
            for (EnginePart el : enginePartList) {
                if (el.getPartID().equals(enginePartID)) {
                    found = Optional.of(el);
                    break;
                }
            }
            EnginePart enginePart = found.get();
            log.info(enginePart.toString());
            return enginePart;
        } catch (NoSuchElementException e){
            log.error(e);
            return null;
        }
    }

    public void updateEnginePartByID(EnginePart enginePart) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<EnginePart> enginePartList = getEnginePartList();
        try{
            enginePart.getPartID()
                    .equals(enginePartList
                            .stream()
                            .filter(x -> enginePart.getPartID().equals(x.getPartID()))
                            .findFirst()
                            .get()
                            .getPartID());
            enginePartList = enginePartList.stream().filter(x-> !enginePart.getPartID().equals(x.getPartID())).collect(Collectors.toList());
            writeEngineParts(enginePartList);
            enginePartList.add(enginePart);
            enginePartList = sortEnginePartList(enginePartList);
            writeEngineParts(enginePartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart,Constants.ENGINE_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_ENP_NOT_FOUND);
        }
    }

    public void deleteEnginePartByID(Long id) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<EnginePart> enginePartList = getEnginePartList();
        try {
            EnginePart enginePart = enginePartList.stream().filter(x-> id.equals(x.getPartID())).findAny().get();
            enginePartList = enginePartList.stream().filter(x-> !id.equals(x.getPartID())).collect(Collectors.toList());
            this.writeEngineParts(enginePartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart,Constants.ENGINE_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        catch (NoSuchElementException e){
            log.info(Constants.ERROR_ENP_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }

    //ELECTRICITYPART METHODS

    private List<ElectricityPart> sortElectricityPartList(List<ElectricityPart> clientList) {
        clientList=clientList.stream().sorted((o1, o2)->o1.getPartID().compareTo(o2.getPartID())).collect(Collectors.toList());
        return clientList;
    }

    public List<ElectricityPart> getElectricityPartList() throws IOException {
        this.initReader(Constants.ELECTRICITY_PART);
        CsvToBean<ElectricityPart> csvToBean=new CsvToBeanBuilder<ElectricityPart>(this.reader).withType(ElectricityPart.class).build();
        List<ElectricityPart> clientlist=csvToBean.parse();
        clientlist=sortElectricityPartList(clientlist);
        return clientlist;
    }

    private void writeElectricityParts (List<ElectricityPart> cList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        this.initWriter(Constants.ELECTRICITY_PART);
        StatefulBeanToCsv statefulBeanToCSV = new StatefulBeanToCsvBuilder<ElectricityPart>(this.writer).withApplyQuotesToAll(false).build();
        statefulBeanToCSV.write(cList);
        this.close();
    }

    public void createElectricityPart(ElectricityPart electricityPart) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<ElectricityPart> electricityPartList = getElectricityPartList();
        electricityPartList.add(electricityPart);
        electricityPartList = sortElectricityPartList(electricityPartList);
        this.writeElectricityParts(electricityPartList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart, Constants.ELECTRICITY_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public ElectricityPart getElectricityPartByID(Long electricityPartID) throws IOException {
        List<ElectricityPart> electricityPartList = getElectricityPartList();
        try{
            Optional<ElectricityPart> found = Optional.empty();
            for (ElectricityPart el : electricityPartList) {
                if (el.getPartID().equals(electricityPartID)) {
                    found = Optional.of(el);
                    break;
                }
            }
            ElectricityPart electricityPart = found.get();
            return electricityPart;
        } catch (NoSuchElementException e){
            log.error(e);
            return null;
        }
    }

    public void updateElectricityPartByID(ElectricityPart electricityPart) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<ElectricityPart> electricityPartList = getElectricityPartList();
        try{
            electricityPart.getPartID()
                    .equals(electricityPartList
                            .stream()
                            .filter(x -> electricityPart.getPartID().equals(x.getPartID()))
                            .findFirst()
                            .get()
                            .getPartID());
            electricityPartList = electricityPartList.stream().filter(x-> !electricityPart.getPartID().equals(x.getPartID())).collect(Collectors.toList());
            writeElectricityParts(electricityPartList);
            electricityPartList.add(electricityPart);
            electricityPartList = sortElectricityPartList(electricityPartList);
            writeElectricityParts(electricityPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart,Constants.ELECTRICITY_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_ELP_NOT_FOUND);
        }
    }

    public void deleteElectricityPartByID(Long id) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<ElectricityPart> electricityPartList = getElectricityPartList();
        try {
            ElectricityPart electricityPart = electricityPartList.stream().filter(x-> id.equals(x.getPartID())).findAny().get();
            electricityPartList = electricityPartList.stream().filter(x-> !id.equals(x.getPartID())).collect(Collectors.toList());
            this.writeElectricityParts(electricityPartList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart,Constants.ELECTRICITY_PART,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        catch (NoSuchElementException e){
            log.info(Constants.ERROR_ELP_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }

    //ORDER METHODS

    private List<Order> sortOrderList(List<Order> orderList) {
        //orderList=orderList.stream().sorted((o1, o2)->o1.getOrderID().compareTo(o2.getOrderID())).collect(Collectors.toList());
        return orderList;
    }

    public List<Order> getOrderList() throws IOException {
        this.initReader(Constants.ORDER);
        CsvToBean<Order> csvToBean=new CsvToBeanBuilder<Order>(this.reader).withType(Order.class).build();
        List<Order> orderList=csvToBean.parse();
        orderList=sortOrderList(orderList);
        return orderList;
    }

    private void writeOrders (List<Order> cList) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        this.initWriter(Constants.ORDER);
        StatefulBeanToCsv statefulBeanToCSV = new StatefulBeanToCsvBuilder<Order>(this.writer).withApplyQuotesToAll(false).build();
        statefulBeanToCSV.write(cList);
        this.close();
    }

    public void createOrder(Order order) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Order> orderList = getOrderList();
        orderList.add(order);
        orderList = sortOrderList(orderList);
        this.writeOrders(orderList);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(order, Constants.ORDER, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Order getOrderByID(Long orderID) throws IOException {
        List<Order> orderList = getOrderList();
        try{
            Optional<Order> found = Optional.empty();
            for (Order el : orderList) {
                if (el.getOrderID().equals(orderID)) {
                    found = Optional.of(el);
                    break;
                }
            }
            Order order = found.get();
            return order;
        } catch (NoSuchElementException e){
            log.error(e);
            return null;
        }
    }

    public void updateOrderByID(Order order) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Order> orderList = getOrderList();
        try{
            order.getOrderID()
                    .equals(orderList
                            .stream()
                            .filter(x -> order.getOrderID().equals(x.getOrderID()))
                            .findFirst()
                            .get()
                            .getOrderID());
            orderList = orderList.stream().filter(x-> !order.getOrderID().equals(x.getOrderID())).collect(Collectors.toList());
            writeOrders(orderList);
            orderList.add(order);
            orderList = sortOrderList(orderList);
            writeOrders(orderList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(order,Constants.ORDER,className,methodName),Constants.MONGODB_TEST_SERVER);
        } catch (NoSuchElementException e){
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
            log.info(Constants.ERROR_ORDER_NOT_FOUND);
        }
    }

    public void deleteOrderByID(Long id) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String methodName = getMethodName();
        String className = getClassName();
        List<Order> orderList = getOrderList();
        try {
            Order order = orderList.stream().filter(x-> id.equals(x.getOrderID())).findAny().get();
            orderList = orderList.stream().filter(x-> !id.equals(x.getOrderID())).collect(Collectors.toList());
            this.writeOrders(orderList);
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(order,Constants.ORDER,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
        catch (NoSuchElementException e){
            log.info(Constants.ERROR_ORDER_NOT_FOUND);
            saveToLog(mongoDBDataProvider.initHistoryContentFalse(Constants.NULL,className,methodName),Constants.MONGODB_TEST_SERVER);
        }
    }
}
