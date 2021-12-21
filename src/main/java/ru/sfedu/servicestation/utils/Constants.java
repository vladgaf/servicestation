package ru.sfedu.servicestation.utils;

import java.util.Locale;

public class Constants {

    //PATHS

    public static final String PATH_TO_XML = "PATH_TO_XML";
    public static final String XML_FILE_EXTENSION = "XML_FILE_EXTENSION";

    public static final String PATH_TO_CSV = "PATH_TO_CSV";
    public static final String CSV_FILE_EXTENSION = "CSV_FILE_EXTENSION";

    public static final String PATH_TO_SQL = "PATH_TO_SQL";

    //BEANS

    public static final String CLIENT = "clients";
    public static final String CHASSIS_PART = "chassisParts";
    public static final String CAR = "cars";
    public static final String CLIENT_TYPE = "clientTypes";
    public static final String ELECTRICITY_PART = "electricityPart";
    public static final String EMPLOYEE = "employees";
    public static final String EMPLOYEE_AREA = "employeeAreas";
    public static final String ENGINE_PART = "engineParts";
    public static final String ORDER = "orders";
    public static final String PART = "parts";

    //MULTIPLIERS

    public static final Double INDIVIDUAL_RATIO = 0.25;
    public static final Double COMPANY_RATIO = 0.5;

    public static final Double PARTS_INCOME_RATIO = 0.2;
    public static final Double EMPLOYEE_INCOME_RATIO = 0.1;

    public static final Double DOUBLE_ZERO = 0.0;
    public static final Integer INT_ZERO = 0;


    //LOG STRINGS

    public static final String LOG_MARKUP = "Markup: ";
    public static final String LOG_PARTS_INCOME_FS = "Parts income (for service): ";
    public static final String LOG_EMPLOYEE_INCOME_FS = "Parts income (for service): ";
    public static final String LOG_SERVICE_INCOME = "Service income: ";
    public static final String LOG_EMPLOYEE_INCOME = "\t Employee income: ";


    public static final String ERROR_FILE = "File is corrupted";
    public static final String ERROR_ID_IS_TAKEN = "Part ID already exists";

    public static final String ERROR_MARKUP_NF = "ERROR: Can't calculate markup. Order not found";
    public static final String ERROR_INCOME_NF = "ERROR: Can't calculate markup. Order not found";

    public static final String ERROR_CLIENT_NOT_FOUND = "ERROR:Client does not exist";
    public static final String ERROR_CAR_NOT_FOUND = "ERROR:Car does not exist";
    public static final String ERROR_EMPLOYEE_NOT_FOUND = "ERROR:Employee does not exist";
    public static final String ERROR_ORDER_NOT_FOUND = "ERROR:Order does not exist";

    public static final String ERROR_ENP_NOT_FOUND = "ERROR:EnginePart does not exist";
    public static final String ERROR_CHP_NOT_FOUND = "ERROR:ChassisPart does not exist";
    public static final String ERROR_ELP_NOT_FOUND = "ERROR:ElectricityPart does not exist";


    //MONGODB

    public static final String MONGODB="MONGODB";
    public static final String MONGODB_TEST_SERVER="test";
    public static final String SYSTEM="System";
    public static final String NULL="null";

    public static final int MONGODB_TIMEOUT = 300;
    public static final String MONGODB_CLOSE = "MongoDB: Nothing to close";
    public static final String MONGODB_FAULT_CONNECTION = "MongoDB: Connection not set";
    public static final String MONGODB_SUCCESSFUL_CONNECTION = "MongoDB: Connection set";
    public static final String MONGO_PING = "ping";
    public static final String HC_STATUS_OK = "OK";
    public static final String HC_STATUS_FAIL = "FAIL";

    //CSV DELIMITERS

    public static final String LIST_SPACE = " ";
    public static final String LIST_EMPTYSTRING = "";
    public static final String PARTIDLIST_OBJECT_DELIMITER = ",";
    public static final String LIST_BRACKET_L = "[";
    public static final String LIST_BRACKET_R = "]";
    public static final String CAR_FIELDS_DELIMITER = "@";
    public static final String CLIENT_FIELDS_DELIMITER = "\\^";
    public static final String CHASSIS_PART_FIELDS_DELIMITER = "\\*";
    public static final String CHASSIS_PART_OBJECT_DELIMITER = "#";
    public static final String ENGINE_PART_FIELDS_DELIMITER = "&";
    public static final String ENGINE_PART_OBJECT_DELIMITER = "!";
    public static final String ELECTRICITY_PART_FIELDS_DELIMITER = "<";
    public static final String ELECTRICITY_PART_OBJECT_DELIMITER = ">";
    public static final String EMPLOYEE_FIELDS_DELIMITER = "=";
    public static final String PARTS_FIELDS_DELIMITER = "-";
    public static final String PARTS_OBJECT_DELIMITER = "\\%";
    public static final String PART_ID_LIST_FIELD_DELIMITER = "‱";

