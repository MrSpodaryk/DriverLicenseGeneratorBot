package com.botscrew.dao;

import com.botscrew.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    User findByChatId(Long chatId);
}
