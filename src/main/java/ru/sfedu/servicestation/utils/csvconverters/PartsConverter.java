package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.ElectricityPart;
import ru.sfedu.servicestation.beans.EnginePart;
import ru.sfedu.servicestation.beans.Part;
import ru.sfedu.servicestation.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PartsConverter extends AbstractBeanField {

    private final String fieldDelimiter = Constants.PARTS_FIELDS_DELIMITER;
    private final String objectDelimiter = Constants.PARTS_OBJECT_DELIMITER;

    private static final Logger log = LogManager.getLogger(PartsConverter.class);

    @Override
    public Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        List<Part> partsList=new ArrayList<>();
        List<String> stringList= Arrays.asList(s.split(objectDelimiter));
        try {
            stringList.forEach(x-> {
                Part partObject = new Part();
                String[] data = x.split(fieldDelimiter);

                partObject.setPartID(Long.parseLong(data[0]));
                partObject.setName(data[1]);
                partObject.setPrice(Integer.parseInt(data[2]));
                //log.info(data[3]);
                partObject.setAvailability(Boolean.parseBoolean(data[3]));
                partsList.add(partObject);
            });
        } catch (Exception e){
            log.error(e);
        } finally {
            return partsList;
        }
    }

    @Override
    public String convertToWrite(Object value){
        List<Part> partsList=(List<Part>) value;
        if (partsList==null){
            return objectDelimiter;
        } else {
            List<String> stringList= partsList.stream()
                    .map(x-> String.format("%d"
                                    +fieldDelimiter
                                    + "%s"
                                    +fieldDelimiter
                                    + "%d"
                                    +fieldDelimiter
                                    + "%b",
                            x.getPartID(),
                            x.getName(),
                            x.getPrice(),
                            x.getAvailability()))
                    .collect(Collectors.toList()
                    );
            return String.join(objectDelimiter,stringList);
        }
    }
}
