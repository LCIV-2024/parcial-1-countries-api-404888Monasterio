package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Entity
public class CountryEntity {
    @Id
    private String code;
    private String name;
    private long population;
    private double area;
    private String region;
    //private List<String> borders;
    //private Map<String, String> languages;
}
