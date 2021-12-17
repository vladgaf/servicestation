package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.Client;
import ru.sfedu.servicestation.beans.Car;
import ru.sfedu.servicestation.beans.ClientType;
import ru.sfedu.servicestation.utils.csvconverters.*;
import ru.sfedu.servicestation.utils.Constants;

public class ClientConverter extends AbstractBeanField {

    private final String fieldDelimiter = Constants.CLIENT_FIELDS_DELIMITER;
    private static final Logger log = LogManager.getLogger(ClientConverter.class);

    private final ClientTypeConverter clientTypeConverter=new ClientTypeConverter();
    private final CarConverter carConverter=new CarConverter();

    @Override
    public Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        Client client = new Client();
        try {
            String[] data = s.split(fieldDelimiter);
            //String[] parsedData = data[0].split(fieldDelimiter);
            log.debug(data[0]);
            client.setCar((Car) carConverter.convert(data[3]));
            client.setClientID(Long.parseLong(data[0]));
            client.setClientType((ClientType) clientTypeConverter.convert(data[2]));
            client.setName(data[1]);
        } catch (NullPointerException e){
            log.error(e);
        } finally {
            return client;
        }
    }

    @Override
    public String convertToWrite(Object client){
        Client clientList = (Client) client;
        if (clientList==null){
            return null;
        } else {
            return String.format("%d"
                            + fieldDelimiter
                            + "%s"
                            + fieldDelimiter
                            + "%s"
                            + fieldDelimiter
                            + "%s",
                    clientList.getClientID(),
                    clientList.getName(),
                    clientList.getClientType(),
                    carConverter.convertToWrite(clientList.getCar()));
        }
    }
}
