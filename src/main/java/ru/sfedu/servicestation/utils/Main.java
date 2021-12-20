package ru.sfedu.servicestation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javax.xml.bind.JAXBException;
import java.io.IOException;

import ru.sfedu.servicestation.api.AbstractDataProvider;
import ru.sfedu.servicestation.api.DataProviderJDBC;
import ru.sfedu.servicestation.api.DataProviderCSV;
import ru.sfedu.servicestation.api.DataProviderXML;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;

public class Main {
    private static Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        switch (args[1].toUpperCase(Locale.ROOT)){
            case Constants.CLI_GENERATE_BEANS:


        }

    }



    private static AbstractDataProvider getDataProvider(String dpType) throws IOException, JAXBException, CsvRequiredFieldEmptyException, SQLException, CsvDataTypeMismatchException {
        switch(dpType.toUpperCase(Locale.ROOT)) {
            case(Constants.CSV): {
                return new DataProviderCSV();
            }
            case(Constants.XML): {
                return new DataProviderXML();
            }
            case(Constants.JDBC): {
                return new DataProviderJDBC();
            }
            default: {
                log.error(Constants.CLI_ERROR_INVALID_DP);
                System.exit(0);
            }
        }
        return null;
    }
}
