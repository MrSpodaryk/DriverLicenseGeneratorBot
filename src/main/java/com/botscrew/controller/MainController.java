package com.botscrew.controller;

import com.botscrew.service.DriverLicenseTemplateService;
import com.botscrew.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @Autowired
    UserService userService;
    @Autowired
    DriverLicenseTemplateService driverLicenseTemplateService;

    @GetMapping("template/{id}")
    public String getTemplate(@PathVariable Integer id, Model model) {
        model.addAttribute("license", driverLicenseTemplateService.getTemplateById(
                userService.findUserById(id).getUnfinishedTemplateId()));
        return "template";
    }
}
