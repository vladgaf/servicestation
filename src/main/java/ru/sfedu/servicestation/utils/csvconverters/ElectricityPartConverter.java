package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.ElectricityPart;
import ru.sfedu.servicestation.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElectricityPartConverter extends AbstractBeanField {

    private final String fieldDelimiter = Constants.ELECTRICITY_PART_FIELDS_DELIMITER;
    private final String objectDelimiter = Constants.ELECTRICITY_PART_OBJECT_DELIMITER;

    private static final Logger log = LogManager.getLogger(CarConverter.class);

    @Override
    public Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        List<ElectricityPart> electricityPartList=new ArrayList<>();
        List<String> stringList= Arrays.asList(s.split(objectDelimiter));
        try {
            stringList.forEach(x-> {
                ElectricityPart electricityPart = new ElectricityPart();
                String[] data = s.replace(",",".").split(fieldDelimiter);
                electricityPart.setEngineVolume(Float.parseFloat(data[0]));
                electricityPart.setPower(Float.parseFloat(data[1]));
                electricityPart.setPartID(Long.parseLong(data[2]));
                electricityPart.setName(data[3]);
                electricityPart.setPrice(Integer.parseInt(data[4]));
                electricityPart.setAvailability(Boolean.parseBoolean(data[5]));
                electricityPartList.add(electricityPart);
            });
        } catch (Exception e){
            log.error(e);
        } finally {
            return electricityPartList;
        }
    }

    @Override
    public String convertToWrite(Object value){
        List<ElectricityPart> electricityPartList=(List<ElectricityPart>) value;
        if (electricityPartList==null){
            return objectDelimiter;
        } else {
            List<String> stringList= electricityPartList.stream()
                    .map(x-> String.format("%f"
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
                            x.getEngineVolume(),
                            x.getPower(),
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
