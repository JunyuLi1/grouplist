CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100)
);

完成拦截器及会话管理：
    JWT 和 ThreadLocal 并不是替代关系，而是互补的
    JWT 让多个服务器共享用户身份信息，适用于分布式架构。
    ThreadLocal 让一个请求内的所有方法都能共享 userId，适用于单个服务器内部的请求上下文管理。
    解决会话过期问题：
    JWT 解析时检查是否过期，过期则不存入 ThreadLocal。
    ThreadLocal 仅在请求生命周期内生效，请求结束后自动清理。
    如果用户登出，使用 Redis 维护 JWT 黑名单，确保 Token 失效。
Redis：
    查询缓存，缓存团队任务列表，
    使用分布式锁（如Redisson）解决并发问题（如多个用户同时修改任务状态）。
Kafka： 
    异步发送任务状态变更通知（如邮件或消息推送）。 
    记录任务操作日志，便于审计和追踪。