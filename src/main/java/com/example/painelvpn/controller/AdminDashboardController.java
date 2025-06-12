package com.example.painelvpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.painelvpn.model.User;
import com.example.painelvpn.persistence.IUserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminDashboardController {

  @Autowired
  private IUserRepository userRepository;

  @GetMapping("/admin-dashboard")
  public String renderAdminDashboard(HttpSession session, Model model) {
    User user = (User) session.getAttribute("loggedUser");
    if (user == null || user.getRole() != 1) {
      return "index";
    }

    model.addAttribute("users", userRepository.findAll());
    return "admin-dashboard";
  }

  @GetMapping("/registrar-usuario")
  public String renderRegisterUser(HttpSession session, Model model) {
    User user = (User) session.getAttribute("loggedUser");
    if (user == null || user.getRole() != 1) {
      return "index";
    }

    model.addAttribute("user", new User());
    return "registrar-usuario";
  }

  @PostMapping("/registrar-usuario")
  public String registerUser(@ModelAttribute User newUser, Model model) {
    userRepository.save(newUser);
    model.addAttribute("users", userRepository.findAll());
    return "admin-dashboard";
  }
}