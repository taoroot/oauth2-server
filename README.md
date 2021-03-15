# 基于 Spring & Mybatis 的统一账号管理平台

环境依赖:

- JRE1.8
- Mysql5.7

启动流程:

- 配置数据库

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

- 配置图形验证码

```yaml
captcha:
  enable: true # 启用
  base-str: '0123' #  随机字符
  length: 4  #  长度
```

- 配置手机验证码(腾讯云)

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


# 接口

{xxx} 表示是一个参数变量

## 图形验证码

```http
GET /captcha?key={timestamp} HTTP/1.1
```
## 手机验证码

```http
GET /sms?phone={phone} HTTP/1.1
```

## OAuth2 授权码
```http
GET /oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type={response_type}&scope={scope}&state={state} HTTP/1.1
```

## OAuth2 密码登录

扩展支持图形验证码,提高接口安全性

captchaKey: 图形验证码key, captchaCode: 图形验证码

```http
POST /oauth/token HTTP/1.1
Authorization: Basic {Base64({client_id};{secret})}
Content-Type: application/x-www-form-urlencoded

grant_type=password&username={username}&password={password}&scope={scope}&captchaKey={captchaKey}&captchaCode={captchaCode}
```

## OAuth2 授权码登录

```http
POST /oauth/token HTTP/1.1
Authorization: Basic {Base64({client_id};{secret})}
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code&code={code}&scope={scope}
```

## 手机号,微信登录

```
if (type == 'sms')
    code = '手机验证码'
if (type == 'WX_MP')
    code = '微信公众平台授权码'
if (type == 'WX_OPEN')
    code = '微信开放平台授权码'
```

```http
POST /oauth/token HTTP/1.1
Authorization: Basic {Base64({client_id};{secret})}
Content-Type: application/x-www-form-urlencoded

grant_type=social&type={type}&code={code}&scope={scope}
```


## 登录成功
```json
{
    "access_token": "access_token",
    "token_type": "bearer",
    "expires_in": 43199,
    "scope": "read_user",
    "user_id": "11"
}
```


## 获取用户信息

```http
GET /user_base HTTP/1.1
Authorization: Bearer {access_token}
```

scope!=all
```json
{
  "msg": "SUCCESS",
  "code": "SUCCESS",
  "name": "xxxxx"
}
```

scope==all
```json
{
  "msg": "SUCCESS",
  "code": "SUCCESS",
  "phone": "xxx",
  "name": "xxx",
  "email": "xxxx"
}
```