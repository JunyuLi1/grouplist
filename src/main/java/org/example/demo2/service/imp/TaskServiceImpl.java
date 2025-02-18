package org.example.demo2.service.imp;

import jakarta.annotation.Resource;
import org.example.demo2.entity.Response;
import org.example.demo2.entity.Task;
import org.example.demo2.mapper.TaskMapper;
import org.example.demo2.service.TaskService;
import org.example.demo2.utils.BloomFilter;
import org.example.demo2.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BloomFilter bloomFilter;

    @Override
    public Response<String> createTask(Task task) {
        String taskId = UUID.randomUUID().toString().replace("-", "");
        Integer userid = UserHolder.getUser().getId();
        int result = taskMapper.createTask(taskId, task.getTaskTitle(), task.getDescription(), userid,
                                    task.getTaskStatus(), task.getDueDate(), task.getCreateTime(), task.getUpdateTime(),
                                    task.getReminderTime(), task.getPriority());
        int addCollab_result = taskMapper.add_collaborator(taskId, userid, "Task_Creator");
        if(result == 0 || addCollab_result == 0) {
            return Response.fail("创建失败");
        }
        bloomFilter.add(taskId);
        return Response.success("创建成功");
    }

    @Override
    @Transactional
    public Response<String> updateTask(Task task) {
        // 使用分布式锁

        int result = taskMapper.updateTask(task.getTaskId(), task.getTaskTitle(), task.getDescription(), task.getCreatorId(),
                task.getTaskStatus(), task.getDueDate(), task.getCreateTime(), task.getUpdateTime(),
                task.getReminderTime(), task.getPriority());
        if(result == 0) {
            return Response.fail("更新失败");
        }
        String key = "task:info:" + task.getTaskId();
        stringRedisTemplate.delete(key);
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

    @Override
    public Response<String> queryAllTask(Integer userId) {
        String key = "task:user:" + userId; //此表结构为hash，key是taskID，value为task title
        // 从redis中查询任务记录
        Map<Object, Object> all_tasks = stringRedisTemplate.opsForHash().entries(key);
        // 判断是否在redis中存在
        if (!all_tasks.isEmpty()) {
            String result = all_tasks.toString();
            return Response.success(result);
        }
        // 如果redis中没有则查数据库
        List<Task> tasks = taskMapper.findTasksByUserId(userId);
        if(tasks.isEmpty()) {
            return Response.fail("用户没有任务记录");
        }

        // 成功查询完数据库并写入redis
        Map<Object, Object> returnValue = new HashMap<>();
        for(Task task : tasks){
            stringRedisTemplate.opsForHash().put(key, task.getTaskId(), task.getTaskTitle());
            returnValue.put(task.getTaskId(), task.getTaskTitle());
        }

        // 需要添加随机timeout防止缓存key失效导致缓存雪崩
        stringRedisTemplate.expire(key, 2, TimeUnit.MINUTES);


        return Response.success(returnValue.toString());
    }

    @Override
    public Response<String> queryTask(String taskId) {
        // 此时可能出现缓存击穿问题, 使用布隆过滤器判断是否存在，存在则继续下去
//        if(!bloomFilter.contains(taskId)) {
//            return Response.fail("没有该任务记录");
//        }

        // 从redis中查询任务记录
        String key = "task:info:" + taskId; //此表结构为hash，k-v是task的每个属性
        Map<Object, Object> taskInfo = stringRedisTemplate.opsForHash().entries(key);
        // 判断是否在redis中存在
        if (!taskInfo.isEmpty()) {
            String result = taskInfo.toString();
            return Response.success(result);
        }
        // 如果redis中没有则查数据库
        Task task = taskMapper.findSpecificTask(taskId);
        if(task == null) {
            return Response.fail("没有该条任务记录");
        }
        // 成功查询完数据库并写入redis
        Map<String, String> taskMap = new HashMap<>();
        taskMap.put("taskId", taskId);
        taskMap.put("taskTitle", task.getTaskTitle());
        taskMap.put("description", task.getDescription());
        taskMap.put("creatorId", String.valueOf(task.getCreatorId()));
        taskMap.put("taskStatus", String.valueOf(task.getTaskStatus()));
        taskMap.put("dueDate", String.valueOf(task.getDueDate()));
        taskMap.put("createTime", String.valueOf(task.getCreateTime()));
        taskMap.put("updateTime", String.valueOf(task.getUpdateTime()));
        taskMap.put("reminderTime", String.valueOf(task.getReminderTime()));
        taskMap.put("priority", String.valueOf(task.getPriority()));
        List<Integer> collaboratorIDs = taskMapper.findCollaborator(taskId);
        taskMap.put("collaborators", collaboratorIDs.toString());

        stringRedisTemplate.opsForHash().putAll(key, taskMap);

        // 需要添加随机timeout防止缓存key失效导致缓存雪崩
        stringRedisTemplate.expire(key, 2, TimeUnit.MINUTES);
        return Response.success(taskMap.toString());
    }
}
