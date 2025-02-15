package org.example.demo2.service.imp;

import org.example.demo2.entity.Response;
import org.example.demo2.entity.Task;
import org.example.demo2.mapper.TaskMapper;
import org.example.demo2.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Response<String> createTask(Task task) {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        int result = taskMapper.createTask(taskId, task.getTaskTitle(), task.getDescription(), task.getCreatorId(),
                                    task.getTaskStatus(), task.getDueDate(), task.getCreateTime(), task.getUpdateTime(),
                                    task.getReminderTime(), task.getPriority());
        int addCollab_result = taskMapper.add_collaborator(taskId, task.getCreatorId(), "Task_Creator");
        if(result == 0 || addCollab_result == 0) {
            return Response.fail("创建失败");
        }
        return Response.success("创建成功");
    }

    @Override
    public Response<String> updateTask(Task task) {
        int result = taskMapper.updateTask(task.getTaskId(), task.getTaskTitle(), task.getDescription(), task.getCreatorId(),
                task.getTaskStatus(), task.getDueDate(), task.getCreateTime(), task.getUpdateTime(),
                task.getReminderTime(), task.getPriority());
        if(result == 0) {
            return Response.fail("更新失败");
        }
        return Response.success("更新成功");
    }

    @Override
    public Response<String> addCollaborator(String taskId, Integer userId, String collaboratorRole) {
        int result = taskMapper.add_collaborator(taskId, userId, collaboratorRole);
        if(result == 0) {
            return Response.fail("创建失败");
        }
        return Response.success("创建成功");
    }
}
