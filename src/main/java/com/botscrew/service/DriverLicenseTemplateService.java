package com.botscrew.service;

import com.botscrew.models.DriverLicenseTemplate;

public interface DriverLicenseTemplateService {
    void save(DriverLicenseTemplate template);
    DriverLicenseTemplate getTemplateById(Integer id);
}
