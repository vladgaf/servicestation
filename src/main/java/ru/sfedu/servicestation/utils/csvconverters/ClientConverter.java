package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.Client;
import ru.sfedu.servicestation.utils.Constants;

public class ClientConverter extends AbstractBeanField {

    private final String fieldDelimiter = Constants.CLIENT_FIELDS_DELIMITER;
    private static final Logger log = LogManager.getLogger(CarConverter.class);

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        Client client = new Client();
        try {
            String[] data = s.split(fieldDelimiter);
            //log.debug(data[0]);
            client.setClientID(Long.parseLong(data[0]));
            //client.set
        } catch (NullPointerException e){
            log.error(e);
        } finally {
            return null;
        }

    }
}
