package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.Car;
import ru.sfedu.servicestation.beans.ClientType;
import ru.sfedu.servicestation.utils.Constants;

public class ClientTypeConverter extends AbstractBeanField {

    private static final Logger log = LogManager.getLogger(ClientTypeConverter.class);

    @Override
    public Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            return ClientType.valueOf(s);
        } catch (Exception e){
            log.info(e);
            return null;
        }
    }

}
