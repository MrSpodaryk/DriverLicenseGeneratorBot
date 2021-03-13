package com.botscrew.entity;


import com.botscrew.messengercdk.model.MessengerUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class User implements MessengerUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Long chatId;
    private String state;
    private String firstName;
    private String lastName;
    private Integer unfinishedDriverLicenseId;

    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
    private Set<DriverLicense> driverLicenseList;


    @Override
    public Long getChatId() {
        return chatId;
    }

    @Override
    public String getState() {
        return state;
    }
}
