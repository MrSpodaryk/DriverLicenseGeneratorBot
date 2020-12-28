package com.botscrew.service.impl;

import com.botscrew.constant.ChatState;
import com.botscrew.dao.UserDao;
import com.botscrew.messengercdk.model.MessengerUser;
import com.botscrew.messengercdk.model.incomming.Profile;
import com.botscrew.messengercdk.service.Messenger;
import com.botscrew.messengercdk.service.UserProvider;
import com.botscrew.models.User;
import com.botscrew.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService, UserProvider {

    @Autowired
    private UserDao userDao;

    @Autowired
    private Messenger messenger;

    @Override
    public User createIfNotExist(Long chatId) {
        User user = userDao.findByChatId(chatId);
        if (user == null) {
            user = new User();
            user.setChatId(chatId);
            Profile profileInfo = messenger.getProfile(chatId);
            user.setFirstName(profileInfo.getFirstName());
            user.setLastName(profileInfo.getLastName());

            user.setState(ChatState.WELCOME);
            userDao.save(user);
        }
        return user;
    }

    @Override
    public void changeState(User user, String chatState) {
        user.setState(chatState);
        userDao.save(user);
    }

    @Override
    public User findByChatId(Long userChatId) {
        return userDao.findByChatId(userChatId);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User findUserById(Integer userId) {
        if (userDao.findById(userId).isPresent()) {
            return userDao.findById(userId).get();
        } else {
            throw new NoSuchElementException();
        }

    }

    @Override
    public Boolean checkState(User user, String message) {
        return message.equalsIgnoreCase("stop");
    }

    @Override
    public MessengerUser getByChatIdAndPageId(Long chatId, Long pageId) {
        return createIfNotExist(chatId);
    }
}
