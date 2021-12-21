package ru.sfedu.servicestation.beans;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.servicestation.utils.csvconverters.*;

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
    @CsvCustomBindByName(required = false, converter = ClientConverter.class)
    private Client client;
    @CsvCustomBindByName(required = false, converter = EmployeeConverter.class)
    private Employee employee;
    @CsvCustomBindByName(required = false, converter = PartIDListConverter.class)
    private List<Long> partIDList;
    @CsvCustomBindByName(required = false, converter = PartsConverter.class)
    private List<Part> parts;
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

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public List<Long> getPartIDList() {
        return partIDList;
    }

    public void setPartIDList(List<Long> partIDList) {
        this.partIDList = partIDList;
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
                Objects.equals(employee, order.employee) &&
                Objects.equals(partIDList, order.partIDList) &&
                Objects.equals(parts, order.parts) &&
                Objects.equals(employeeSalary, order.employeeSalary) &&
                Objects.equals(totalServiceIncome, order.totalServiceIncome) &&
                Objects.equals(totalEmployeeIncome, order.totalEmployeeIncome) &&
                Objects.equals(totalMarkup, order.totalMarkup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, client, employee, partIDList, parts, employeeSalary, totalServiceIncome, totalEmployeeIncome, totalMarkup);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", client=" + client +
                ", employee=" + employee +
                ", partIDList=" + partIDList +
                ", parts=" + parts +
                ", employeeSalary=" + employeeSalary +
                ", totalServiceIncome=" + totalServiceIncome +
                ", totalEmployeeIncome=" + totalEmployeeIncome +
                ", totalMarkup=" + totalMarkup +
                '}';
    }
}