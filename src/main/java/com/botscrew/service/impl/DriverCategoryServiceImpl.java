package com.botscrew.service.impl;

import com.botscrew.dao.DriverCategoryDao;
import com.botscrew.models.DriverCategory;
import com.botscrew.service.DriverCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverCategoryServiceImpl implements DriverCategoryService {

    @Autowired
    DriverCategoryDao driverCategoryDao;

    @Override
    public List<DriverCategory> findAll() {
        return driverCategoryDao.findAll();
    }
}
