package com.example.painelvpn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.painelvpn.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

  @GetMapping("/dashboard")
  public String dashboard(HttpSession session, Model model) {
    User user = (User) session.getAttribute("loggedUser");
    if (user == null) {
      return "index"; // not logged in
    }
    return "dashboard";
  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "index";
  }
}
