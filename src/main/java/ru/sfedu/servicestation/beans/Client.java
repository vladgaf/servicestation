package ru.sfedu.servicestation.beans;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.servicestation.utils.csvconverters.CarConverter;
import ru.sfedu.servicestation.utils.csvconverters.ClientTypeConverter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Client {
    @CsvBindByName
    private Long clientID;
    @CsvBindByName
    private String name;
    @CsvCustomBindByName(required = false, converter = ClientTypeConverter.class)
    private ClientType clientType;
    @CsvCustomBindByName(required = false, converter = CarConverter.class)
    private Car car;

    public Client() {
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(clientID, client.clientID) &&
                Objects.equals(name, client.name) &&
                clientType == client.clientType &&
                Objects.equals(car, client.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, name, clientType, car);
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientID=" + clientID +
                ", name='" + name + '\'' +
                ", clientType=" + clientType +
                ", car=" + car +
                '}';
    }
}
