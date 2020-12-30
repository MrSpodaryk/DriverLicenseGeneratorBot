package com.botscrew.controller;

import com.botscrew.service.DriverLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DriverLicenseService driverLicenseService;

    @GetMapping("driver-license/{id}")
    public String getTemplate(@PathVariable Integer id, Model model) {
        model.addAttribute("license", driverLicenseService.getDriverLicenseById(id));
        return "template";
    }
}
