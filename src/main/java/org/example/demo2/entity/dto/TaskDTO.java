package org.example.demo2.entity.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.demo2.entity.TaskPriority;
import org.example.demo2.entity.TaskStatus;

import java.time.LocalDateTime;

public class TaskDTO {
    private String taskTitle;
    private String description;
    private Integer creatorId;
    private TaskStatus taskStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reminderTime;
    private TaskPriority priority;
    private String role;
    private Integer collaborator;
    private String taskId;

    public TaskDTO() {
    }

    public TaskDTO(String taskTitle, String taskId, Integer collaborator, String role, TaskPriority priority,
                   LocalDateTime reminderTime, LocalDateTime updateTime, LocalDateTime createTime,
                   LocalDateTime dueDate, TaskStatus taskStatus, Integer creatorId, String description) {
        this.taskTitle = taskTitle;
        this.taskId = taskId;
        this.collaborator = collaborator;
        this.role = role;
        this.priority = priority;
        this.reminderTime = reminderTime;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.dueDate = dueDate;
        this.taskStatus = taskStatus;
        this.creatorId = creatorId;
        this.description = description;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Integer collaborator) {
        this.collaborator = collaborator;
    }
}
