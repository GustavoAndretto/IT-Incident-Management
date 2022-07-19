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
    // Verifica se o email consta na DTO
    if (!userDTO.getEmail().isPresent()) {
      throw new Error("Email is null in UserDTO");
    }

    // Verifica se o usuário já existe no banco de dados [uniqueKey = email]
    if (repository.findByEmail(userDTO.getEmail().get()).isPresent()) {
      throw new Error("User already exists!");
    }

    // Valida se os campos obrigatórios da DTO não estão vazios.
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

    // Cria a entidade User que armazenará o novo usuário a ser inserido
    User user = new User();

    // Inicializa a entidade
    user.setName(userDTO.getName().get());
    user.setEmail(userDTO.getEmail().get());
    user.setPassword(passwordEncoder().encode(userDTO.getPassword().get()));
    user.setCpf(userDTO.getCpf().get());
    user.setPhone(userDTO.getPhone().get());

    // Salva a entidade e retorna a DTO para o controller
    return new UserDTO(repository.save(user));
  }

  public UserDTO update(UserDTO userDTO) {
     // Verifica se o email consta na DTO
    if (userDTO.getEmail().isEmpty())
      throw new Error("Email is empty");

     // Retorna o usuário pelo email caso exista no banco.
    User user = repository.findByEmail(userDTO.getEmail().get())
        .orElseThrow(() -> new Error(String.format("User not found with email: %s", userDTO.getEmail())));

    // Valida se os campos na DTO estão presentes, caso sim, atualiza na entidade
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

    // Salva o usuário atualizado
    repository.save(user);

    // Retorna a DTO do usuário atualizada
    return new UserDTO(user);
  }

  public User findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new Error("Id not found"));
  }

  public User findByEmail(String email) {
    return repository.findByEmail(email).orElseThrow(() -> new Error("Email not found"));
  }

  public List<User> findAll() {
    return repository.findAll();
  }

public void delete(String email) {
  Optional<User> user = repository.findByEmail(email);

  // Deleta caso encontre o usuário pelo email no banco de dados.
  if(user.isPresent()){
    repository.deleteById(user.get().getId());
  }
}
}
