package ru.javawebinar.topjava.model;

public class UserRoles {
    private Integer userId;
    private Role role;

    public UserRoles() {
    }

    public UserRoles(Integer userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
