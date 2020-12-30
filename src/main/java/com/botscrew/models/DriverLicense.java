package com.botscrew.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DriverLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String Uid;
    private String firstName;
    private String lastName;
    private Timestamp timestamp;
    private String dateOfBirth;
    private String email;
    private boolean isFinished;
    private String imgUrl;
    private String driverCategory;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
