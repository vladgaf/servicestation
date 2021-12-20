package ru.sfedu.servicestation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;

import ru.sfedu.servicestation.api.AbstractDataProvider;
import ru.sfedu.servicestation.api.DataProviderJDBC;
import ru.sfedu.servicestation.api.DataProviderCSV;
import ru.sfedu.servicestation.api.DataProviderXML;


import java.util.Arrays;
import java.util.Locale;

public class Main {
    private static Logger log = LogManager.getLogger(Main.class);

    private static CliBeansFiller cliBeansFiller = null;
    static {
        try {
            cliBeansFiller = new CliBeansFiller();
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws CsvDataTypeMismatchException, SQLException, JAXBException, CsvRequiredFieldEmptyException, IOException {
        if (args.length < 2) {
            log.error(Constants.CLI_ERROR_NOT_ENOUGH_ARGUMENTS);
            System.exit(0);
        }
        AbstractDataProvider dataProvider = getDataProvider(args[0]);

        switch (args[1].toUpperCase(Locale.ROOT)){
            case Constants.CLI_GENERATE_BEANS:
                switch(args[0].toUpperCase(Locale.ROOT)) {
                    case(Constants.CSV): {
                        cliBeansFiller.defaultBeansCSV();
                        break;
                    }
                    case(Constants.XML): {
                        cliBeansFiller.defaultBeansXML();
                        break;
                    }
                    case(Constants.JDBC): {
                        cliBeansFiller.defaultBeansJDBC();
                        break;
                    }
                }

            case Constants.CLI_CLEAR_DATA:
                switch(args[0].toUpperCase(Locale.ROOT)) {
                    case(Constants.CSV): {
                        cliBeansFiller.dropBeansCSV();
                        break;
                    }
                    case(Constants.XML): {
                        cliBeansFiller.dropBeansXML();
                        break;
                    }
                    case(Constants.JDBC): {
                        cliBeansFiller.dropBeansJDBC();
                        break;
                    }
                }

            case Constants.CLI_CALCULATE_MARKUP:
                dataProvider.calculateIncome(Long.parseLong(args[2]));

            case Constants.CLI_CALCULATE_INCOME:
                dataProvider.calculateIncome(Long.parseLong(args[2]));

            default:
                log.info(Constants.CLI_ERROR_WRONG_COMMAND);
                System.exit(0);
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
