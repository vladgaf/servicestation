package ru.sfedu.servicestation.utils.csvconverters;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.servicestation.beans.Car;
import ru.sfedu.servicestation.beans.ChassisPart;
import ru.sfedu.servicestation.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChassisPartConverter extends AbstractBeanField {

    private final String fieldDelimiter = Constants.CHASSIS_PART_FIELDS_DELIMITER;
    private final String objectDelimiter = Constants.CHASSIS_PART_OBJECT_DELIMITER;

    private static final Logger log = LogManager.getLogger(CarConverter.class);

    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        List<ChassisPart> chassisPartList=new ArrayList<>();
        List<String> stringList= Arrays.asList(s.split(objectDelimiter));
        try {
            stringList.stream().forEach(x-> {
                ChassisPart chassisPart = new ChassisPart();
                String[] data = s.replace(",",".").split(fieldDelimiter);
                chassisPart.setCondition(Integer.parseInt(data[0]));
                chassisPart.setSide(data[1]);
                chassisPart.setChassisType(data[2]);
                chassisPart.setPartID(Long.parseLong(data[3]));
                chassisPart.setName(data[4]);
                chassisPart.setPrice(Integer.parseInt(data[5]));
                chassisPart.setAvailability(Boolean.parseBoolean(data[6]));
                chassisPartList.add(chassisPart);
            });
        } catch (NumberFormatException e){
            //log.error(e);
        } finally {
            return chassisPartList;
        }
    }

    @Override
    public String convertToWrite(Object value){
        List<ChassisPart> chassisPartList=(List<ChassisPart>) value;
        if (chassisPartList==null){
            return objectDelimiter;
        } else {
            List<String> stringList= chassisPartList.stream()
                    .map(x-> String.format("%d"
                                    +fieldDelimiter
                                    + "%s"
                                    +fieldDelimiter
                                    + "%s"
                                    +fieldDelimiter
                                    + "%d"
                                    +fieldDelimiter
                                    + "%s"
                                    +fieldDelimiter
                                    + "%d"
                                    +fieldDelimiter
                                    + "%b",
                            x.getCondition(),
                            x.getSide(),
                            x.getChassisType(),
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


