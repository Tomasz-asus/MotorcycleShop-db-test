package com.example.motorcycleshop.model;

public class RoleToUserForm {
    private String username;
    private final String roleName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public RoleToUserForm(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }
}
