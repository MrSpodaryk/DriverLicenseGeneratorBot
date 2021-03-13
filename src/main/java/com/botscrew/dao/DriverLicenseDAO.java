package com.botscrew.dao;

import com.botscrew.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverLicenseDAO extends JpaRepository<DriverLicense, Integer> {
}
