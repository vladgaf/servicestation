package ru.sfedu.servicestation.api;

import org.junit.jupiter.api.Test;
import ru.sfedu.servicestation.utils.ConfigurationUtil;
import ru.sfedu.servicestation.utils.Constants;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataProviderXMLTest extends TestBase{

    public DataProviderXMLTest() throws IOException, JAXBException {
    }
    
    @Test
    void calculateMarkupSuccess() throws JAXBException, IOException {
        xmlInstance.createOrder(order1);
        assertEquals(xmlInstance.calculateMarkup(order1.getOrderID()), expectedIndividualMarkup);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateMarkupFail() throws JAXBException, IOException {
        assertNull(xmlInstance.calculateMarkup(30L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateIndividualMarkupSuccess() throws IOException, JAXBException {
        xmlInstance.createOrder(order1);
        assertEquals(xmlInstance.calculateIndividualMarkup(xmlInstance.getOrderByID(order1.getOrderID())), expectedIndividualMarkup);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateIndividualMarkupFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculateIndividualMarkup(xmlInstance.getOrderByID(corruptedOrderID)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateCompanyMarkupSuccess() throws IOException, JAXBException {
        xmlInstance.createOrder(order1);
        assertEquals(xmlInstance.calculateCompanyMarkup(xmlInstance.getOrderByID(order1.getOrderID())), expectedCompanyMarkup);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateCompanyMarkupFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculateCompanyMarkup(xmlInstance.getOrderByID(corruptedOrderID)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateIncomeSuccess() throws JAXBException, IOException {
        xmlInstance.createChassisPart(chassisPart1);
        xmlInstance.createEnginePart(enginePart1);
        xmlInstance.createElectricityPart(electricityPart1);
        xmlInstance.createOrder(test_order);
        test_order.setTotalServiceIncome(expectedTotalServiceIncome);
        test_order.setTotalEmployeeIncome(expectedTotalEmployeeIncome);
        assertEquals(xmlInstance.calculateIncome(order1.getOrderID()), test_order);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateIncomeFail() throws JAXBException, IOException {
        assertNull(xmlInstance.calculateIncome(3L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculatePartsIncomeSuccess() throws IOException, JAXBException {
        xmlInstance.createChassisPart(chassisPart1);
        xmlInstance.createEnginePart(enginePart1);
        xmlInstance.createElectricityPart(electricityPart1);
        xmlInstance.createOrder(order1);
        log.info(xmlInstance.getOrderByID(order1.getOrderID()));
        log.info(xmlInstance.calculatePartsIncome(xmlInstance.getOrderByID(order1.getOrderID())));
        assertEquals(xmlInstance.calculatePartsIncome(xmlInstance.getOrderByID(order1.getOrderID())), expectedPartsIncome);

        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculatePartsIncomeFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculatePartsIncome(xmlInstance.getOrderByID(corruptedOrderID)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateEmployeeIncomeSuccess() throws IOException, JAXBException {
        xmlInstance.createOrder(order1);
        assertEquals(xmlInstance.calculateEmployeeIncome(xmlInstance.getOrderByID(order1.getOrderID())), expectedEmployeeIncome);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateEmployeeIncomeFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculateEmployeeIncome(xmlInstance.getOrderByID(corruptedOrderID)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

}