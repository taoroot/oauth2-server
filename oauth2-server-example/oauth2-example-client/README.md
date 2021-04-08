# oauth2-client

OAuth2 客户端

如何获取 client_id 和 client_secret 请看
[oauth2-server](https://github.com/taoroot/oauth2-server)

这里没用 Security OAuth2 的 EnableOAuth2Client
因为他的实现都是基于session的,保持持久登录
因为我们的token是jwt,本身就是持久化功能, 所以实际上只是想获取的token返回给前端就好


# 接口

- 通过code换取

```http
POST /token?code=qp4rOb HTTP/1.1
```