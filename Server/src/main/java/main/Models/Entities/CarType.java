package main.Models.Entities;

import javax.persistence.*;

@Entity
@Table(name = "car_types")
public class CarType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type", nullable = false)
    private int idType;

    @Column(name = "type_name", length = 20, nullable = false)
    private String typeName;

    public CarType() {
    }

    public CarType(int idType, String typeName) {
        this.idType = idType;
        this.typeName = typeName;
    }

    public int getIdType() { return idType; }

    public void setIdType(int idType) { this.idType = idType; }

    public String getTypeName() { return typeName; }

    public void setTypeName(String typeName) { this.typeName = typeName; }
}