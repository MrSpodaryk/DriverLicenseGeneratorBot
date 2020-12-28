package com.botscrew.service.impl;

import com.botscrew.dao.DriverLicenseTemplateDao;
import com.botscrew.models.DriverLicenseTemplate;
import com.botscrew.service.DriverLicenseTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverLicenseTemplateServiceImpl implements DriverLicenseTemplateService {

    @Autowired
    DriverLicenseTemplateDao driverLicenseTemplateDao;

    @Override
    public void save(DriverLicenseTemplate template) {
        driverLicenseTemplateDao.save(template);
    }

    @Override
    public DriverLicenseTemplate getTemplateByUserId(Integer id) {
        if (driverLicenseTemplateDao.findById(id).isPresent()){
            return driverLicenseTemplateDao.findById(id).get();
        } else {
            return new DriverLicenseTemplate();
        }
    }


}
