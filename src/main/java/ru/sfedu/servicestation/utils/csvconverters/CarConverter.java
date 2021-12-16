package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.Car;
import ru.sfedu.servicestation.utils.Constants;

public class CarConverter extends AbstractBeanField{

    private final String fieldDelimiter = Constants.CAR_FIELDS_DELIMITER;
    private static final Logger log = LogManager.getLogger(CarConverter.class);

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        Car car = new Car();
        try {
            String[] data = s.split(fieldDelimiter);
            //log.debug(data[0]);
            car.setCarID(Long.parseLong(data[0]));
            car.setBrand(data[1]);
            car.setModel(data[2]);
            car.setYear(Integer.parseInt(data[3]));
            car.setEngine(data[4]);
        } catch (NullPointerException e){
            log.error(e);
        } finally {
            return car;
        }
    }

    @Override
    public String convertToWrite(Object car){
        Car carList = (Car) car;
        if (carList==null){
            return null;
        } else {
            return String.format("%d"
                            + fieldDelimiter
                            + "%s"
                            + fieldDelimiter
                            + "%s"
                            + fieldDelimiter
                            + "%d"
                            + fieldDelimiter
                            + "%s",
                    carList.getCarID(),
                    carList.getBrand(),
                    carList.getModel(),
                    carList.getYear(),
                    carList.getEngine());

        }
    }
}
