package com.botscrew.service.impl;

import com.botscrew.dao.GenderDao;
import com.botscrew.models.Gender;
import com.botscrew.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class GenderServiceImpl implements GenderService {

    @Autowired
    GenderDao genderDao;

    @Override
    public Gender getGenderById(Integer id) {
        if (genderDao.findById(id).isPresent()) {
            return genderDao.findById(id).get();
        } else {
            throw new NoSuchElementException();
        }
    }
}
