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

@Service
public class UserServiceImpl implements UserService, UserProvider {

    @Autowired
    private UserDao userDao;

    @Autowired
    private Messenger messenger;

    @Override
    public User createIfNotExist(Long chatId) {
        System.out.println("!!!!!!!!!!!!!!!!!!! createIfNotExist");
        System.out.println("!!!!!!!!!!!!!!!!!!! chatId === " + chatId);

        User user = userDao.findByChatId(chatId);
        System.out.println("!!!!!!!!!!!!!!!!!!! userDao === " + userDao);
        System.out.println("!!!!!!!!!!!!!!!!!!! user === " + user.toString());

        if (user == null) {
            System.out.println("!!!!!!!!!!!!!!!!!!! new user created");
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
        System.out.println("!!!!!!!!!!!!!!!!!!!!!! change state");
        user.setState(chatState);
        userDao.save(user);
    }

    @Override
    public User findByChatId(Long userChatId) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!! findByChatId");

        return userDao.findByChatId(userChatId);
    }

    @Override
    public void save(User user) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!! save");

        userDao.save(user);
    }

    @Override
    public Boolean checkState(User user, String message) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!! checkState");
        return message.equalsIgnoreCase("stop");
    }

    @Override
    public MessengerUser getByChatIdAndPageId(Long chatId, Long pageId) {
        return createIfNotExist(chatId);
    }
}
