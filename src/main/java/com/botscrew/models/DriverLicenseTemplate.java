package com.botscrew.models;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "driver_license_template")
public class DriverLicenseTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "Uid")
    private String Uid;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Basic
    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "is_finished")
    private boolean isFinished;

    @Basic
    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="gender_id")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "driver_license_template_driver_category",
            joinColumns = @JoinColumn(name = "driver_license_template_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "driver_category_id", referencedColumnName = "id", nullable = false))
    private Set<DriverCategory> driverCategoryList = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<DriverCategory> getDriverCategoryList() {
        return driverCategoryList;
    }

    public void setDriverCategoryList(Set<DriverCategory> driverCategoryList) {
        this.driverCategoryList = driverCategoryList;
    }

    @Override
    public String toString() {
        return "DriverLicenseTemplate{" +
                "id=" + id +
                ", Uid='" + Uid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", timestamp='" + timestamp + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", isFinished=" + isFinished +
                ", imgUrl='" + imgUrl + '\'' +
//                ", user=" + user +
                ", driverCategoryList=" + driverCategoryList +
                '}';
    }
}
