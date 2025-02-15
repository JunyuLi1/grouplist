package org.example.demo2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public class Task {

    private String taskId;

    private String taskTitle;

    private String description;

    private Integer creatorId;

    private TaskStatus taskStatus = TaskStatus.PENDING; // 默认为pending
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reminderTime;

    private TaskPriority priority = TaskPriority.LOW; // 默认优先级为low

    public Task() {
    }

    public Task(String description, String taskTitle, String taskId, Integer creatorId, TaskStatus taskStatus,
                LocalDateTime dueDate, LocalDateTime createTime, LocalDateTime updateTime, LocalDateTime reminderTime,
                TaskPriority priority) {
        this.description = description;
        this.taskTitle = taskTitle;
        this.taskId = taskId;
        this.creatorId = creatorId;
        this.taskStatus = taskStatus;
        this.dueDate = dueDate;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.reminderTime = reminderTime;
        this.priority = priority;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
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

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
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

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                ", description='" + description + '\'' +
                ", creatorId=" + creatorId +
                ", taskStatus=" + taskStatus +
                ", dueDate=" + dueDate +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", reminderTime=" + reminderTime +
                ", priority=" + priority +
                '}';
    }
}
