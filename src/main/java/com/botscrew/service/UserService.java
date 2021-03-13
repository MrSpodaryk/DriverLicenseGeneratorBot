package com.botscrew.service;


import com.botscrew.entity.User;

public interface UserService {
    User createIfNotExist(Long chatId);

    void changeState(User user, String chatState);

    Boolean checkState(User user, String message);

    User findByChatId(Long userChatId);

    void save(User user);
}
