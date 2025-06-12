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

    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      model.addAttribute("error", "Username não encontrado.");
      return "index.html";
    }

    User user = userOptional.get();
    if (user.isLocked()) {
      model.addAttribute("error", "Conta bloqueada. Entre em contato com o administrador da rede");
      return "index";
    }

    if (!user.getPassword().equals(password)) {
      user.setLoginAttempts(user.getLoginAttempts() + 1);

      if (user.getLoginAttempts() >= 10) {
        user.setLocked(true);
      }
      userRepository.save(user);

      if (user.isLocked()) {
        model.addAttribute("error", "Conta bloqueada. Entre em contato com o administrador da rede.");
      } else {
        model.addAttribute("error",
            "Username ou password incorretos. Tentativas restantes: " + (10 - user.getLoginAttempts()));
      }
      return "index";
    }

    session.setAttribute("loggedUser", user);
    user.setLoginAttempts(0);
    userRepository.save(user);

    if (user.getRole() == 1) {
      return "admin-dashboard";
    } else {
      return "dashboard";
    }
  }
}
