package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import ru.sfedu.servicestation.api.TestBase;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CarConverterTest extends TestBase {

    private static final Logger log = LogManager.getLogger(CarConverterTest.class);

    public CarConverterTest() throws IOException, JAXBException {
    }

    @Test
    void convert() throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        CarConverter carConverter = new CarConverter();
        carConverter.convert(car1.toString());
    }
}