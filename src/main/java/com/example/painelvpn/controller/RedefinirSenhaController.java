package com.example.painelvpn.controller;

import com.example.painelvpn.model.User;
import com.example.painelvpn.persistence.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class RedefinirSenhaController {

  @Autowired
  private IUserRepository userRepository;

  @GetMapping("/redefinir-senha")
  public String renderPasswordResetSolicitation() {
    return "redefinir-senha";
  }

  @PostMapping("/redefinir-senha")
  public String generateResetLink(@RequestParam String username, Model model) {

    if (username == null || username.isBlank()) {
      model.addAttribute("message", "Informe o username para realizar o processo de redefinir uma nova senha.");
      return "redefinir-senha";
    }

    Optional<User> user = userRepository.findByUsername(username);

    if (user.isEmpty()) {
      model.addAttribute("message", "Usuário não encontrado com esse username.");
      return "redefinir-senha";
    } else {
      model.addAttribute("username", username);
      return "nova-senha";
    }
  }

  @PostMapping("/nova-senha")
  public String changePassword(@RequestParam String username, @RequestParam String password,
      @RequestParam String confirmPassword, Model model) {
    if (password == null || password.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
      model.addAttribute("error", "Por favor preencha ambos campos de senha.");
      model.addAttribute("username", username);
      return "nova-senha";
    }

    if (!password.equals(confirmPassword)) {
      model.addAttribute("error", "A senha informada não corresponde com a confirmação de senha");
      model.addAttribute("username", username);
      return "nova-senha";
    }

    if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$")) {
      model.addAttribute("error", "Senha não corresponde todas as regras definidas.");
      model.addAttribute("username", username);
      return "nova-senha";
    }

    Optional<User> userOptional = userRepository.findByUsername(username);
    User user = userOptional.get();
    user.setPassword(password);
    userRepository.save(user);
    model.addAttribute("message", "Sucesso, agora você pode realizar a autenticação.");
    return "index";
  }
}