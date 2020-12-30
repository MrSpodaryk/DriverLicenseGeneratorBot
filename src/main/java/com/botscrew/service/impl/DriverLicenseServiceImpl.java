package com.botscrew.service.impl;

import com.botscrew.dao.DriverLicenseDao;
import com.botscrew.models.DriverLicense;
import com.botscrew.service.DriverLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverLicenseServiceImpl implements DriverLicenseService {

    private final DriverLicenseDao driverLicenseDao;

    @Override
    public void save(DriverLicense license) {
        driverLicenseDao.save(license);
    }

    @Override
    public DriverLicense getDriverLicenseById(Integer id) {
        if (driverLicenseDao.findById(id).isPresent()) {
            return driverLicenseDao.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public List<DriverLicense> getAllDriverLicensesByUserId(Integer id) {
        return driverLicenseDao.findAll().stream()
                .filter(license -> license.getUser().getId()
                        .equals(id))
                .collect(Collectors.toList());
    }
}
