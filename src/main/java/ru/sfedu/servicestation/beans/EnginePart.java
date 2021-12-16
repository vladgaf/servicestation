package ru.sfedu.servicestation.beans;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EnginePart extends Part{

    @CsvBindByName
    private Integer condition;
    @CsvBindByName
    private String fuel;
    @CsvBindByName
    private Integer serialNumber;
    @CsvBindByName
    private Float volume;

    public EnginePart() {
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnginePart that = (EnginePart) o;
        return Objects.equals(condition, that.condition) &&
                Objects.equals(fuel, that.fuel) &&
                Objects.equals(serialNumber, that.serialNumber) &&
                Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, fuel, serialNumber, volume);
    }

    @Override
    public String toString() {
        return "EnginePart{" +
                "condition=" + condition +
                ", fuel='" + fuel + '\'' +
                ", serialNumber=" + serialNumber +
                ", volume=" + volume +
                '}';
    }
}
