package ru.sfedu.servicestation.beans;

import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChassisPart extends Part{

    @CsvBindByName
    private Integer condition;
    @CsvBindByName
    private String side;
    @CsvBindByName
    private String chassisType;

    public ChassisPart() {
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getChassisType() {
        return chassisType;
    }

    public void setChassisType(String chassisType) {
        this.chassisType = chassisType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChassisPart that = (ChassisPart) o;
        return Objects.equals(condition, that.condition) &&
                Objects.equals(side, that.side) &&
                Objects.equals(chassisType, that.chassisType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, side, chassisType);
    }

    @Override
    public String toString() {
        return "ChassisPart{" +
                "condition=" + condition +
                ", side='" + side + '\'' +
                ", chassisType='" + chassisType + '\'' +
                ", partID=" + partID +
                ", name='" + partName + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                '}';
    }
}
