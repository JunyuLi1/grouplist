package org.example.demo2.service.imp;

import jakarta.annotation.Resource;
import org.example.demo2.entity.Response;
import org.example.demo2.entity.Task;
import org.example.demo2.mapper.TaskMapper;
import org.example.demo2.service.TaskService;
import org.example.demo2.utils.BloomFilter;
import org.example.demo2.utils.RedisLock;
import org.example.demo2.utils.UserHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Resource
    private RedissonClient redissonClient;

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
        // 使用redisson分布式锁, 并采用集群模式，确保只有一人在修改任务
        RLock lock = redissonClient.getLock("update_task"+task.getTaskId());
        boolean isLock = lock.tryLock();
        if(!isLock) {
            // 获取锁失败
            return Response.fail("该任务正在更新，请稍后重试");
        }
        try{
            // 获取锁成功，执行更新业务逻辑
            int result = taskMapper.updateTask(task.getTaskId(), task.getTaskTitle(), task.getDescription(), task.getCreatorId(),
                    task.getTaskStatus(), task.getDueDate(), task.getCreateTime(), task.getUpdateTime(),
                    task.getReminderTime(), task.getPriority());
            if(result == 0) {
                return Response.fail("更新失败");
            }
            String key = "task:info:" + task.getTaskId();
            stringRedisTemplate.delete(key); // 如果有缓存则删除旧有缓存数据，如果没有也不报错
            return Response.success("更新成功");
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional
    public Response<String> addCollaborator(String taskId, Integer userId, String collaboratorRole) {
        // 使用分布式锁确保只有一人在添加合作人员
        RedisLock lock = new RedisLock(stringRedisTemplate, "add_collaborator"+taskId);
        boolean isLock = lock.tryLock(300, TimeUnit.SECONDS);
        if(!isLock) {
            // 获取锁失败
            return Response.fail("该任务正在更新，请稍后重试");
        }
        try{
            int result = taskMapper.add_collaborator(taskId, userId, collaboratorRole);
            if(result == 0) {
                return Response.fail("创建失败");
            }
            return Response.success("创建成功");
        }
        finally {
            lock.unlock();
        }
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

        // 添加随机timeout防止缓存key失效导致缓存雪崩
        Random rand = new Random();
        int randomTimeout = rand.nextInt(10);
        stringRedisTemplate.expire(key, 15+randomTimeout, TimeUnit.MINUTES);


        return Response.success(returnValue.toString());
    }

    @Override
    public Response<String> queryTask(String taskId) {
        // TODO:此时可能出现缓存穿透问题, 使用布隆过滤器判断是否存在，存在则继续下去, 为了测试方便将其注释
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

        // 随机timeout防止缓存key失效导致缓存雪崩
        Random rand = new Random();
        int randomTimeout = rand.nextInt(10);
        stringRedisTemplate.expire(key, 15+randomTimeout, TimeUnit.MINUTES);

        return Response.success(taskMap.toString());
    }

    @Override
    public Response<String> setFrequentTask(String taskId) {
        String key = "task:info:" + taskId;
        stringRedisTemplate.delete(key); // 确保使短期key失效
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
        return Response.success("设置成功");
    }

    @Override
    public Response<String> queryFrequentTask(String taskId) {
        // 此函数针对的是缓存击穿，也就是热点key的问题

        // 这个需要对应一个set frequent task函数，也就是将task取出来放到redis作为永久数据

        String key = "task:info:" + taskId; //此表结构为hash，k-v是task的每个属性
        Map<Object, Object> taskInfo = stringRedisTemplate.opsForHash().entries(key);
        // 判断是否在redis中存在
        if (taskInfo.isEmpty()) {
            // redis没有说明未命中
            return Response.fail("没有该条任务记录");
        }
        String expire_key = "expire_time:task:" + taskId;
        String storedTimestamp = stringRedisTemplate.opsForValue().get(expire_key);
        if (storedTimestamp != null) {
            LocalDateTime cachedTime = LocalDateTime.parse(storedTimestamp);
            if (cachedTime.isAfter(LocalDateTime.now())) {
                // 没过期，则直接返回
                return Response.success(taskInfo.toString());
            } else {
                // 缓存已过期，尝试获取锁
                RedisLock lock = new RedisLock(stringRedisTemplate, "query_frequentTask"+taskId);
                boolean isLock = lock.tryLock(300, TimeUnit.SECONDS);
                if(!isLock) {
                    // 获取锁失败，说明有人正在更新缓存，则先返回旧有数据
                    return Response.success(taskInfo.toString());
                }
                try{
                    // TODO：获取锁成功，异步开启独立线程将任务信息写入redis
                    return Response.success(taskInfo.toString());
                }
                finally {
                    lock.unlock();
                }

            }
        }
        return Response.fail("无法从缓存中获取该任务的过期时间，请检查该任务的合理性");
    }
}
