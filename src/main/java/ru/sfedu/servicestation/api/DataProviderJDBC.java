package ru.sfedu.servicestation.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import ru.sfedu.servicestation.beans.*;
import ru.sfedu.servicestation.beans.Employee;
import ru.sfedu.servicestation.utils.ConfigurationUtil;
import ru.sfedu.servicestation.utils.Constants;
import ru.sfedu.servicestation.utils.JDBCConnect;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataProviderJDBC extends AbstractDataProvider {

    private static final Logger log = LogManager.getLogger(DataProviderJDBC.class);
    MongoDBDataProvider mongoDBDataProvider=new MongoDBDataProvider();

    public Connection connection=null;
    public Statement statement=null;
    private final String PATH_TO_SQL= ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_SQL);

    public DataProviderJDBC() throws IOException {
    }

    private String getClassName(){
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }
    private String getMethodName(){
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public void initConnection() throws SQLException {
        connection = JDBCConnect.getConnection();
        statement = connection.createStatement();
    }
    public void closeConnection() throws SQLException {
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
        log.info(Constants.JDBC_TABLES_DPOPPED);
    }


    //RESULTSET CONVERTERS

    public List<Order> getOrderListFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        List<Order> orderList = new ArrayList<>();
        while (resultSet.next()){
            Order order=new Order();
            order.setOrderID(resultSet.getLong(1));
            order.setEmployeeSalary(resultSet.getDouble(2));
            order.setTotalServiceIncome(resultSet.getDouble(3));
            order.setTotalEmployeeIncome(resultSet.getDouble(4));
            order.setTotalMarkup(resultSet.getDouble(5));
            order.setEngineParts(getOrderEnginePartsByID(resultSet.getLong(1)));
            order.setChassisParts(getOrderChassisPartsByID(resultSet.getLong(1)));
            order.setElectricityParts(getOrderElectricityPartsByID(resultSet.getLong(1)));
            order.setClient(getClientByID(resultSet.getLong(6)));
            order.setEmployee(getEmployeeByID(resultSet.getLong(7)));
        }
        return orderList;
    }

    public List<ChassisPart> getChassisPartListFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        List<ChassisPart> chassisPartList = new ArrayList<>();
        while (resultSet.next()){
            ChassisPart chassisPart = new ChassisPart();
            chassisPart.setPartID(resultSet.getLong(1));
            chassisPart.setName(resultSet.getString(2));
            chassisPart.setPrice(resultSet.getInt(3));
            chassisPart.setAvailability(resultSet.getBoolean(4));
            chassisPart.setCondition(resultSet.getInt(5));
            chassisPart.setSide(resultSet.getString(6));
            chassisPart.setChassisType(resultSet.getString(7));
            chassisPartList.add(chassisPart);
        }

        return chassisPartList;
    }

    public List<EnginePart> getEnginePartListFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        List<EnginePart> enginePartList = new ArrayList<>();
        while (resultSet.next()){
            EnginePart enginePart = new EnginePart();
            enginePart.setPartID(resultSet.getLong(1));
            enginePart.setName(resultSet.getString(2));
            enginePart.setPrice(resultSet.getInt(3));
            enginePart.setAvailability(resultSet.getBoolean(4));
            enginePart.setCondition(resultSet.getInt(5));
            enginePart.setFuel(resultSet.getString(6));
            enginePart.setSerialNumber(resultSet.getInt(7));
            enginePart.setVolume(resultSet.getFloat(8));
            enginePartList.add(enginePart);
        }

        return enginePartList;
    }

    public List<ElectricityPart> getElectricityPartListFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        List<ElectricityPart> enginePartList = new ArrayList<>();
        while (resultSet.next()){
            ElectricityPart enginePart = new ElectricityPart();
            enginePart.setPartID(resultSet.getLong(1));
            enginePart.setName(resultSet.getString(2));
            enginePart.setPrice(resultSet.getInt(3));
            enginePart.setAvailability(resultSet.getBoolean(4));
            enginePart.setEngineVolume(resultSet.getFloat(5));
            enginePart.setPower(resultSet.getFloat(6));
            enginePartList.add(enginePart);
        }
        return enginePartList;
    }

    public List<Client> getClientListFromResultSet(ResultSet resultSet) throws SQLException, IOException {
        List<Client> clientList = new ArrayList<>();
        while (resultSet.next()){
            Client client=new Client();
            client.setClientID(resultSet.getLong(1));
            client.setName(resultSet.getString(2));
            client.setClientType(ClientType.valueOf(resultSet.getString(3)));
            client.setCar(getCarByID(resultSet.getLong(4)));
            clientList.add(client);
        }
        return clientList;
    }

    //LISTS CONVERTERS

    public List<Order> getOrderList() throws SQLException, IOException {
        initDataSource();
        initConnection();
        ResultSet rs = statement.executeQuery(Constants.SELECT_ORDER);
        List<Order> orderList=getOrderListFromResultSet(rs);
        closeConnection();
        return orderList;
    }

    public List<ChassisPart> getChassisPartList() throws SQLException, IOException {
        initDataSource();
        initConnection();
        ResultSet rs = statement.executeQuery(Constants.SELECT_CHASSIS_PART);
        List<ChassisPart> chassisPartList = getChassisPartListFromResultSet(rs);
        closeConnection();
        return chassisPartList;
    }

    public List<EnginePart> getEnginePartList() throws SQLException, IOException {
        initDataSource();
        initConnection();
        ResultSet rs = statement.executeQuery(Constants.SELECT_ENGINE_PART);
        List<EnginePart> enginePartList = getEnginePartListFromResultSet(rs);
        closeConnection();
        return enginePartList;
    }

    public List<ElectricityPart> getElectricityPartList() throws SQLException, IOException {
        initDataSource();
        initConnection();
        ResultSet rs = statement.executeQuery(Constants.SELECT_ELECTRICITY_PART);
        List<ElectricityPart> electricityPartList = getElectricityPartListFromResultSet(rs);
        closeConnection();
        return electricityPartList;
    }

    public List<Client> getClientList() throws SQLException, IOException {
        initDataSource();
        initConnection();
        ResultSet rs = statement.executeQuery(Constants.SELECT_CLIENT);
        List<Client> clientList = getClientListFromResultSet(rs);
        closeConnection();
        return clientList;
    }



    //CRUD

    //ORDER METHODS

    public void insertOrder(Order order) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        try {
            insertClient(order.getClient());
            insertEmployee(order.getEmployee());
            statement.executeUpdate(String.format(Constants.CREATE_ORDER,
                    order.getOrderID(),
                    order.getEmployeeSalary(),
                    order.getTotalServiceIncome(),
                    order.getTotalEmployeeIncome(),
                    order.getTotalMarkup(),
                    order.getClient().getClientID(),
                    order.getEmployee().getEmployeeID()
            ));
            insertOrderChassisParts(order.getChassisParts(), order.getOrderID());
            insertOrderEngineParts(order.getEngineParts(), order.getOrderID());
            insertOrderElectricityParts(order.getElectricityParts(), order.getOrderID());
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(order, Constants.ORDER, className, methodName), Constants.MONGODB_TEST_SERVER);
        } catch (JdbcSQLIntegrityConstraintViolationException e) {

        } finally {
            closeConnection();
        }
    }

    public Order getOrderByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_ORDER_BY_ID,id));
        Order order=new Order();
        if (resultSet.next()){
            order.setOrderID(resultSet.getLong(1));
            order.setEmployeeSalary(resultSet.getDouble(2));
            order.setTotalServiceIncome(resultSet.getDouble(3));
            order.setTotalEmployeeIncome(resultSet.getDouble(4));
            order.setTotalMarkup(resultSet.getDouble(5));
            order.setEngineParts(getOrderEnginePartsByID(resultSet.getLong(1)));
            order.setChassisParts(getOrderChassisPartsByID(resultSet.getLong(1)));
            order.setElectricityParts(getOrderElectricityPartsByID(resultSet.getLong(1)));
            order.setClient(getClientByID(resultSet.getLong(6)));
            order.setEmployee(getEmployeeByID(resultSet.getLong(7)));
        }
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(order, Constants.ORDER, className, methodName), Constants.MONGODB_TEST_SERVER);
        log.debug(order);
        closeConnection();
        return order;
    }

    public void updateOrder(Order order) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.UPDATE_ORDER,
                order.getOrderID(),
                order.getEmployeeSalary(),
                order.getTotalServiceIncome(),
                order.getTotalEmployeeIncome(),
                order.getTotalMarkup(),
                order.getOrderID()
        ));
        updateEmployee(order.getEmployee());
        updateClient(order.getClient());
        deleteOrderEngineParts(order.getEngineParts(), order.getOrderID());
        deleteOrderChassisParts(order.getChassisParts(), order.getOrderID());
        deleteOrderElectricityParts(order.getElectricityParts(), order.getOrderID());
        insertOrderEngineParts(order.getEngineParts(), order.getOrderID());
        insertOrderChassisParts(order.getChassisParts(), order.getOrderID());
        insertOrderElectricityParts(order.getElectricityParts(), order.getOrderID());
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(order, Constants.ORDER, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    public void deleteOrder(Order order) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.DELETE_ORDER, order.getOrderID()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(order, Constants.ORDER, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    //CHASSISPART METHODS

    public void insertChassisPart(ChassisPart chassisPart) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        try {
            log.info(String.format(Constants.CREATE_CHASSIS_PART,
                    chassisPart.getPartID(),
                    chassisPart.getName(),
                    chassisPart.getPrice(),
                    chassisPart.getAvailability(),
                    chassisPart.getCondition(),
                    chassisPart.getSide(),
                    chassisPart.getChassisType()
            ));
            statement.executeUpdate(String.format(Constants.CREATE_CHASSIS_PART,
                    chassisPart.getPartID(),
                    chassisPart.getName(),
                    chassisPart.getPrice(),
                    chassisPart.getAvailability(),
                    chassisPart.getCondition(),
                    chassisPart.getSide(),
                    chassisPart.getChassisType()
            ));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart, Constants.CHASSIS_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
        } catch (JdbcSQLIntegrityConstraintViolationException e){

        } finally {

        }
    }

    public ChassisPart getChassisPartByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_CHASSIS_PART_BY_ID,id));
        ChassisPart chassisPart = new ChassisPart();
        if(resultSet.next()){
            chassisPart.setPartID(resultSet.getLong(1));
            chassisPart.setName(resultSet.getString(2));
            chassisPart.setPrice(resultSet.getInt(3));
            chassisPart.setAvailability(resultSet.getBoolean(4));
            chassisPart.setCondition(resultSet.getInt(5));
            chassisPart.setSide(resultSet.getString(6));
            chassisPart.setChassisType(resultSet.getString(7));
        }
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart, Constants.CHASSIS_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
        return chassisPart;
    }

    public void updateChassisPart(ChassisPart chassisPart) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.UPDATE_CHASSIS_PART,
                chassisPart.getPartID(),
                chassisPart.getName(),
                chassisPart.getPrice(),
                chassisPart.getAvailability(),
                chassisPart.getCondition(),
                chassisPart.getSide(),
                chassisPart.getChassisType()
        ));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart, Constants.CHASSIS_PART, className, methodName), Constants.MONGODB_TEST_SERVER);

    }

    public void deleteChassisPart(ChassisPart chassisPart) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.DELETE_CHASSIS_PART,chassisPart.getPartID()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(chassisPart, Constants.CHASSIS_PART, className, methodName), Constants.MONGODB_TEST_SERVER);

    }

    //ENGINEPART METHODS

    public void insertEnginePart(EnginePart enginePart) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        try {
            statement.executeUpdate(String.format(Constants.CREATE_ENGINE_PART,
                    enginePart.getPartID(),
                    enginePart.getName(),
                    enginePart.getPrice(),
                    enginePart.getAvailability(),
                    enginePart.getCondition(),
                    enginePart.getFuel(),
                    enginePart.getSerialNumber(),
                    enginePart.getVolume().toString().replace(',','.')
            ));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart, Constants.ENGINE_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
        } catch (JdbcSQLIntegrityConstraintViolationException e){

        }
        closeConnection();
    }

    public EnginePart getEnginePartByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_ENGINE_PART_BY_ID,id));
        EnginePart enginePart = new EnginePart();
        if(resultSet.next()){
            enginePart.setPartID(resultSet.getLong(1));
            enginePart.setName(resultSet.getString(2));
            enginePart.setPrice(resultSet.getInt(3));
            enginePart.setAvailability(resultSet.getBoolean(4));
            enginePart.setCondition(resultSet.getInt(5));
            enginePart.setFuel(resultSet.getString(6));
            enginePart.setSerialNumber(resultSet.getInt(7));
            enginePart.setVolume(resultSet.getFloat(8));
        }
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart, Constants.ENGINE_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
        return enginePart;
    }

    public void updateEnginePart(EnginePart enginePart) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.UPDATE_ENGINE_PART,
                enginePart.getPartID(),
                enginePart.getName(),
                enginePart.getPrice(),
                enginePart.getAvailability(),
                enginePart.getCondition(),
                enginePart.getFuel(),
                enginePart.getSerialNumber(),
                enginePart.getVolume()
        ));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart, Constants.ENGINE_PART, className, methodName), Constants.MONGODB_TEST_SERVER);

    }

    public void deleteEnginePart(EnginePart enginePart) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.DELETE_ENGINE_PART,enginePart.getPartID()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(enginePart, Constants.ENGINE_PART, className, methodName), Constants.MONGODB_TEST_SERVER);

    }

    //ELECTRICITYPART METHODS

    public void insertElectricityPart(ElectricityPart electricityPart) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        try {
            statement.executeUpdate(String.format(Constants.CREATE_ELECTRICITY_PART,
                    electricityPart.getPartID(),
                    electricityPart.getName(),
                    electricityPart.getPrice(),
                    electricityPart.getAvailability(),
                    electricityPart.getEngineVolume().toString().replace(',','.'),
                    electricityPart.getPower().toString().replace(',','.')
            ));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart, Constants.ELECTRICITY_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
        } catch (JdbcSQLIntegrityConstraintViolationException e){

        } finally {
            closeConnection();
        }
    }

    public ElectricityPart getElectricityPartByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_ELECTRICITY_PART_BY_ID,id));
        log.debug(String.format(Constants.SELECT_ELECTRICITY_PART_BY_ID,id));
        ElectricityPart electricityPart = new ElectricityPart();
        if(resultSet.next()){
            electricityPart.setPartID(resultSet.getLong(1));
            electricityPart.setName(resultSet.getString(2));
            electricityPart.setPrice(resultSet.getInt(3));
            electricityPart.setAvailability(resultSet.getBoolean(4));
            electricityPart.setEngineVolume(resultSet.getFloat(5));
            electricityPart.setPower(resultSet.getFloat(6));
        }
        log.debug(electricityPart);
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart, Constants.ELECTRICITY_PART, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
        return electricityPart;
    }

    public void updateElectricityPart(ElectricityPart electricityPart) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.UPDATE_ELECTRICITY_PART,
                electricityPart.getPartID(),
                electricityPart.getName(),
                electricityPart.getPrice(),
                electricityPart.getAvailability(),
                electricityPart.getEngineVolume(),
                electricityPart.getPower()
        ));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart, Constants.ELECTRICITY_PART, className, methodName), Constants.MONGODB_TEST_SERVER);

    }

    public void deleteElectricityPart(ElectricityPart electricityPart) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.DELETE_ELECTRICITY_PART,electricityPart.getPartID()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(electricityPart, Constants.ELECTRICITY_PART, className, methodName), Constants.MONGODB_TEST_SERVER);

    }


    //ORDER_CHASSISPART METHODS


    public void insertOrderChassisParts(List<ChassisPart> orderChassisPartList, Long orderID) throws SQLException, IOException {
        initDataSource();
        initConnection();
        orderChassisPartList.forEach(n->{
            try{
                insertChassisPart(n);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        List<ChassisPart> chassisPartList=getChassisPartList();
        List<Long> partIDs = chassisPartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<ChassisPart> filteredChassisParts = orderChassisPartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredChassisParts.forEach(n->{
            try {
                initDataSource();
                initConnection();
                log.info(String.format(Constants.CREATE_ORDER_CHASSISPARTS,
                        orderID,
                        n.getPartID()));
                statement.executeUpdate(String.format(Constants.CREATE_ORDER_CHASSISPARTS,
                        orderID,
                        n.getPartID()));
                closeConnection();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<ChassisPart> getOrderChassisPartsByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        List<ChassisPart> chassisPartlist=new ArrayList<>();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_ORDER_CHASSISPARTS_BY_ID,id));
        while (resultSet.next()){
            chassisPartlist.add(getChassisPartByID(resultSet.getLong(2)));
        }
        closeConnection();
        return chassisPartlist;
    }

    public void updateOrderChassisParts(List<ChassisPart> orderChassisPartList,Long orderID) throws SQLException, IOException {
        List<ChassisPart> chassisPartList=getChassisPartList();
        List<Long> partIDs = chassisPartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<ChassisPart> filteredChassisParts = orderChassisPartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredChassisParts.forEach(n->{
            try {
                statement.executeUpdate(String.format(Constants.UPDATE_ORDER_CHASSISPARTS,
                        n.getPartID(),
                        orderID
                ));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public void deleteOrderChassisParts(List<ChassisPart> orderChassisPartList,Long orderID) throws SQLException, IOException {
        initDataSource();
        initConnection();
        List<ChassisPart> chassisPartList=getChassisPartList();
        List<Long> partIDs = chassisPartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<ChassisPart> filteredChassisParts = orderChassisPartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredChassisParts.forEach(n->{
            try {
                initDataSource();
                initConnection();
                statement.executeUpdate(String.format(Constants.DELETE_ORDER_CHASSISPARTS,
                        orderID));
                closeConnection();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        closeConnection();
    }


    //ORDER_ENGINEPARTS METHODS

    public void insertOrderEngineParts(List<EnginePart> orderEnginePartList, Long orderID) throws SQLException, IOException {
        initDataSource();
        initConnection();
        orderEnginePartList.forEach(n->{
            try{
                insertEnginePart(n);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        List<EnginePart> enginePartList=getEnginePartList();
        List<Long> partIDs = enginePartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<EnginePart> filteredEngineParts = orderEnginePartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredEngineParts.forEach(n->{
            try {
                initDataSource();
                initConnection();
                log.info(String.format(Constants.CREATE_ORDER_ENGINEPARTS,
                        orderID,
                        n.getPartID()));
                statement.executeUpdate(String.format(Constants.CREATE_ORDER_ENGINEPARTS,
                        orderID,
                        n.getPartID()));
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        closeConnection();
    }

    public List<EnginePart> getOrderEnginePartsByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        List<EnginePart> enginePartlist=new ArrayList<>();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_ORDER_ENGINEPARTS_BY_ID,id));
        while (resultSet.next()){
            enginePartlist.add(getEnginePartByID(resultSet.getLong(2)));
        }
        initDataSource();
        initConnection();
        return enginePartlist;

    }

    public void updateOrderEngineParts(List<EnginePart> orderEnginePartList,Long orderID) throws SQLException, IOException {
        List<EnginePart> enginePartList=getEnginePartList();
        List<Long> partIDs = enginePartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<EnginePart> filteredEngineParts = orderEnginePartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredEngineParts.forEach(n->{
            try {
                statement.executeUpdate(String.format(Constants.UPDATE_ORDER_ENGINEPARTS,
                        n.getPartID(),
                        orderID
                ));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public void deleteOrderEngineParts(List<EnginePart> orderEnginePartList,Long orderID) throws SQLException, IOException {
        List<EnginePart> enginePartList=getEnginePartList();
        List<Long> partIDs = enginePartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<EnginePart> filteredEngineParts = orderEnginePartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredEngineParts.forEach(n->{
            try {
                initDataSource();
                initConnection();
                statement.executeUpdate(String.format(Constants.DELETE_ORDER_ENGINEPARTS,
                        orderID));
                closeConnection();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        closeConnection();

    }


    //ORDER_ELECTRICTYPARTS METHODS


    public void insertOrderElectricityParts(List<ElectricityPart> orderElectricityPartList, Long orderID) throws SQLException, IOException {
        initDataSource();
        initConnection();
        orderElectricityPartList.forEach(n->{
            try{
                insertElectricityPart(n);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        List<ElectricityPart> enginePartList=getElectricityPartList();
        List<Long> partIDs = enginePartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<ElectricityPart> filteredElectricityParts = orderElectricityPartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredElectricityParts.forEach(n->{
            try {
                initDataSource();
                initConnection();
                statement.executeUpdate(String.format(Constants.CREATE_ORDER_ELECTRICITYPARTS,
                        orderID,
                        n.getPartID()));
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        closeConnection();
    }

    public List<ElectricityPart> getOrderElectricityPartsByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        List<ElectricityPart> electricityPartList=new ArrayList<>();
        ResultSet electricityPartResultSet = statement.executeQuery(String.format(Constants.SELECT_ORDER_ELECTRICITYPARTS_BY_ID,id));
        log.debug(String.format(Constants.SELECT_ORDER_ELECTRICITYPARTS_BY_ID,id));
        //log.debug(electricityPartResultSet.getRow());
        while (electricityPartResultSet.next()){
            log.debug(electricityPartResultSet.getString("PARTID"));
            electricityPartList.add(getElectricityPartByID(electricityPartResultSet.getLong(2)));
        }
        closeConnection();
        return electricityPartList;

    }

    public void updateOrderElectricityParts(List<ElectricityPart> orderElectricityPartList,Long orderID) throws SQLException, IOException {
        List<ElectricityPart> enginePartList=getElectricityPartList();
        List<Long> partIDs = enginePartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<ElectricityPart> filteredElectricityParts = orderElectricityPartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredElectricityParts.forEach(n->{
            try {
                statement.executeUpdate(String.format(Constants.UPDATE_ORDER_ELECTRICITYPARTS,
                        n.getPartID(),
                        orderID
                ));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public void deleteOrderElectricityParts(List<ElectricityPart> orderElectricityPartList,Long orderID) throws SQLException, IOException {
        initDataSource();
        initConnection();
        List<ElectricityPart> enginePartList=getElectricityPartList();
        List<Long> partIDs = enginePartList.stream().map(Part::getPartID).collect(Collectors.toList());
        List<ElectricityPart> filteredElectricityParts = orderElectricityPartList.stream().filter(p -> partIDs.contains(p.getPartID())).collect(Collectors.toList());
        filteredElectricityParts.forEach(n->{
            try {
                initDataSource();
                initConnection();
                statement.executeUpdate(String.format(Constants.DELETE_ORDER_ELECTRICITYPARTS,
                        orderID));
                closeConnection();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        closeConnection();
    }

    //CAR METHODS

    public void insertCar(Car car) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.CREATE_CAR,
                car.getCarID(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getEngine()
        ));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CAR, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Car getCarByID(Long id) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_CAR_BY_ID,id));
        Car car = new Car();
        if (resultSet.next()){
            car.setCarID(resultSet.getLong(1));
            car.setBrand(resultSet.getString(2));
            car.setModel(resultSet.getString(3));
            car.setYear(resultSet.getInt(4));
            car.setEngine(resultSet.getString(5));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CAR, className, methodName), Constants.MONGODB_TEST_SERVER);
        }
        log.debug(car);
        return car;
    }

    public void updateCar(Car car) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.UPDATE_CAR,
                car.getCarID(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getEngine(),
                car.getCarID()
        ));

        saveToLog(mongoDBDataProvider.initHistoryContentTrue(car, Constants.CAR, className, methodName), Constants.MONGODB_TEST_SERVER);

    }


    //CLIENT METHODS

    public void insertClient(Client client) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        insertCar(client.getCar());
        try {
            statement.executeUpdate(String.format(Constants.CREATE_CLIENT,
                    client.getClientID(),
                    client.getName(),
                    client.getClientType(),
                    client.getCar().getCarID()
            ));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(client, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
        } catch (JdbcSQLIntegrityConstraintViolationException e){
            log.info(e);
        }
    }

    public Client getClientByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_CLIENT_BY_ID,id));
        Client client=new Client();
        if(resultSet.next()){
            client.setClientID(resultSet.getLong(1));
            client.setName(resultSet.getString(2));
            client.setClientType(ClientType.valueOf(resultSet.getString(3)));
            client.setCar(getCarByID(resultSet.getLong(4)));
        }
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(client, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
        log.debug(client);
        closeConnection();
        return client;
    }

    public void updateClient(Client client) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.UPDATE_CLIENT,
                client.getName(),
                client.getClientType(),
                client.getCar().getCarID(),
                client.getClientID()
        ));
        updateCar(client.getCar());
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(client, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    public void deleteClient(Client client) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.DELETE_CLIENT,client.getClientID()));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(client, Constants.CLIENT, className, methodName), Constants.MONGODB_TEST_SERVER);

    }


    //EMPLOYEE METHODS

    public void insertEmployee(Employee employee) throws SQLException, IOException {
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.CREATE_EMPLOYEE,
                employee.getEmployeeID(),
                employee.getEmployeeName()
        ));
        saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.EMPLOYEE, className, methodName), Constants.MONGODB_TEST_SERVER);
    }

    public Employee getEmployeeByID(Long id) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        ResultSet resultSet = statement.executeQuery(String.format(Constants.SELECT_EMPLOYEE_BY_ID,id));
        Employee employee = new Employee();
        if (resultSet.next()){
            employee.setEmployeeID(resultSet.getLong(1));
            employee.setEmployeeName(resultSet.getString(2));
            saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.EMPLOYEE, className, methodName), Constants.MONGODB_TEST_SERVER);
        }
        log.debug(employee);
        closeConnection();
        return employee;
    }

    public void updateEmployee(Employee employee) throws SQLException, IOException {
        initDataSource();
        initConnection();
        String methodName = getMethodName();
        String className = getClassName();
        statement.executeUpdate(String.format(Constants.UPDATE_EMPLOYEE,
                employee.getEmployeeName(),
                employee.getEmployeeID()
        ));

        saveToLog(mongoDBDataProvider.initHistoryContentTrue(employee, Constants.EMPLOYEE, className, methodName), Constants.MONGODB_TEST_SERVER);
        closeConnection();
    }

    // USE CASE METHODS

    public Double calculateMarkup(Long orderID) throws JAXBException, IOException {
        try{
            Order order = getOrderByID(orderID);
            ClientType clientType = order.getClient().getClientType();
            switch (clientType){
                case INDIVIDUAL:
                    order.setTotalMarkup(calculateIndividualMarkup(order));
                    updateOrder(order);
                    log.info(Constants.LOG_MARKUP + order.getTotalMarkup());
                    return calculateIndividualMarkup(order);
                case COMPANY:
                    order.setTotalMarkup(calculateCompanyMarkup(order));
                    updateOrder(order);
                    log.info(Constants.LOG_MARKUP + order.getTotalMarkup());
                    return calculateCompanyMarkup(order);
                default:
                    order.setTotalMarkup(Constants.DOUBLE_ZERO);
                    updateOrder(order);
                    log.info(Constants.LOG_MARKUP + order.getTotalMarkup());
                    return order.getEmployeeSalary();
            }
        } catch (NullPointerException | SQLException e){
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
            updateOrder(order);
            log.info(Constants.LOG_SERVICE_INCOME + order.getTotalServiceIncome() + Constants.LOG_EMPLOYEE_INCOME + order.getTotalEmployeeIncome());
            return order;
        } catch (NullPointerException | SQLException e){
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