    public static final String DROP_ALL_TABLES = "DROP TABLE IF EXISTS car, client, employee, enginepart, chassispart, electricitypart, orders, orders_parts, part";

    //SQL SELECT

    public static final String SELECT_EMPLOYEE="SELECT * FROM employee;";
    public static final String SELECT_CAR = "SELECT * FROM car;";
    public static final String SELECT_ORDER = "SELECT * FROM orders;";
    public static final String SELECT_CHASSIS_PART = "SELECT * FROM chassisPart;";
    public static final String SELECT_ENGINE_PART = "SELECT * FROM enginePart;";
    public static final String SELECT_ELECTRICITY_PART = "SELECT * FROM electricityPart;";
    public static final String SELECT_CLIENT = "SELECT * FROM client;";
    public static final String SELECT_PART = "SELECT * FROM part;";

    //SQL UPDATE

    public static final String UPDATE_EMPLOYEE_BY_ID = "UPDATE employee SET " +
            "employeeID = %d," +
            "employeeName ='%s'";

    public static final String UPDATE_ORDER = "UPDATE orders SET " +
            "orderID = %d," +
            "employeeSalary ='%s'," +
            "totalServiceIncome ='%s'," +
            "totalEmployeeIncome ='%s'," +
            "totalMarkup ='%s'" +
            "WHERE orderID = %d;";

    public static final String UPDATE_CHASSIS_PART = "UPDATE chassisPart SET " +
            "partID = %d," +
            "partName ='%s'," +
            "price = %d," +
            "availability ='%s'," +
            "condition = %d," +
            "side ='%s'," +
            "chassisType ='%s'" +
            "WHERE partID = %d;";

    public static final String UPDATE_ENGINE_PART = "UPDATE enginePart SET " +
            "partID = %d," +
            "partName ='%s'," +
            "price = %d," +
            "availability ='%s'," +
            "condition = %d," +
            "fuel ='%s'," +
            "serialNumber = %d," +
            "volume = %f" +
            "WHERE partID = %d;";

    public static final String UPDATE_ELECTRICITY_PART = "UPDATE electricityPart SET " +
            "partID = %d," +
            "partName ='%s'," +
            "price = %d," +
            "availability ='%s'" +
            "enginePower = %f," +
            "volume = %f" +
            "WHERE partID = %d;";


    public static final String UPDATE_CAR = "UPDATE car SET " +
            "carID = %d," +
            "brand ='%s'," +
            "model ='%s'," +
            "carYear = %d," +
            "engine = '%s'" +
            "WHERE carID = %d;";

    public static final String UPDATE_CLIENT = "UPDATE client SET " +
            "clientName ='%s'," +
            "clientType ='%s'," +
            "carID = %d" +
            "WHERE clientID = %d;";

    public static final String UPDATE_EMPLOYEE = "UPDATE employee SET " +
            "employeeName = '%s'" +
            "WHERE employeeID = %d;";

    public static final String UPDATE_PART = "UPDATE chassisPart SET " +
            "partName ='%s'," +
            "price = %d," +
            "availability ='%s'," +
            "WHERE partID = %d;";

    public static final String UPDATE_ORDER_PARTS ="UPDATE orders_parts SET " +
            "partID = %d" +
            "WHERE orderID = %d;";;
    
    //SQL CREATE

    public static final String CREATE_ORDER = "INSERT INTO orders"
            + "  (orderID ,employeeSalary, totalServiceIncome, totalEmployeeIncome, totalMarkup, partsIDList, clientID, employeeID) VALUES "
            + " (%d ,%s, %s, %s, %s, '%s', %d, %d);";

    public static final String CREATE_CHASSIS_PART =  "INSERT INTO chassisPart "
            + "  (partID, partName, price, availability, condition, side, chassisType) VALUES "
            + " (%d, '%s', %d, '%s', %d, '%s', '%s');";

