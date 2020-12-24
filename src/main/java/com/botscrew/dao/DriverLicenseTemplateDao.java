package com.botscrew.dao;

import com.botscrew.models.DriverLicenseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLicenseTemplateDao extends JpaRepository<DriverLicenseTemplate, Integer> {
}
