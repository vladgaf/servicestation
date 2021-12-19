package ru.sfedu.servicestation.api;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.api.MongoDBDataProvider;
import ru.sfedu.servicestation.beans.*;
import ru.sfedu.servicestation.api.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public abstract class AbstractDataProvider  {
    private final MongoDBDataProvider mdbDP =new MongoDBDataProvider();
    private static final Logger log = LogManager.getLogger(AbstractDataProvider.class);

    public AbstractDataProvider() throws IOException {}

    public void saveToLog(HistoryContent historyContent,String string) {
        mdbDP.insertRecord(historyContent,string);
    }


}
