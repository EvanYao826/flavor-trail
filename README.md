# 寻味中国 - Flavor Trail

> 爱吃才会赢 · 中国美食地图

一款探索中国美食文化的综合应用，帮助用户发现各地特色美食、学习烹饪技巧，并通过AI智能推荐获得个性化美食建议。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.x
- **语言**: Java 21
- **数据库**: MySQL 8.0 + Flyway迁移
- **ORM**: MyBatis Plus
- **缓存**: Redis
- **认证**: JWT + Spring Security
- **API文档**: Swagger 3.0 (Knife4j)
- **AI**: Spring AI (可扩展)

### Android端
- **框架**: Kotlin + Jetpack Compose
- **网络**: Retrofit + OkHttp
- **依赖注入**: Hilt
- **地图**: 高德地图SDK
- **数据存储**: DataStore

### Web端
- **框架**: Vue 3 + TypeScript
- **UI**: Element Plus
- **状态管理**: Pinia
- **地图**: ECharts

## 项目结构

```
flavor-trail/
├── flavor-trail-server/    # 后端服务
│   ├── src/main/java/com/flavor/trail/
│   │   ├── controller/     # REST API控制器
│   │   ├── service/        # 业务逻辑层
│   │   ├── mapper/         # 数据访问层
│   │   ├── entity/         # 实体类
│   │   ├── dto/            # 数据传输对象
│   │   ├── vo/             # 视图对象
│   │   ├── config/         # 配置类
│   │   ├── ai/             # AI模块
│   │   │   ├── router/     # 意图路由
│   │   │   └── tools/      # AI工具
│   │   └── security/       # 安全认证
│   └── src/main/resources/
│       ├── db/migration/   # Flyway迁移脚本
│       └── prompts/        # AI提示词模板
├── flavor-trail-app/       # Android应用
└── flavor-trail-web/       # Web前端
```

## 功能模块

| 模块 | 功能描述 | 状态 |
|------|---------|------|
| 用户管理 | 注册、登录、个人信息、密码修改 | ✅ 已实现 |
| 省份管理 | 省份列表、探索状态 | ✅ 已实现 |
| 美食管理 | 列表、详情、搜索、收藏、浏览 | ✅ 已实现 |
| 探索进度 | 探索统计、进度更新 | ✅ 已实现 |
| AI对话 | 天气推荐、食材匹配、营养查询 | ✅ 已实现 |
| 排行榜 | 收藏榜、浏览榜 | ✅ 已实现 |

## 快速开始

### 环境要求

- JDK 21+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.8+

### 后端启动

1. **配置数据库连接**

编辑 `flavor-trail-server/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/flavor_trail?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

2. **创建数据库**

```sql
CREATE DATABASE flavor_trail CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **启动应用**

```bash
cd flavor-trail-server
mvn spring-boot:run
```

4. **访问API文档**

启动后访问：http://localhost:8080/doc.html

### API接口

| 模块 | 接口 | 方法 | 描述 |
|------|------|------|------|
| 认证 | `/api/auth/register` | POST | 用户注册 |
| 认证 | `/api/auth/login` | POST | 用户登录 |
| 用户 | `/api/user/profile` | GET | 获取个人信息 |
| 用户 | `/api/user/profile` | PUT | 修改个人信息 |
| 用户 | `/api/user/password` | PUT | 修改密码 |
| 省份 | `/api/provinces` | GET | 获取省份列表 |
| 省份 | `/api/provinces/{id}/foods` | GET | 获取省份美食 |
| 美食 | `/api/foods/{id}` | GET | 获取美食详情 |
| 美食 | `/api/foods/search` | GET | 搜索美食 |
| 美食 | `/api/foods/{id}/collect` | POST | 收藏/取消收藏 |
| 美食 | `/api/foods/{id}/view` | POST | 记录浏览 |
| 美食 | `/api/foods/ranking` | GET | 美食排行榜 |
| 美食 | `/api/foods/favorites` | GET | 我的收藏 |
| 探索 | `/api/explore/progress` | GET | 探索进度 |
| 探索 | `/api/explore/stats` | GET | 探索统计 |
| 对话 | `/api/chat/sessions` | POST | 创建会话 |
| 对话 | `/api/chat/sessions` | GET | 获取会话列表 |
| 对话 | `/api/chat/sessions/{id}/send` | POST | 发送消息(SSE) |

### 示例请求

**用户注册**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","nickname":"测试用户"}'
```

**用户登录**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

**获取省份列表**
```bash
curl -X GET http://localhost:8080/api/provinces \
  -H "Authorization: Bearer <your_token>"
```

**发送AI消息**
```bash
curl -X POST http://localhost:8080/api/chat/sessions/{sessionId}/send \
  -H "Authorization: Bearer <your_token>" \
  -H "Content-Type: application/json" \
  -d '{"content":"今天吃什么","city":"北京"}'
```

## AI功能

系统内置AI美食助手，支持以下功能：

1. **天气推荐**: 根据天气推荐适合的菜品
2. **食材匹配**: 根据可用食材推荐菜品
3. **营养查询**: 查询菜品营养信息
4. **自由对话**: 日常美食咨询

### 配置真实天气API

在 `application-dev.yml` 中添加高德天气API Key：

```yaml
weather:
  api:
    key: your_amap_api_key
```

## 数据库表结构

| 表名 | 说明 |
|------|------|
| `user` | 用户表 |
| `province` | 省份表 |
| `food` | 美食表 |
| `user_favorite` | 用户收藏表 |
| `explore_progress` | 探索进度表 |
| `chat_session` | 对话会话表 |
| `chat_message` | 对话消息表 |

## 开发计划

详见 [寻味中国-详细开发计划书.md](./寻味中国-详细开发计划书.md)

## License

MIT License