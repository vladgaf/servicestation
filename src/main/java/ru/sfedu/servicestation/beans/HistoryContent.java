package ru.sfedu.servicestation.beans;

import java.util.Map;
import java.util.Objects;

public class HistoryContent {
    private Long id;
    private String className;
    private String createdDate;
    private String actor;
    private String methodName;
    private Map<String,Object> object;
    private String status;

    public HistoryContent() {
    }

    public Long getId() {
        return id;
    }
    public String getClassName() {
        return className;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    public String getActor() {
        return actor;
    }
    public String getMethodName() {
        return methodName;
    }
    public Map<String, Object> getObject() {
        return object;
    }
    public String getStatus() {
        return status;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public void setObject(Map<String, Object> object) {
        this.object = object;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryContent that = (HistoryContent) o;
        return Objects.equals(id, that.id) && Objects.equals(className, that.className) && Objects.equals(createdDate, that.createdDate) && Objects.equals(actor, that.actor) && Objects.equals(methodName, that.methodName) && Objects.equals(object, that.object) && Objects.equals(status, that.status);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, className, createdDate, actor, methodName, object, status);
    }
    @Override
    public String toString() {
        return "HistoryContent{" +
                "id=" + id +
                ", className='" + className + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", actor='" + actor + '\'' +
                ", methodName='" + methodName + '\'' +
                ", object=" + object +
                ", status='" + status + '\'' +
                '}';
    }
}