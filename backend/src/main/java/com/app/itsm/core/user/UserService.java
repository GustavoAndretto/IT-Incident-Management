package com.app.itsm.core.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  UserRepository repository;

  private BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public UserDTO create(UserDTO userDTO) {
    if (!userDTO.getEmail().isPresent()) {
      throw new Error("Email is null in UserDTO");
    }

    if (repository.findByEmail(userDTO.getEmail().get()).isPresent()) {
      throw new Error("User already exists!");
    }

    if (userDTO.getName().isEmpty()) {
      throw new Error("Name cannot be null!");
    } else if (userDTO.getEmail().isEmpty()) {
      throw new Error("Email cannot be null!");
    } else if (userDTO.getPassword().isEmpty()) {
      throw new Error("Password cannot be null!");
    } else if (userDTO.getCpf().isEmpty()) {
      throw new Error("CPF cannot be null!");
    } else if (userDTO.getPhone().isEmpty()) {
      throw new Error("Phone cannot be null!");
    }

    User user = new User();

    user.setName(userDTO.getName().get());
    user.setEmail(userDTO.getEmail().get());
    user.setPassword(passwordEncoder().encode(userDTO.getPassword().get()));
    user.setCpf(userDTO.getCpf().get());
    user.setPhone(userDTO.getPhone().get());

    return new UserDTO(repository.save(user));
  }

  public UserDTO update(UserDTO userDTO) {
    if (userDTO.getEmail().isEmpty())
      throw new NullPointerException("Email is empty");

    User user = repository.findByEmail(userDTO.getEmail().get())
        .orElseThrow(() -> new Error(String.format("User not found with email: %s", userDTO.getEmail())));

    if (userDTO.getName().isPresent())
      user.setName(userDTO.getName().get());

    if (userDTO.getEmail().isPresent())
      user.setEmail(userDTO.getEmail().get());

    if (userDTO.getPassword().isPresent()) {
      user.setPassword(passwordEncoder().encode(userDTO.getPassword().get()));
    }

    if (userDTO.getCpf().isPresent())
      user.setCpf(userDTO.getCpf().get());

    if (userDTO.getPhone().isPresent())
      user.setPhone(userDTO.getPhone().get());

    if (userDTO.getRole().isPresent())
      user.setRole(userDTO.getRole().get());

    if (userDTO.getEnabled().isPresent())
      user.setEnabled(userDTO.getEnabled().get());

    if (userDTO.getPasswordExpired().isPresent())
      user.setPasswordExpired(userDTO.getPasswordExpired().get());

    repository.save(user);

    return new UserDTO(user);
  }

  public User findById(Long id) {
    return repository.findById(id).get();
  }

  public User findByEmail(String email) {
    return repository.findByEmail(email).get();
  }

  public List<User> findAll() {
    return repository.findAll();
  }

public void delete(String email) {
  Optional<User> user = repository.findByEmail(email);

  if(user.isPresent()){
    repository.deleteById(user.get().getId());
  }
}
}
