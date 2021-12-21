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

public class PartIDListConverter extends AbstractBeanField {

    private final String fieldDelimiter = Constants.PART_ID_LIST_FIELD_DELIMITER;

    private static final Logger log = LogManager.getLogger(PartIDListConverter.class);


    @Override
    protected Object convert(String s) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        List<String> dataList = Arrays.asList(s.replace(Constants.LIST_SPACE, Constants.LIST_EMPTYSTRING)
                .replace(Constants.LIST_BRACKET_L, Constants.LIST_EMPTYSTRING)
                .replace(Constants.LIST_BRACKET_R, Constants.LIST_EMPTYSTRING)
                .split(Constants.PARTIDLIST_OBJECT_DELIMITER));
        List<Long> longDataList = new ArrayList<>();
        try{
            dataList.forEach(x->{
                longDataList.add(Long.parseLong(x));
            });
            //log.info(longDataList);
        } catch (Exception e) {

        }
        return longDataList;
    }

    @Override
    public String convertToWrite(Object longDataList){
        //log.info(longDataList);
        return longDataList.toString();
    }
}
