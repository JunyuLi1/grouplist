package org.example.demo2.controller;

import org.example.demo2.entity.Response;
import org.example.demo2.entity.Task;
import org.example.demo2.entity.dto.TaskDTO;
import org.example.demo2.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create_task")
    public Response<String> create_task(@RequestBody TaskDTO taskDTO) {
        Task newTask = new Task();
        BeanUtils.copyProperties(taskDTO, newTask);
        return taskService.createTask(newTask); //调用service层相关函数
    }

    @PostMapping("/update_task")
    public Response<String> update_task(@RequestBody TaskDTO taskDTO) {
        Task newTask = new Task();
        BeanUtils.copyProperties(taskDTO, newTask);
        return taskService.updateTask(newTask); //调用service层相关函数
    }

    @PostMapping("/add_collaborator")
    public Response<String> add_collaborator(@RequestBody TaskDTO taskDTO) {
        return taskService.addCollaborator(taskDTO.getTaskId(), taskDTO.getCollaborator(), taskDTO.getRole()); //调用service层相关函数
    }
}
