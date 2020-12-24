package com.botscrew.dao;

import com.botscrew.models.DriverCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverCategoryDao extends JpaRepository<DriverCategory, Integer> {
}
