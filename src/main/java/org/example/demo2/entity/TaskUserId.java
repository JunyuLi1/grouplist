package org.example.demo2.entity;


import java.io.Serializable;
import java.util.UUID;


public class TaskUserId implements Serializable {
    private Integer userId;
    private UUID taskId;

    public TaskUserId() {
    }

    public TaskUserId(Integer userId, UUID taskId) {
        this.userId = userId;
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}
