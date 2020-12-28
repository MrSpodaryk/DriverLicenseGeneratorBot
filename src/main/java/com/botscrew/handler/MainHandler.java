package com.botscrew.handler;

import com.botscrew.botframework.annotation.*;
import com.botscrew.constant.ButtonPostback;
import com.botscrew.constant.ChatState;
import com.botscrew.constant.GenderId;
import com.botscrew.messengercdk.model.outgoing.builder.QuickReplies;
import com.botscrew.messengercdk.model.outgoing.builder.SenderAction;
import com.botscrew.messengercdk.service.Messenger;
import com.botscrew.messengercdk.service.Sender;
import com.botscrew.models.DriverCategory;
import com.botscrew.models.DriverLicenseTemplate;
import com.botscrew.models.User;
import com.botscrew.service.DriverCategoryService;
import com.botscrew.service.DriverLicenseTemplateService;
import com.botscrew.service.GenderService;
import com.botscrew.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ChatEventsProcessor
public class MainHandler {

    @Autowired
    Sender sender;

    @Autowired
    Messenger messenger;

    @Autowired
    UserService userService;

    @Autowired
    DriverLicenseTemplateService driverLicenseTemplateService;

    @Autowired
    GenderService genderService;

    @Autowired
    DriverCategoryService driverCategoryService;

    private DriverLicenseTemplate driverLicenseTemplate;


    @Text(states = ChatState.WELCOME)
    public void handleWelcome(User user) {
        String name = messenger.getProfile(user.getChatId()).getFirstName();
        String greeting = "Hi " + name + "!";
        sender.send(SenderAction.typingOn(user));
        sender.send(user, greeting, 1000);
        user.setUnfinishedTemplateId(0);
        userService.save(user);
        userService.changeState(user, "default");
        handleText(user);
    }

    @Text
    public void handleText(User user) {
        sender.send(SenderAction.typingOn(user), 2000);
        sender.send(QuickReplies.builder()
                        .user(user)
                        .text("I am your personal assistant for building driver licence template")
                        .postback("Build new template", ButtonPostback.START_NEW_TEMPLATE_BUILDING)
                        .build(),
                6000);
    }

    @Postback(value = ButtonPostback.START_NEW_TEMPLATE_BUILDING)
    public void handleStartBuildingTemplate(User user) {
        sender.send(SenderAction.typingOn(user));
        sender.send(QuickReplies.builder()
                        .user(user)
                        .text("I need to know your sex, please, select it")
                        .postback("Male", ButtonPostback.GENDER + "?gender=male")
                        .postback("Female", ButtonPostback.GENDER + "?gender=female")
                        .build(),
                2000);
    }

    @Postback(value = ButtonPostback.GENDER)
    public void handleGender(User user, @Param("gender") String gender) {
        driverLicenseTemplate = driverLicenseTemplateService.getTemplateById(user.getUnfinishedTemplateId());

        if (gender.equals("male")) {
            driverLicenseTemplate.setGender(genderService.getGenderById(GenderId.MALE));
        } else {
            driverLicenseTemplate.setGender(genderService.getGenderById(GenderId.FEMALE));
        }
        driverLicenseTemplate.setUser(user);
        driverLicenseTemplate.setUid("AB" + Math.abs(ThreadLocalRandom.current().nextInt()));
        driverLicenseTemplateService.save(driverLicenseTemplate);
        userService.findUserById(user.getId());
        user.setUnfinishedTemplateId(driverLicenseTemplate.getId());
        userService.save(user);
        sender.send(SenderAction.typingOn(user));
        sender.send(QuickReplies.builder()
                        .user(user)
                        .text("Thank you, can i use your name from Facebook to fill template?")
                        .postback("Yes", ButtonPostback.FACEBOOK_NAME + "?use=true")
                        .postback("No", ButtonPostback.FACEBOOK_NAME + "?use=false")
                        .build(),
                2000);
    }

    @Postback(ButtonPostback.FACEBOOK_NAME)
    public void handleFacebookName(User user, @Param("use") String use) {
        driverLicenseTemplate = driverLicenseTemplateService.getTemplateById(user.getUnfinishedTemplateId());

        if (Boolean.parseBoolean(use)) {
            driverLicenseTemplate.setFirstName(messenger.getProfile(user.getChatId()).getFirstName());
            driverLicenseTemplate.setLastName(messenger.getProfile(user.getChatId()).getLastName());
            driverLicenseTemplate.setImgUrl(messenger.getProfile(user.getChatId()).getProfilePicture());
            driverLicenseTemplateService.save(driverLicenseTemplate);
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, enter your date of birth in dd/mm/yyyy format, for example: 04/05/1988", 2000);
            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, enter your name", 1000);
            userService.changeState(user, ChatState.ENTER_NAME);
        }
    }


