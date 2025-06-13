package com.archive.articleArchive.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String home(Model model) {
        // Не должно быть добавления error-атрибутов!
        return "index.html"; // или "home" - ваш основной шаблон
    }
}