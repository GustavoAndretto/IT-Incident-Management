package com.app.itsm.core.user;

import java.io.Serializable;
import java.util.Optional;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Optional<Long> id;

    private Optional<String> name;

    private Optional<String> email;

    private Optional<String> cpf;

    private Optional<String> phone;

    private Optional<String> password;

    private Optional<Integer> role;

    private Optional<Boolean> enabled;

    private Optional<Boolean> passwordExpired;

    public UserDTO() {
        this.id = Optional.empty();
        this.name = Optional.empty();
        this.email = Optional.empty();
        this.cpf = Optional.empty();
        this.phone = Optional.empty();
        this.password = Optional.empty();
        this.role = Optional.empty();
        this.enabled = Optional.empty();
        this.passwordExpired = Optional.empty();
    }

    public UserDTO(User user) {
        this.id = Optional.of(user.getId());
        this.name = Optional.of(user.getName());
        this.email = Optional.of(user.getEmail());
        this.cpf = Optional.of(user.getCpf());
        this.phone = Optional.of(user.getPhone());
        this.password = Optional.of(user.getPassword());
        this.role = Optional.of(user.getRole());
        this.enabled = Optional.of(user.getEnabled());
        this.passwordExpired = Optional.of(user.getPasswordExpired());
    }

    public User toUser() {
        return new User(this.name.get(), this.email.get(), this.cpf.get(), this.phone.get(), this.password.get(),
                this.role.get());
    }

    public Optional<Long> getId() {
        return id;
    }

    public void setId(Optional<Long> id) {
        this.id = id;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getCpf() {
        return cpf;
    }

    public void setCpf(Optional<String> cpf) {
        this.cpf = cpf;
    }

    public Optional<String> getPhone() {
        return phone;
    }

    public void setPhone(Optional<String> phone) {
        this.phone = phone;
    }

    public Optional<String> getPassword() {
        return password;
    }

    public void setPassword(Optional<String> password) {
        this.password = password;
    }

    public Optional<Integer> getRole() {
        return role;
    }

    public void setRole(Optional<Integer> role) {
        this.role = role;
    }

    public Optional<Boolean> getEnabled() {
        return enabled;
    }

    public void setEnabled(Optional<Boolean> enabled) {
        this.enabled = enabled;
    }

    public Optional<Boolean> getPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(Optional<Boolean> passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

}
