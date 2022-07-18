package com.app.itsm.core.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  UserService service;

  @GetMapping(value = "/find", params = "id")
  User findById(@RequestParam Long id) {
    return service.findById(id);
  }

  @GetMapping(value = "/find", params = "email")
  User findByEmail(@RequestParam String email) {
    return service.findByEmail(email);
  }

  @PostMapping("/create")
  public UserDTO create(@RequestBody UserDTO userDTO) {
    return service.create(userDTO);
  }

  @PutMapping("/update")
  UserDTO update(@RequestBody UserDTO userDTO) {
    return service.update(userDTO);
  }

  @GetMapping("/list")
  List<User> list() {
    return service.findAll();
  }

  @DeleteMapping("/delete")
  void delete(@RequestParam String email) {
    service.delete(email);
  }

}
