package com.botscrew.dao;

import com.botscrew.models.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverLicenseDao extends JpaRepository<DriverLicense, Integer> {
}
