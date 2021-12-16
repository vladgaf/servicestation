package ru.sfedu.servicestation.utils;

import ru.sfedu.servicestation.beans.*;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLList {

    @XmlElement(name="car")
    private List<Car> carList=null;

    public List<Car> getCars(){
        return carList;
    }

    public void setCars(List<Car> carList){
        this.carList=carList;
    }

    @XmlElement(name="chassisPart")
    private List<ChassisPart> chassisPartList=null;

    public List<ChassisPart> getChassisParts(){
        return chassisPartList;
    }

    public void setChassisParts(List<ChassisPart> chassisPartList){
        this.chassisPartList=chassisPartList;
    }

    @XmlElement(name="client")
    private List<Client> clientList=null;

    public List<Client> getClients(){
        return clientList;
    }

    public void setClients(List<Client> clientList){
        this.clientList=clientList;
    }

    @XmlElement(name="clientType")
    private List<ClientType> clientTypeList=null;

    public List<ClientType> getClientTypes(){
        return clientTypeList;
    }

    public void setClientTypes(List<ClientType> clientTypeList){
        this.clientTypeList=clientTypeList;
    }

    @XmlElement(name="electricityPart")
    private List<ElectricityPart> electricityPartList=null;

    public List<ElectricityPart> getElectricityParts(){
        return electricityPartList;
    }

    public void setElectricityParts(List<ElectricityPart> electricityPartList){
        this.electricityPartList=electricityPartList;
    }

    @XmlElement(name="employee")
    private List<Employee> employeeList=null;

    public List<Employee> getEmployees(){
        return employeeList;
    }

    public void setEmployees(List<Employee> employeeList){
        this.employeeList=employeeList;
    }

    @XmlElement(name="enginePart")
    private List<EnginePart> enginePartList=null;

    public List<EnginePart> getEngineParts(){
        return enginePartList;
    }

    public void setEngineParts(List<EnginePart> enginePartList){
        this.enginePartList=enginePartList;
    }

    @XmlElement(name="order")
    private List<Order> orderList=null;

    public List<Order> getOrders(){
        return orderList;
    }

    public void setOrders(List<Order> orderList){
        this.orderList=orderList;
    }

    @XmlElement(name="part")
    private List<Part> partList=null;

    public List<Part> getParts(){
        return partList;
    }

    public void setParts(List<Part> partList){
        this.partList=partList;
    }
}