    public static final String CREATE_ENGINE_PART = "INSERT INTO enginePart "
            + "  (partID, partName, price, availability, condition, fuel, serialNumber, volume) VALUES "
            + " (%d, '%s', %d, '%s', %d, '%s', %d, %s);";;

    public static final String CREATE_ELECTRICITY_PART = "INSERT INTO electricityPart "
            + "  (partID, partName, price, availability, engineVolume, power) VALUES "
            + " (%d, '%s', %d, '%s', %s, %s);";



    public static final String CREATE_CLIENT = "INSERT INTO client"
            + "  (clientID, clientName, clientType, carID) VALUES "
            + " (%d, '%s', '%s', %d);";

    public static final String CREATE_EMPLOYEE = "INSERT INTO employee"
            + "  (employeeID, employeeName) VALUES "
            + " (%d, '%s');";

    public static final String CREATE_CAR = "INSERT INTO car"
            + "  (carID, brand, model, carYear, engine) VALUES "
            + " (%d, '%s', '%s', %d, '%s');";

    public static final String CREATE_PART = "INSERT INTO part "
            + "  (partID, partName, price, availability) VALUES "
            + " (%d, '%s', %d, '%s');";

    public static final String CREATE_ORDER_PARTS = "INSERT INTO orders_parts"
            + " (orderID ,partID ) VALUES "
            + " (%d ,%d);";
    
    //SQL DELETE

    public static final String DELETE_EMPLOYEE="DELETE FROM employee WHERE employeeID=%d;";
    public static final String DELETE_CAR="DELETE FROM car WHERE carID=%d;";
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE orderID=%d;";
    public static final String DELETE_CHASSIS_PART =  "DELETE FROM chassisPart WHERE partID=%d;";
    public static final String DELETE_ENGINE_PART =  "DELETE FROM enginePart WHERE partID=%d;";
    public static final String DELETE_ELECTRICITY_PART =  "DELETE FROM electricityPart WHERE partID=%d;";
    public static final String DELETE_CLIENT = "DELETE FROM client WHERE clientID=%d;";
    public static final String DELETE_PART = "DELETE FROM part WHERE partID=%d;";
    public static final String DELETE_ORDER_PARTS = "DELETE FROM orders_parts WHERE orderID=%d;";



    //SQL SELECT QUERY

    public static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE orderID=%d;";
    public static final String SELECT_CHASSIS_PART_BY_ID = "SELECT * FROM chassisPart WHERE partID=%d;";
    public static final String SELECT_ENGINE_PART_BY_ID = "SELECT * FROM enginePart WHERE partID=%d;";
    public static final String SELECT_ELECTRICITY_PART_BY_ID = "SELECT * FROM electricityPart WHERE partID=%d;";
    public static final String SELECT_CAR_BY_ID ="SELECT * FROM car WHERE carID=%d;";
    public static final String SELECT_CLIENT_BY_ID = "SELECT * FROM client WHERE clientID=%d;";
    public static final String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employee WHERE employeeID=%d;";
    public static final String SELECT_PART_BY_ID = "SELECT * FROM part WHERE partID=%d;";
    public static final String SELECT_ORDER_PARTS_BY_ID = "SELECT * FROM orders_parts WHERE orderID=%d;";


    //CLI

    public static final String CSV = "CSV";
    public static final String XML = "XML";
    public static final String JDBC = "JDBC";

    public static final String CLI_ERROR_INVALID_DP = "ERROR: Invalid DataProvider";
    public static final String CLI_ERROR_NOT_ENOUGH_ARGUMENTS = "ERROR: Not enough arguments";
    public static final String CLI_ERROR_WRONG_COMMAND = "ERROR: Wrong command";

    public static final String BEANS_FILLED = "Beans filled successfully";
    public static final String JDBC_TABLES_DPOPPED = "JDBC: Tables dropped successfully";
    public static final String CSV_TABLES_DPOPPED = "CSV: Files cleared";
    public static final String XML_TABLES_DPOPPED = "XML: Files cleared";

    public static final String CLI_GENERATE_BEANS = "GENERATEBEANS";
    public static final String CLI_CLEAR_DATA = "CLEARDATA";
    public static final String CLI_CALCULATE_MARKUP = "CALCULATEMARKUP";
    public static final String CLI_CALCULATE_INCOME = "CALCULATEINCOME";

}
