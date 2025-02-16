package org.example.demo2.entity.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.demo2.entity.TaskPriority;
import org.example.demo2.entity.TaskStatus;

import java.time.LocalDateTime;

public class TaskDTO {
    private String taskTitle;
    private String description;
    private Integer collaboratorID;
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
    private String taskId;

    public TaskDTO() {
    }

    public TaskDTO(String taskTitle, String description, Integer collaboratorID, TaskStatus taskStatus,
                   LocalDateTime dueDate, LocalDateTime createTime, LocalDateTime updateTime, LocalDateTime reminderTime,
                   TaskPriority priority, String role, String taskId) {
        this.taskTitle = taskTitle;
        this.description = description;
        this.collaboratorID = collaboratorID;
        this.taskStatus = taskStatus;
        this.dueDate = dueDate;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.reminderTime = reminderTime;
        this.priority = priority;
        this.role = role;
        this.taskId = taskId;
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

    public Integer getCollaboratorID() {
        return collaboratorID;
    }

    public void setCollaboratorID(Integer collaboratorID) {
        this.collaboratorID = collaboratorID;
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

}
