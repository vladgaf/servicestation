package ru.sfedu.servicestation.api;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataProviderXMLTest2 extends TestBase{

    public DataProviderXMLTest2() throws IOException, JAXBException {
    }

    @Test
    void createCar() throws JAXBException, IOException {
        xmlInstance.createCar(car1);
    }
}