package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.Employee;
import ru.sfedu.servicestation.utils.Constants;

public class EmployeeConverter extends AbstractBeanField {
    private final String fieldDelimiter = Constants.EMPLOYEE_FIELDS_DELIMITER;
    private static final Logger log = LogManager.getLogger(CarConverter.class);

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        Employee employee = new Employee();
        try {
            String[] data = s.split(fieldDelimiter);
            employee.setEmployeeID(Long.parseLong(data[0]));
            employee.setName(data[1]);
        } catch (NullPointerException e){
            log.error(e);
        } finally {
            return employee;
        }
    }

    @Override
    public String convertToWrite(Object employee){
        Employee employeeList = (Employee) employee;
        if (employeeList==null){
            return null;
        } else {
            return String.format("%d"
                            + fieldDelimiter
                            + "%s",
                    employeeList.getEmployeeID(),
                    employeeList.getName());

        }
    }
}
