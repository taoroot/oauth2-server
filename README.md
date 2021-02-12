# 基于 Spring & Mybatis 的统一账号管理平台

环境依赖:

- JRE1.8
- Mysql5.7

启动流程:

- 配置数据库账号密码[**必须**]

会自动创建表

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
    url: jdbc:mysql://127.0.0.1:3306/auth?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
```

- 配置微信登录

公众平台用于在微信**内置浏览器**打开时登录

开放平台用于在非微信环境下**扫码登录**

```yaml
social:
  wx-mp: # 微信公众平台
    key: xxxx
    secret: xxx
  wx-open: # 微信开放平台
    key: xxxx
    secret: xxx
```

# 图形验证码配置

```yaml
captcha:
  enable: true # 启用
  base-str: '0123' #  随机字符
  length: 4  #  长度
```

# 手机登录配置(腾讯云)

```yaml
sms:
  enable: true # 启用
  secretId: 'xxxx'
  secretKey: 'xxxx'
  appId: 'xxx'
  sign: 'xxx'
  templateId: 'xxx'
```

# 编译 & 部署
```shell
mvn package
java -Dloader.path=./lib -jar auth-0.0.1.jar
```