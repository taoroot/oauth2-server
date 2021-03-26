# Spring OAuth2 前世今生

这里将 OAuth2.0 单独作为一个主题讲解, 主要是在 Spring 架构体系中, 有多个模块都提供了 OAuth2.0 支持, 虽然官方已经发出声明,现有的这些模块都将进入维护模式, 由 Spring Security 5 统一提供 (官方自己弃坑了,又挖了新坑)


## 现有模块

1. #### [org.springframework.social:spring-social-xxxx](https://github.com/spring-projects/spring-social)

   社交账号登录, 也就是第三方登录, 例如 微信, QQ, Github; 就是第三方应用提供了 oauth2 接口, 所以可以对接过来.
   这里的xxx,代表官方提供的一个具体实现,例如 facebook 叫 spring-social-facebook, 国内的应用到很少, 可能是历史原因,对oauth2的实现或多或少的不怎么标准.所以官方不提供.


2. #### [org.springframework.security.oauth:spring-security-oauth2](https://projects.spring.io/spring-social/)

   **spring-security-oauth** 项目(独立项目)的子模块, 提供资源服务器,认证服务器,客户端实现功能, 你可以用 `EnableResourceServer` 搭建资源服务器, `EnableAuthorizationServer` 搭建认证服务器. `EnableOAuth2Client` 搭建客户端 (spring-social功能一样);


3. #### [org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure](https://github.com/spring-projects/spring-security-oauth2-boot)

   Spring Boot 自动装配 `spring-security-oauth2`, 使得 `spring-security-oauth2` 简单上手使用, 提供 `EnableOAuth2Sso` 可以快速开发基于 OAuth2 实现 SSO 登录.


4. #### [org.springframework.cloud:spring-cloud-starter-oauth2](https://github.com/spring-cloud/spring-cloud-security)

   集成 `spring-security-oauth2-autoconfigure`, 固定版本号

   集成 `spring-cloud-security`, 适配 `spring-security-oauth2` 在 cloud 中使用, 例如 网关的适配, 服务间相互调用(负载均衡), 请求代理.


可以看出 [2], [3], [4] 具有包含关系, 而官方要停止维护的是 [2] `spring-security-oauth2`, 导致这套现有都受影响.
可以看出 `spring-security-oauth2` 这个模块代码量非常大, 没有按照功能拆分到子模块(你想用一个功能,就得引入全套代码)


## 新模块

1. #### [org.springframework.security:spring-security-oauth2-jose](https://github.com/spring-projects/spring-security/tree/master/oauth2/oauth2-jose)

   Spring Security 提供处理 token (jwt, jws) 功能


2. #### [org.springframework.security:spring-security-oauth2-client](https://github.com/spring-projects/spring-security/tree/master/oauth2/oauth2-client)

   Spring Security 提供处理**客户端**功能


3. #### [org.springframework.security:spring-security-oauth2-resource-server](https://github.com/spring-projects/spring-security/tree/master/oauth2/oauth2-resource-server)

   Spring Security 提供处理**资源服务器**功能


4. #### [org.springframework.security.experimental:spring-security-oauth2-authorization-server](https://github.com/spring-projects-experimental/spring-authorization-server)

   Spring Security 提供处理**认证服务器**功能


5. #### [org.springframework.boot:spring-boot-starter-oauth2-client](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters/spring-boot-starter-oauth2-client)

   Spring Boot 集成 `spring-security-oauth2-client`, 仅导入依赖


6. #### [org.springframework.boot:spring-boot-starter-oauth2-resource](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters/spring-boot-starter-oauth2-resource-server)

   Spring Boot 集成 `spring-security-oauth2-resource-server`, 仅导入依赖


[1],[2],[3] 都是 Spring Security 子模块, [5],[6],[7] 都是 Spring Boot 子模块, 除了[4] 证服务器不是官方亲儿子, 因为作者认为认证服务器不应该作为一个通用模块来实现, 而应该是一个独立项目存在,本来不打算去写这个了, 奈何社区呼吁很高,所以只好开了这个独立项目.

先来说说变化, 原来的 spring-security-oauth 项目(也就是提供 spring-security-oauth2 模块的) 将废弃掉(内部还有两个模块`spring-security-jwt`, `spring-security-oauth`). 将现有功能重构到 spring security 项目的子模块中. Spring Boot 提供了相关支持, 但是 Spring Cloud 还没有提供相关支持(虽然用的spring security 5, 但是目前还没用里面的oauth2 功能)

`Spring Framework, Spring Boot, Spring Cloud, Spring Security 这四在 Spring 体系中都是一等公民, 独立项目. 只是我们听的比较多的是前三者. Spring 是想把所有关于安全相关的都集成到security中,现在也确实这么干, 你用boot项目的,是可以做到只使用security完成oauth2相关的了,不过cloud就不行,至少 cloud 相关依赖还没出, 自己也尝试使用 spring cloud 搭建, 不过最终效果不理想.. 并没有解决动态代理相关问题
`
[项目地址 https://github.com/taroot/taox](https://github.com/taroot/taox)