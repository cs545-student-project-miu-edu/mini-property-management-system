package edu.miu.cs545.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer noOfBedrooms;
    Double noOfBathrooms;
    Double plotSize;
    Double price;
    Double area;
    String description;
    @OneToOne(fetch = FetchType.LAZY)
    Address address;
    @ManyToOne(fetch = FetchType.LAZY)
    Owner owner;
    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
    @JsonManagedReference
    List<Offer> offers;
    @Enumerated(EnumType.STRING)
    PropertyState status;
}
