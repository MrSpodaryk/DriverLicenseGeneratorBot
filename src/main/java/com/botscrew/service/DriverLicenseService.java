package com.botscrew.service;

import com.botscrew.entity.DriverLicense;

import java.util.List;

public interface DriverLicenseService {
    void save(DriverLicense license);

    DriverLicense getDriverLicenseById(Integer id);

    List<DriverLicense> getAllDriverLicensesByUserId(Integer id);
}
