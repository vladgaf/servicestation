package ru.sfedu.servicestation.utils;

public class Constants {

    public static final String PATH_TO_XML = "PATH_TO_XML";
    public static final String XML_FILE_EXTENSION = "XML_FILE_EXTENSION";

    public static final String PATH_TO_CSV = "PATH_TO_CSV";
    public static final String CSV_FILE_EXTENSION = "CSV_FILE_EXTENSION";

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


    public static final String LOG_MARKUP = "Markup: ";
    public static final String LOG_PARTS_INCOME_FS = "Parts income (for service): ";
    public static final String LOG_EMPLOYEE_INCOME_FS = "Parts income (for service): ";
    public static final String LOG_SERVICE_INCOME = "Service income: ";
    public static final String LOG_EMPLOYEE_INCOME = "Employee income: ";


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

    public static final Double INDIVIDUAL_RATIO = 0.25;
    public static final Double COMPANY_RATIO = 0.5;

    public static final Double PARTS_INCOME_RATIO = 0.2;
    public static final Double EMPLOYEE_INCOME_RATIO = 0.1;

    public static final Double DOUBLE_ZERO = 0.0;
    public static final Integer INT_ZERO = 0;

    public static final String MONGODB="MONGODB";
    public static final String MONGODB_TEST_SERVER="test";
    public static final String SYSTEM="System";
    public static final String NULL="null";

    public static final String STATUS_OK = "OK";
    public static final String STATUS_FAIL ="FAIL";


    public static final String CAR_FIELDS_DELIMITER = "@";
    public static final String CLIENT_FIELDS_DELIMITER = "^";
    public static final String CHASSIS_PART_FIELDS_DELIMITER = "%";
    public static final String CHASSIS_PART_OBJECT_DELIMITER = "#";
    public static final String ENGINE_PART_FIELDS_DELIMITER = "&";
    public static final String ENGINE_PART_OBJECT_DELIMITER = "!";
    public static final String ELECTRICITY_PART_FIELDS_DELIMITER = "<";
    public static final String ELECTRICITY_PART_OBJECT_DELIMITER = ">";
    public static final String EMPLOYEE_FIELDS_DELIMITER = "=";
}
