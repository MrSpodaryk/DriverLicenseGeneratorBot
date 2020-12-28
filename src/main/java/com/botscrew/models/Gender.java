package com.botscrew.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gender")
public class Gender {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Basic
	@Column(name = "gender")
	private String gender;

	@OneToMany (mappedBy="gender", fetch=FetchType.EAGER)
	private Set<DriverLicenseTemplate> driverLicenseTemplateList = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Set<DriverLicenseTemplate> getDriverLicenseTemplateList() {
		return driverLicenseTemplateList;
	}

	public void setDriverLicenseTemplateList(Set<DriverLicenseTemplate> driverLicenseTemplateList) {
		this.driverLicenseTemplateList = driverLicenseTemplateList;
	}

	@Override
	public String toString() {
		return "Gender{" +
				"id=" + id +
				", gender='" + gender + '\'' +
				'}';
	}
}