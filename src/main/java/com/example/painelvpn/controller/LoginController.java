package com.example.painelvpn.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.painelvpn.model.User;
import com.example.painelvpn.persistence.IUserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

  @Autowired
  private IUserRepository userRepository;

  @GetMapping("/")
  public String renderIndex() {
    return "index";
  }

  @PostMapping("/user-login")
  public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {

    if (username == null || username.isBlank()) {
      model.addAttribute("error", "Informe o username para realizar o processo de entrada.");
      return "index.html";
    }

    if (password == null || password.isBlank()) {
      model.addAttribute("error", "Informe o password para realizar o processo de entrada.");
      return "index.html";
    }

    Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
    if (user.isEmpty()) {
      model.addAttribute("error", "Username ou password estão incorretos.");
      return "index.html";
    } else {
      session.setAttribute("loggedUser", user.get());
      if (user.get().getRole() == 1) {
        return "admin-dashboard";
      } else {
        return "dashboard";
      }
    }
  }
}
