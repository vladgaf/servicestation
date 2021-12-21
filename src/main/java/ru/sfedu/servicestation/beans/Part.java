package ru.sfedu.servicestation.beans;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Part implements Serializable {
    @CsvBindByName
    protected Long partID;
    @CsvBindByName
    protected String name;
    @CsvBindByName
    protected Integer price;
    @CsvBindByName
    protected Boolean availability;

    public Part() {
    }

    public Long getPartID() {
        return partID;
    }

    public void setPartID(Long partID) {
        this.partID = partID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(partID, part.partID) &&
                Objects.equals(name, part.name) &&
                Objects.equals(price, part.price) &&
                Objects.equals(availability, part.availability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partID, name, price, availability);
    }

    @Override
    public String toString() {
        return "Part{" +
                "partID=" + partID +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                '}';
    }
}
