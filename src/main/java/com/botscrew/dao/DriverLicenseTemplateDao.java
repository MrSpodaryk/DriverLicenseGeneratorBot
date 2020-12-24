package com.botscrew.dao;

import com.botscrew.models.DriverLicenseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverLicenseTemplateDao extends JpaRepository<DriverLicenseTemplate, Integer> {
}