    @Text(states = ChatState.ENTER_NAME)
    public void handleEnterName(User user, @Text String name) {
        driverLicenseTemplate = driverLicenseTemplateService.getTemplateById(user.getUnfinishedTemplateId());

        if (!name.isEmpty()) {
            driverLicenseTemplate.setFirstName(name);
            driverLicenseTemplateService.save(driverLicenseTemplate);
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "And now enter your surname", 1000);
            userService.changeState(user, ChatState.ENTER_SURNAME);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, enter your name", 1000);
            userService.changeState(user, ChatState.ENTER_NAME);
        }
    }

    @Text(states = ChatState.ENTER_SURNAME)
    public void handleEnterSurname(User user, @Text String surname) {
        driverLicenseTemplate = driverLicenseTemplateService.getTemplateById(user.getUnfinishedTemplateId());

        if (!surname.isEmpty()) {
            driverLicenseTemplate.setLastName(surname);
            driverLicenseTemplateService.save(driverLicenseTemplate);
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, enter your date of birth in dd/mm/yyyy format, for example: 04/05/1988", 2000);
            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, enter your surname", 1000);
            userService.changeState(user, ChatState.ENTER_SURNAME);
        }
    }

    @Text(states = ChatState.ENTER_DATE_OF_BIRTH)
    public void handleEnterDateOfBirth(User user, @Text String date) {
        driverLicenseTemplate = driverLicenseTemplateService.getTemplateById(user.getUnfinishedTemplateId());

        Predicate<String> dateTester = d -> {
            Pattern pattern = Pattern.compile("^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$");
            Matcher matcher = pattern.matcher(d);
            return matcher.matches();
        };

        if (dateTester.test(date)) {
            driverLicenseTemplate.setDateOfBirth(date);
            driverLicenseTemplateService.save(driverLicenseTemplate);
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Cool, now i would like to know all your driver categories, so, please, enter them", 2000);
            userService.changeState(user, ChatState.ENTER_ALL_CATEGORY);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "You entered date with wrong format, please try again", 2000);
            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
        }
    }

    @Text(states = ChatState.ENTER_ALL_CATEGORY)
    public void handleEnterAllCategory(User user, @Text String listOfCategory) {
        driverLicenseTemplate = driverLicenseTemplateService.getTemplateById(user.getUnfinishedTemplateId());

        if (!listOfCategory.isEmpty()) {
            Set<String> allCategories = new HashSet<>(Arrays.asList(listOfCategory.split("[,\\s]")));
            Set<DriverCategory> allDriverCategories = driverCategoryService.findAll().stream()
                    .filter(category -> allCategories.stream()
                            .map(String::toUpperCase)
                            .collect(Collectors.toSet())
                            .contains(category.getCategory()))
                    .collect(Collectors.toSet());

            driverLicenseTemplate.setDriverCategoryList(allDriverCategories);
            driverLicenseTemplateService.save(driverLicenseTemplate);
            sender.send(user, "Cool, now i would like to know your Email, so, please, enter it", 2000);
            userService.changeState(user, ChatState.ENTER_EMAIL);
        } else {
            sender.send(user, "You must have at least one driver category to create a template", 2000);
            userService.changeState(user, ChatState.ENTER_ALL_CATEGORY);
        }
    }

    @Text(states = ChatState.ENTER_EMAIL)
    public void handleUseFacebookEmail(User user, @Text String email) {
        driverLicenseTemplate = driverLicenseTemplateService.getTemplateById(user.getUnfinishedTemplateId());

        Predicate<String> emailTester = d -> {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+\\=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$");
            Matcher matcher = pattern.matcher(d);
            return matcher.matches();
        };

        if (emailTester.test(email)) {
            driverLicenseTemplate.setEmail(email);
            driverLicenseTemplate.setTimestamp(new Timestamp(new Date().getTime()));
            driverLicenseTemplate.setFinished(true);
            driverLicenseTemplateService.save(driverLicenseTemplate);
            sender.send(SenderAction.typingOn(user));
            sender.send(QuickReplies.builder()
                            .user(user)
                            .text("Thank you, now you can see your driver license template")
                            .postback("See Templates", ButtonPostback.SEE_TEMPLATE)
                            .build(),
                    1000);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "You entered email with wrong format, please try again", 2000);
            userService.changeState(user, ChatState.ENTER_EMAIL);
        }
    }

    @Postback(value = ButtonPostback.SEE_TEMPLATE)
    public void handleSeeTemplate(User user) {
        userService.changeState(user, ChatState.WELCOME);
        sender.send(user, "http://localhost:8080/template/"+user.getId());
    }

    @Read
    public void handleRead(User user) {
    }

    @Echo
    public void handleEcho(User user) {
    }
}
