package com.botscrew.handler;

import com.botscrew.botframework.annotation.ChatEventsProcessor;
import com.botscrew.botframework.annotation.Param;
import com.botscrew.botframework.annotation.Postback;
import com.botscrew.botframework.annotation.Text;
import com.botscrew.constant.ButtonPostback;
import com.botscrew.constant.ChatState;
import com.botscrew.messengercdk.model.outgoing.builder.GenericTemplate;
import com.botscrew.messengercdk.model.outgoing.builder.QuickReplies;
import com.botscrew.messengercdk.model.outgoing.builder.SenderAction;
import com.botscrew.messengercdk.model.outgoing.element.TemplateElement;
import com.botscrew.messengercdk.model.outgoing.element.button.PostbackButton;
import com.botscrew.messengercdk.model.outgoing.element.button.WebButton;
import com.botscrew.messengercdk.service.Messenger;
import com.botscrew.messengercdk.service.Sender;
import com.botscrew.entity.DriverCategory;
import com.botscrew.entity.DriverLicense;
import com.botscrew.entity.Gender;
import com.botscrew.entity.User;
import com.botscrew.service.DriverLicenseService;
import com.botscrew.service.UserService;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ChatEventsProcessor
@RequiredArgsConstructor
public class LicenceFlowHandler {

    private final Sender sender;
    private final Messenger messenger;
    private final UserService userService;
    private final DriverLicenseService driverLicenseService;
    private DriverLicense driverLicense;

