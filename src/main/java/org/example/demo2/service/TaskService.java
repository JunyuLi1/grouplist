package org.example.demo2.service;


import org.example.demo2.entity.Response;
import org.example.demo2.entity.Task;



public interface TaskService {
    Response<String> createTask(Task task);
    Response<String> updateTask(Task task);
    Response<String> addCollaborator(String taskId, Integer userId, String collaboratorRole);
    Response<String> queryAllTask(Integer userId);
    Response<String> queryTask(String taskId);
    Response<String> queryFrequentTask(String taskId);
    Response<String> setFrequentTask(String taskId);
}
