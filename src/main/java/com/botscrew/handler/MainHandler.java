package com.botscrew.handler;

import com.botscrew.botframework.annotation.*;
import com.botscrew.constant.ButtonPostback;
import com.botscrew.constant.ChatState;
import com.botscrew.messengercdk.model.outgoing.builder.QuickReplies;
import com.botscrew.messengercdk.model.outgoing.builder.SenderAction;
import com.botscrew.messengercdk.service.Messenger;
import com.botscrew.messengercdk.service.Sender;
import com.botscrew.models.DriverLicenseTemplate;
import com.botscrew.models.User;
import com.botscrew.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ChatEventsProcessor
public class MainHandler {

    @Autowired
    private Sender sender;

    @Autowired
    private Messenger messenger;

    @Autowired
    private UserService userService;

    private DriverLicenseTemplate driverLicenseTemplate = new DriverLicenseTemplate();

    @Text
    public void handleText(User user) {
        System.out.println("user == " + user);
    }


//    @Text(states = ChatState.WELCOME)
//    public void handleWelcome(User user) {
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! handleWelcome user === " + user.toString());
//        String name = messenger.getProfile(user.getChatId()).getFirstName();
//        String greeting = "Hi " + name + "!";
//        sender.send(SenderAction.typingOn(user));
//        sender.send(user, greeting, 1000);
//        userService.changeState(user, "default");
//    }
//
//    @Text
//    public void handleText(User user) {
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! handleText user === " + user.toString());
//        sender.send(SenderAction.typingOn(user), 2000);
//        sender.send(QuickReplies.builder()
//                        .user(user)
//                        .text("I am your personal assistant for building driver licence template")
//                        .postback("Build template", ButtonPostback.START_TEMPLATE_BUILDING)
//                        .build(),
//                6000);
//    }
//
//    @Postback(value = ButtonPostback.START_TEMPLATE_BUILDING)
//    public void handleStart(User user) {
//        sender.send(SenderAction.typingOn(user));
//        sender.send(QuickReplies.builder()
//                        .user(user)
//                        .text("Can i use your name from Facebook to fill template?")
//                        .postback("Yes", ButtonPostback.FACEBOOK_NAME + "?use=true")
//                        .postback("No", ButtonPostback.FACEBOOK_NAME + "?use=false")
//                        .build(),
//                2000);
//    }
//
//    @Postback(ButtonPostback.FACEBOOK_NAME)
//    public void handleFacebookName(User user, @Param("use") String use) {
//        if (Boolean.parseBoolean(use)) {
//            driverLicenseTemplate.setFirstName(messenger.getProfile(user.getChatId()).getFirstName());
//            driverLicenseTemplate.setLastName(messenger.getProfile(user.getChatId()).getLastName());
//            driverLicenseTemplate.setImgUrl(messenger.getProfile(user.getChatId()).getProfilePicture());
//            driverLicenseTemplate.setUid("AB" + Math.abs(ThreadLocalRandom.current().nextInt()));
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "Please, enter your date of birth in dd/mm/yyyy format, for example: 04/05/1988", 2000);
//            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
//        } else {
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "Please, enter your name", 1000);
//            userService.changeState(user, ChatState.ENTER_NAME);
//        }
//    }
//
//
//    @Text(states = ChatState.ENTER_NAME)
//    public void handleEnterName(User user, @Text String name) {
//        if (!name.isEmpty()) {
//            driverLicenseTemplate.setFirstName(name);
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "And now enter your surname", 1000);
//            userService.changeState(user, ChatState.ENTER_SURNAME);
//        } else {
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "Please, enter your name", 1000);
//            userService.changeState(user, ChatState.ENTER_NAME);
//        }
//    }
//
//    @Text(states = ChatState.ENTER_SURNAME)
//    public void handleEnterSurname(User user, @Text String surname) {
//        if (!surname.isEmpty()) {
//            driverLicenseTemplate.setLastName(surname);
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "Please, enter your date of birth in dd/mm/yyyy format, for example: 04/05/1988", 2000);
//            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
//        } else {
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "Please, enter your surname", 1000);
//            userService.changeState(user, ChatState.ENTER_SURNAME);
//        }
//    }
//
//    @Text(states = ChatState.ENTER_DATE_OF_BIRTH)
//    public void handleEnterDateOfBirth(User user, @Text String date) {
//        Predicate<String> dateTester = d -> {
//            Pattern pattern = Pattern.compile("^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$");
//            Matcher matcher = pattern.matcher(d);
//            return matcher.matches();
//        };
//
//        if (dateTester.test(date)) {
//            driverLicenseTemplate.setDateOfBirth(date);
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "Cool, now i would like to know all your driver categories, so, please, enter them", 2000);
//            userService.changeState(user, ChatState.ENTER_ALL_CATEGORY);
//        } else {
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "You entered date with wrong format, please try again", 2000);
//            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
//        }
//    }
//
////    @Text(states = ChatState.ENTER_ALL_CATEGORY)
////    public void handleEnterAllCategory(User user, @Text String listOfCategory) {
////        if (!listOfCategory.isEmpty()) {
////            List<String> categories = Arrays.asList(listOfCategory.split("[,\\s]"));
////            categories = categories.stream()
////                    .filter(category -> DriverCategory.contains(category.toUpperCase()))
////                    .collect(Collectors.toList());
////            driverLicenseTemplate.setDriverCategoryList(categories);
////            sender.send(user, "Cool, now i would like to know your Email, so, please, enter it", 2000);
////            userService.changeState(user, ChatState.ENTER_EMAIL);
////        } else {
////            sender.send(user, "You must have at least one driver category to create a template", 2000);
////            userService.changeState(user, ChatState.ENTER_ALL_CATEGORY);
////        }
////    }
//
//    @Text(states = ChatState.ENTER_EMAIL)
//    public void handleUseFacebookEmail(User user, @Text String email) {
//        Predicate<String> emailTester = d -> {
//            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+\\=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$");
//            Matcher matcher = pattern.matcher(d);
//            return matcher.matches();
//        };
//
//        if (emailTester.test(email)) {
//            driverLicenseTemplate.setEmail(email);
//            sender.send(SenderAction.typingOn(user));
//            sender.send(QuickReplies.builder()
//                            .user(user)
//                            .text("Nice, and, finally, i need to know your sex, please, select it")
//                            .postback("Male", ButtonPostback.GENDER + "?gender=male")
//                            .postback("Female", ButtonPostback.GENDER + "?gender=female")
//                            .build(),
//                    2000);
//        } else {
//            sender.send(SenderAction.typingOn(user));
//            sender.send(user, "You entered date with wrong format, please try again", 2000);
//            userService.changeState(user, ChatState.ENTER_EMAIL);
//        }
//    }
//
////    @Postback(value = ButtonPostback.GENDER)
////    public void handleMale(User user, @Param("gender") String gender) {
////        if (gender.equals("male")) {
////            driverLicenseTemplate.setSex(Gender.MALE);
////        } else {
////            driverLicenseTemplate.setSex(Gender.FEMALE);
////        }
////        driverLicenseTemplate.setFinish(true);
////        showAllEnteredData(user);
////    }
//
//    private void showAllEnteredData(User user) {
//        sender.send(SenderAction.typingOn(user));
//        sender.send(user, driverLicenseTemplate.toString(), 2000);
//    }

    @Read
    public void handleRead(User user) {
        System.out.println("-----------READ == " + user.toString());
    }

    @Echo
    public void handleEcho(User user) {
        System.out.println("-----------ECHO == " + user.toString());
    }
}
