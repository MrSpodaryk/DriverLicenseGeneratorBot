package com.botscrew.models;


import com.botscrew.messengercdk.model.MessengerUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements MessengerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "state")
    private String state;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
    private Set<DriverLicenseTemplate> driverLicenseTemplateList = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<DriverLicenseTemplate> getDriverLicenseTemplateList() {
        return driverLicenseTemplateList;
    }

    public void setDriverLicenseTemplateList(Set<DriverLicenseTemplate> driverLicenseTemplateList) {
        this.driverLicenseTemplateList = driverLicenseTemplateList;
    }

    @Override
    public Long getChatId() {
        System.out.println("???????????????????????  getChatId == " + chatId);

        return chatId;
    }

    @Override
    public String getState() {
        System.out.println("???????????????????????  getState == " + state);
        return state;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", state='" + state + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", driverLicenseTemplateList=" + driverLicenseTemplateList +
                '}';
    }
}
