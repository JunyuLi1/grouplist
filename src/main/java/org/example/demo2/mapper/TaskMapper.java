package org.example.demo2.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.demo2.entity.TaskPriority;
import org.example.demo2.entity.TaskStatus;

import java.time.LocalDateTime;

@Mapper
public interface TaskMapper {
    int createTask(String taskId, String taskTitle, String description, Integer creatorId, TaskStatus taskStatus,
                   LocalDateTime dueDate, LocalDateTime createTime, LocalDateTime updateTime, LocalDateTime reminderTime,
                   TaskPriority priority);
    int updateTask(String taskId, String taskTitle, String description, Integer creatorId, TaskStatus taskStatus,
                   LocalDateTime dueDate, LocalDateTime createTime, LocalDateTime updateTime, LocalDateTime reminderTime,
                   TaskPriority priority);
    int add_collaborator(String taskId, Integer userId, String collaboratorRole);
}
