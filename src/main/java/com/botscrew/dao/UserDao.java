package com.botscrew.dao;

import com.botscrew.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByChatId(Long chatId);
}
