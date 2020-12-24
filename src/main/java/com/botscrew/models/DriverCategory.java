package com.botscrew.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "driver_category")
public class DriverCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "category")
    private String category;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "driver_license_template_driver_category",
            joinColumns = @JoinColumn(name = "driver_category_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "driver_license_template_id", referencedColumnName = "id", nullable = false))
    private Set<DriverLicenseTemplate> driverLicenseTemplateList = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<DriverLicenseTemplate> getDriverLicenseTemplateList() {
        return driverLicenseTemplateList;
    }

    public void setDriverLicenseTemplateList(Set<DriverLicenseTemplate> driverLicenseTemplateList) {
        this.driverLicenseTemplateList = driverLicenseTemplateList;
    }

    @Override
    public String toString() {
        return "DriverCategory{" +
                "id=" + id +
                ", category='" + category + '\'' +
//                ", driverLicenseTemplateList=" + driverLicenseTemplateList +
                '}';
    }
}
