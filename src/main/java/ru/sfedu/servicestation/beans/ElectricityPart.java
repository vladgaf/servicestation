package ru.sfedu.servicestation.beans;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ElectricityPart extends Part{

    @CsvBindByName
    private Float engineVolume;
    @CsvBindByName
    private Float power;

    public ElectricityPart() {
    }

    public Float getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(Float engineVolume) {
        this.engineVolume = engineVolume;
    }

    public Float getPower() {
        return power;
    }

    public void setPower(Float power) {
        this.power = power;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricityPart that = (ElectricityPart) o;
        return Objects.equals(engineVolume, that.engineVolume) &&
                Objects.equals(power, that.power);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engineVolume, power);
    }

    @Override
    public String toString() {
        return "ElectricityPart{" +
                "engineVolume=" + engineVolume +
                ", power=" + power +
                ", partID=" + partID +
                ", name='" + partName + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                '}';
    }
}