    @Text(states = ChatState.WELCOME)
    public void handleWelcome(User user) {
        String name = messenger.getProfile(user.getChatId()).getFirstName();
        String greeting = "Hi " + name + "!";
        sender.send(SenderAction.typingOn(user));
        sender.send(user, greeting, 1000);
        userService.save(user);
        if (user.getUnfinishedDriverLicenseId() != null && driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId()) != null) {
            handleUser(user);
        } else {
            handleNewUser(user);
        }
    }

    public void handleUser(User user) {
        sender.send(SenderAction.typingOn(user), 1000);
        sender.send(QuickReplies.builder()
                        .user(user)
                        .text("I am your personal assistant for building driver licence template")
                        .postback("Build new driver license", ButtonPostback.START_NEW_DRIVER_LICENSE_BUILDING)
                        .postback("See my driver licenses", ButtonPostback.SEE_ALL_DRIVER_LICENSES)
                        .build(),
                3000);
    }

    public void handleNewUser(User user) {
        sender.send(SenderAction.typingOn(user), 1000);
        sender.send(QuickReplies.builder()
                        .user(user)
                        .text("I am your personal assistant for building driver licence template")
                        .postback("Build new driver license", ButtonPostback.START_NEW_DRIVER_LICENSE_BUILDING)
                        .build(),
                3000);
    }

    @Postback(value = ButtonPostback.START_NEW_DRIVER_LICENSE_BUILDING)
    public void handleStartBuildingDriverLicense(User user) {
        driverLicense = new DriverLicense();
        driverLicense.setUser(user);
        driverLicenseService.save(driverLicense);
        user.setUnfinishedDriverLicenseId(driverLicense.getId());
        userService.save(user);

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
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        if (gender.equals("male")) {
            driverLicense.setGender(Gender.MALE);
        } else {
            driverLicense.setGender(Gender.FEMALE);
        }

        driverLicense.setUid("AB" + Math.abs(ThreadLocalRandom.current().nextInt()));
        driverLicenseService.save(driverLicense);
        sender.send(SenderAction.typingOn(user));
        sender.send(QuickReplies.builder()
                        .user(user)
                        .text("Thank you, can i use your data from Facebook to fill template?")
                        .postback("Yes", ButtonPostback.FACEBOOK_DATA + "?use=true")
                        .postback("No", ButtonPostback.FACEBOOK_DATA + "?use=false")
                        .build(),
                2000);
    }

    @Postback(ButtonPostback.FACEBOOK_DATA)
    public void handleFacebookName(User user, @Param("use") String use) {
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        if (Boolean.parseBoolean(use)) {
            driverLicense.setFirstName(messenger.getProfile(user.getChatId()).getFirstName());
            driverLicense.setLastName(messenger.getProfile(user.getChatId()).getLastName());
            driverLicense.setImgUrl(messenger.getProfile(user.getChatId()).getProfilePicture());
            driverLicenseService.save(driverLicense);
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
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        if (!name.isEmpty()) {
            driverLicense.setFirstName(name);
            driverLicenseService.save(driverLicense);
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
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        if (!surname.isEmpty()) {
            driverLicense.setLastName(surname);
            driverLicenseService.save(driverLicense);
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, paste your photo url", 2000);
            userService.changeState(user, ChatState.ENTER_PHOTO_URL);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, enter your surname", 1000);
            userService.changeState(user, ChatState.ENTER_SURNAME);
        }
    }

    @Text(states = ChatState.ENTER_PHOTO_URL)
    public void handleEnterImgUrl(User user, @Text String imgUrl) {
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        if (!imgUrl.isEmpty()) {
            driverLicense.setImgUrl(imgUrl);
            driverLicenseService.save(driverLicense);
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, enter your date of birth in dd/mm/yyyy format, for example: 04/05/1988", 2000);
            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "Please, paste your photo url", 1000);
            userService.changeState(user, ChatState.ENTER_SURNAME);
        }
    }

    @Text(states = ChatState.ENTER_DATE_OF_BIRTH)
    public void handleEnterDateOfBirth(User user, @Text String date) {
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        Predicate<String> dateTester = d -> {
            Pattern pattern = Pattern.compile("^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$");
            Matcher matcher = pattern.matcher(d);
            return matcher.matches();
        };

        if (dateTester.test(date)) {
            driverLicense.setDateOfBirth(date);
            driverLicenseService.save(driverLicense);
            sender.send(SenderAction.typingOn(user));
            sender.send(QuickReplies.builder()
                    .user(user)
                    .text("Cool, now i would like to know your driver category, so, please, select it")
                    .postback("A1", ButtonPostback.DRIVER_CATEGORY + "?category=A1")
                    .postback("A", ButtonPostback.DRIVER_CATEGORY + "?category=A")
                    .postback("B1", ButtonPostback.DRIVER_CATEGORY + "?category=B1")
                    .postback("B", ButtonPostback.DRIVER_CATEGORY + "?category=B")
                    .postback("C", ButtonPostback.DRIVER_CATEGORY + "?category=C")
                    .postback("D", ButtonPostback.DRIVER_CATEGORY + "?category=D")
                    .build());
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "You entered date with wrong format, please try again", 2000);
            userService.changeState(user, ChatState.ENTER_DATE_OF_BIRTH);
        }
    }

    @Postback(value = ButtonPostback.DRIVER_CATEGORY)
    public void handleEnterAllCategory(User user, @Param("category") String category) {
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        driverLicense.setDriverCategory(DriverCategory.valueOf(category).toString());
        driverLicenseService.save(driverLicense);
        sender.send(user, "Cool, now i would like to know your Email, so, please, enter it", 1000);
        userService.changeState(user, ChatState.ENTER_EMAIL);
    }

    @Text(states = ChatState.ENTER_EMAIL)
    public void handleUseFacebookEmail(User user, @Text String email) {
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        Predicate<String> emailTester = d -> {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+\\=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$");
            Matcher matcher = pattern.matcher(d);
            return matcher.matches();
        };

        if (emailTester.test(email)) {
            driverLicense.setEmail(email);
            driverLicense.setTimestamp(new Timestamp(new Date().getTime()));
            driverLicense.setFinished(true);
            driverLicenseService.save(driverLicense);
            sender.send(SenderAction.typingOn(user));
            sender.send(QuickReplies.builder()
                            .user(user)
                            .text("Thank you, now you can see your driver license")
                            .postback("See driver license", ButtonPostback.SEE_DRIVER_LICENSE)
                            .build(),
                    1000);
        } else {
            sender.send(SenderAction.typingOn(user));
            sender.send(user, "You entered email with wrong format, please try again", 2000);
            userService.changeState(user, ChatState.ENTER_EMAIL);
        }
    }

    @Postback(value = ButtonPostback.SEE_DRIVER_LICENSE)
    public void handleSeeDriverLicense(User user) {
        driverLicense = driverLicenseService.getDriverLicenseById(user.getUnfinishedDriverLicenseId());

        userService.changeState(user, ChatState.WELCOME);
        sender.send(GenericTemplate.builder()
                .user(user)
                .addElement(TemplateElement.builder()
                        .imageUrl(driverLicense.getImgUrl())
                        .title("There you can find your licenses")
                        .button(new WebButton("See new license", "http://localhost:8080/driver-license/" + driverLicense.getId()))
                        .button(new PostbackButton("See all licenses", ButtonPostback.SEE_ALL_DRIVER_LICENSES))
                        .build())
                .build());
    }

    @Postback(value = ButtonPostback.SEE_ALL_DRIVER_LICENSES)
    public void handleSeeAllDriverLicenses(User user) {
        List<DriverLicense> driverLicenseList = driverLicenseService.getAllDriverLicensesByUserId(user.getId());

        GenericTemplate.Builder builder = GenericTemplate.builder();
        for (DriverLicense license : driverLicenseList) {
            builder.addElement(TemplateElement.builder()
                    .imageUrl(license.getImgUrl())
                    .title(license.getUid())
                    .button(new WebButton("See license", "http://localhost:8080/driver-license/" + license.getId()))
                    .build()
            );
        }
        sender.send(builder
                .user(user)
                .build());
        userService.changeState(user, ChatState.WELCOME);
    }
}
