package org.example.demo2.entity;



public class TaskUser {
    private TaskUserId id;
    private String role;

    public TaskUser() {
    }

    public TaskUser(TaskUserId id, String role) {
        this.id = id;
        this.role = role;
    }

    public TaskUserId getId() {
        return id;
    }

    public void setId(TaskUserId id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
