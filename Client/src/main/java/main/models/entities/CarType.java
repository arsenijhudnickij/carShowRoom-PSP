package main.models.entities;

public class CarType {
    private int idType;
    private String typeName;

    public CarType() {
    }

    public CarType(int idType, String typeName) {
        this.idType = idType;
        this.typeName = typeName;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}