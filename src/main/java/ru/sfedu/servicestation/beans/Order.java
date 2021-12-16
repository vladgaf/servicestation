package ru.sfedu.servicestation.beans;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable {

    @CsvBindByName
    private Long orderID;
    @CsvBindByName
    private Client client;
    @CsvBindByName
    private List<EnginePart> engineParts;
    @CsvBindByName
    private List<ElectricityPart> electricityParts;
    @CsvBindByName
    private List<ChassisPart> chassisParts;

    @CsvBindByName
    private Employee employee;

    @CsvBindByName
    private Double employeeSalary;

    @CsvBindByName
    private Double totalServiceIncome;

    @CsvBindByName
    private Double totalEmployeeIncome;

    @CsvBindByName
    private Double totalMarkup;

    public Order() {
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<EnginePart> getEngineParts() {
        return engineParts;
    }

    public void setEngineParts(List<EnginePart> engineParts) {
        this.engineParts = engineParts;
    }

    public List<ElectricityPart> getElectricityParts() {
        return electricityParts;
    }

    public void setElectricityParts(List<ElectricityPart> electricityParts) {
        this.electricityParts = electricityParts;
    }

    public List<ChassisPart> getChassisParts() {
        return chassisParts;
    }

    public void setChassisParts(List<ChassisPart> chassisParts) {
        this.chassisParts = chassisParts;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(Double employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public Double getTotalServiceIncome() {
        return totalServiceIncome;
    }

    public void setTotalServiceIncome(Double totalServiceIncome) {
        this.totalServiceIncome = totalServiceIncome;
    }

    public Double getTotalEmployeeIncome() {
        return totalEmployeeIncome;
    }

    public void setTotalEmployeeIncome(Double totalEmployeeIncome) {
        this.totalEmployeeIncome = totalEmployeeIncome;
    }

    public Double getTotalMarkup() {
        return totalMarkup;
    }

    public void setTotalMarkup(Double totalMarkup) {
        this.totalMarkup = totalMarkup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderID, order.orderID) &&
                Objects.equals(client, order.client) &&
                Objects.equals(engineParts, order.engineParts) &&
                Objects.equals(electricityParts, order.electricityParts) &&
                Objects.equals(chassisParts, order.chassisParts) &&
                Objects.equals(employee, order.employee) &&
                Objects.equals(employeeSalary, order.employeeSalary) &&
                Objects.equals(totalServiceIncome, order.totalServiceIncome) &&
                Objects.equals(totalEmployeeIncome, order.totalEmployeeIncome) &&
                Objects.equals(totalMarkup, order.totalMarkup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, client, engineParts, electricityParts, chassisParts, employee, employeeSalary, totalServiceIncome, totalEmployeeIncome, totalMarkup);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", client=" + client +
                ", engineParts=" + engineParts +
                ", electricityParts=" + electricityParts +
                ", chassisParts=" + chassisParts +
                ", employee=" + employee +
                ", employeeSalary=" + employeeSalary +
                ", totalServiceIncome=" + totalServiceIncome +
                ", totalEmployeeIncome=" + totalEmployeeIncome +
                ", totalMarkup=" + totalMarkup +
                '}';
    }
}