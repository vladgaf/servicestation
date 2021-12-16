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
        assertEquals(xmlInstance.calculateMarkup(1L), 250.0);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateMarkupFail() throws JAXBException, IOException {
        assertNull(xmlInstance.calculateMarkup(30L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateIndividualMarkupSuccess() throws IOException {
        assertEquals(xmlInstance.calculateIndividualMarkup(order1), 250.0);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateIndividualMarkupFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculateIndividualMarkup(xmlInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateCompanyMarkupSuccess() throws IOException {
        assertEquals(xmlInstance.calculateCompanyMarkup(order1), 500.0);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateCompanyMarkupFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculateCompanyMarkup(xmlInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateIncomeSuccess() throws JAXBException, IOException {
        xmlInstance.createOrder(test_order);
        test_order.setTotalServiceIncome(144.4);
        test_order.setTotalEmployeeIncome(900.0);
        assertEquals(xmlInstance.calculateIncome(1L), test_order);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateIncomeFail() throws JAXBException, IOException {
        assertNull(xmlInstance.calculateIncome(3L));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculatePartsIncomeSuccess() throws IOException {
        assertEquals(xmlInstance.calculatePartsIncome(order1), 44.400000000000006);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculatePartsIncomeFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculatePartsIncome(xmlInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }


    @Test
    void calculateEmployeeIncomeSuccess() throws IOException {
        assertEquals(xmlInstance.calculateEmployeeIncome(order1), 100.0);
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void calculateEmployeeIncomeFail() throws IOException, JAXBException {
        assertNull(xmlInstance.calculateEmployeeIncome(xmlInstance.getOrderByID(1L)));
        clearData(ConfigurationUtil.getConfigurationEntry(Constants.PATH_TO_XML));
    }

    @Test
    void testCreateEnginePart() throws JAXBException, IOException {
        xmlInstance.createEnginePart(enginePart1);

    }

    @Test
    void testCreateOrder() throws JAXBException, IOException {
        xmlInstance.createOrder(order1);
    }
}