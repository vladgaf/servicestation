package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.EnginePart;
import ru.sfedu.servicestation.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnginePartConverter extends AbstractBeanField {

    private final String fieldDelimiter = Constants.ENGINE_PART_FIELDS_DELIMITER;
    private final String objectDelimiter = Constants.ENGINE_PART_OBJECT_DELIMITER;

    private static final Logger log = LogManager.getLogger(CarConverter.class);

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        List<EnginePart> enginePartList=new ArrayList<>();
        List<String> stringList= Arrays.asList(s.split(objectDelimiter));
        try {
            stringList.stream().forEach(x-> {
                EnginePart enginePart = new EnginePart();
                String[] data = s.replace(",",".").split(fieldDelimiter);
                enginePart.setCondition(Integer.parseInt(data[0]));
                enginePart.setFuel(data[1]);
                enginePart.setSerialNumber(Integer.parseInt(data[2]));
                enginePart.setVolume(Float.parseFloat(data[3]));
                enginePart.setPartID(Long.parseLong(data[4]));
                enginePart.setName(data[5]);
                enginePart.setPrice(Integer.parseInt(data[6]));
                enginePart.setAvailability(Boolean.parseBoolean(data[7]));
                enginePartList.add(enginePart);
            });
        } catch (NumberFormatException e){
            //log.error(e);
        } finally {
            return enginePartList;
        }
    }

    @Override
    public String convertToWrite(Object value){
        List<EnginePart> enginePartList=(List<EnginePart>) value;
        if (enginePartList==null){
            return objectDelimiter;
        } else {
            List<String> stringList= enginePartList.stream()
                    .map(x-> String.format("%d"
                                    +fieldDelimiter
                                    + "%s"
                                    +fieldDelimiter
                                    + "%d"
                                    +fieldDelimiter
                                    + "%f"
                                    +fieldDelimiter
                                    + "%d"
                                    +fieldDelimiter
                                    + "%s"
                                    +fieldDelimiter
                                    + "%d"
                                    +fieldDelimiter
                                    + "%b",
                            x.getCondition(),
                            x.getFuel(),
                            x.getSerialNumber(),
                            x.getVolume(),
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
