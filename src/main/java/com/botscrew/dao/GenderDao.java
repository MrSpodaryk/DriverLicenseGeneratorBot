package com.botscrew.dao;

import com.botscrew.models.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderDao extends JpaRepository<Gender, Integer> {
}
