# spring-security-oauth2 架构分析

[官方文档](https://projects.spring.io/spring-security-oauth/docs/oauth2.html)

本文分为两部分内容, 一部分是讲 OAuth 2.0 provider (**OAuth2 服务提供者**), 另一部分是讲 OAuth 2.0 client (**OAuth2 客户端**)

## OAuth 2.0 provider

OAuth 2.0 提供程序机制负责公开 OAuth 2.0 保护的资源。 并且需要提供一套配置, 负责确认哪些 `OAuth 2.0 Client` 可以独立地或代表用户访问受保护的资源。
`OAuth 2.0 Provider` 通过管理和验证用于访问受保护资源的 `OAuth 2.0 Token` 来做到这一点。
在适用的情况下，`OAuth 2.0 Provider` 还必须提供一个接口，让用户确认客户端可以被授予访问受保护资源的权限(即确认授权页面)。

### 分类

`OAuth 2.0 Provider`根据所扮演的角色, 被划分为**授权服务**和**资源服务**, 虽然这些有时存在于同一个应用程序中,
使用`Spring Security OAuth`可以选择将它们拆分为两个应用程序,或者是共享一个**授权服务**,多个**资源服务**.

访问令牌(Token)获取请求由`Spring MVC Controller`处理，对受保护资源的访问由**Spring Security 过滤器链**处理。

为了实现 OAuth 2.0 **授权服务器**，**Spring Security 过滤器链**中需要以下请求地址:

- AuthorizationEndpoint 请求验证地址. 默认URL: /oauth/authorize.
- TokenEndpoint 获取Token地址. 默认URL: /oauth/token.

为了实现 OAuth 2.0 **资源服务器**需要以下过滤器:

- OAuth2AuthenticationProcessingFilter 用于为对于当前请求加载`Authentication`(身份验证信息)

:::tip
对于所有OAuth 2.0提供程序特性，使用特殊的 `Spring OAuth @Configuration Adapter` 简化了配置
:::

## 授权服务器

在配置**授权服务器**时，必须考虑**客户端**用于从最终用户获取访问令牌的授权类型((authorization code, user credentials, refresh token))
服务器的配置用于提供客户端详细信息服务和令牌服务的实现，并全局启用或禁用该机制的某些方面.
但是注意,每个客户机都可以专门配置权限，以便能够使用某些授权机制和访问授权。
仅仅因为你的提供者被配置为支持*客户端授权*，并不意味着某个特定的客户被授权使用该授权类型,(上述是全局支持,如果要让某个客户端支持的,还得手动为配置上)。

`@EnableAuthorizationServer` 注释用于配置OAuth 2.0 **授权服务器**机制,并且还得存在 bean `AuthorizationServerConfigurer`.
默认里面的方法都是空实现

下面的特性被委托给 Spring 创建的独立配置器，并传递到 AuthorizationServerConfigurer 中:

- ClientDetailsServiceConfigurer: 定义客户端详细信息服务的配置程序。可以初始化客户端详细信息，也可以直接引用现有的存储
- AuthorizationServerSecurityConfigurer: 定义`令牌接口`上的安全配置
- AuthorizationServerEndpointsConfigurer: 定义`授权和令牌接口`以及`令牌服务`。

::: tips 授权码配置
提供程序配置的一个重要方面是向 OAuth 客户端供`授权码模式`的方式
OAuth 客户端通过将最终用户定向到一个授权页面来获得授权代码，用户可以在该页面输入凭证，从而导致从提供者授权服务器重定向到带有授权代码的OAuth客户端。
:::

### 管理客户端

ClientDetailsServiceConfigurer(来自AuthorizationServerConfigurer的回调)可用于定义客户端详细信息服务的内存或JDBC实现。
以下这具体字段:

- clientId: (必要) the client id.
- secret: (必要) the client secret
- scope: (授权范围). 如果是空,就是不受限制
- authorizedGrantTypes: 授权类型,默认空
- authorities: 权限(一般填写 security 权限)

:::tip
客户端详细信息可以在运行的应用程序中通过直接访问底层存储(例如JdbcClientDetailsService中的数据库表)
或通过ClientDetailsManager接口(ClientDetailsService的两个实现也都实现了这个接口)来更新。

注意:JDBC服务的模式并没有打包到库中(因为在实践中可能会使用太多的变体)，但是在github的[测试代码](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql)中有一个示例可以作为开始。
Managing Tokens
:::


### 管理令牌

`AuthorizationServerTokenServices`接口定义了管理`OAuth 2.0 令牌`所必需的操作。

- 当创建访问令牌时，必须存储身份验证，以便接受**访问令牌**的资源以后可以引用它。
- **访问令牌**用于加载用于授权其创建的身份验证。

当创建您的`AuthorizationServerTokenServices`实现时，您可能想要考虑使用 DefaultTokenServices，它存在多种策略可以插入来更改访问令牌的格式和存储。
默认情况下，它通过随机值创建令牌，并处理除它委托给`TokenStore`的令牌持久性之外的所有事情。
默认存储是内存中的实现，但也有其他一些可用的实现。下面是对它们的描述和讨论:

- 默认的`InMemoryTokenStore`于单台服务器说非常好(即低流量，在出现故障时不需要对备份服务器进行热交换)。
  大多数项目都可以从这里开始，并且可能在开发模式中以这种方式操作，以便在没有依赖关系的情况下轻松启动服务器。
- `JdbcTokenStore`是 JDBC版本，它将令牌数据存储在关系数据库中。
  如果可以在服务器之间共享数据库，则使用JDBC版本;如果只有一个服务器，则使用扩展同一服务器的实例;如果有多个组件，则使用授权和资源服务器。
  要使用JdbcTokenStore，您需要在类路径上使用“spring-jdbc”。
- `JSON Web Token (JWT) version` 是 JWT 版本的存储将有关授予的所有数据编码到令牌本身中(因此根本没有后端存储，这是一个显著的优势)。
  一个缺点是您不能轻易地撤销访问令牌，因此它们通常被授予较短的到期时间，而撤销是在刷新令牌处处理的。
  另一个缺点是，如果在令牌中存储大量用户凭据信息，令牌可能会变得非常大。
  JwtTokenStore并不是真正意义上的“存储”，因为它不持久化任何数据，但是它扮演着在`DefaultTokenServices`中转换令牌值和身份验证信息的相同角色。

:::tip
JDBC服务的模式没有与库打包在一起(因为在实践中可能有太多变体需要使用)，但是在github的[测试代码](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql)中有一个示例可以作为开始。
确保`@EnableTransactionManagement`，以防止在创建令牌时，客户端应用程序之间并发冲突。
还请注意，示例模式具有显式的主键声明——这些在并发环境中也是必需的。
:::

### JWT TOKEN

要使用JWT令牌，您需要在授权服务器中使用JwtTokenStore。
资源服务器还需要能够解码令牌，因此`JwtTokenStore`依赖于`JwtAccessTokenConverter`，并且授权服务器和资源服务器都需要相同的实现。
或者它需要与授权服务器中的私钥(签名密钥)相匹配的公钥(验证密钥)(公钥-私钥或非对称密钥)。公钥(如果可用)由授权服务器在/oauth/token_key端点上公开，
默认情况下使用访问规则`denyAll()`是安全的。你可以通过在AuthorizationServerSecurityConfigurer中注入一个标准的SpEL表达式来打开它。`permitAll()`可能足够了，因为它是一个公钥)。
要使用JwtTokenStore，您需要在类路径中使用`Spring -security-jwt`(您可以在与Spring OAuth相同的github存储库中找到它，但发布周期不同)。

### 授权类型

`AuthorizationEndpoint`支持的授权类型可以通过`AuthorizationServerEndpointsConfigurer`配置。
默认情况下，支持除密码以外的所有授予类型(有关如何启用它的详细信息，请参阅下面的内容)。下列属性会影响授权类型:

- authenticationManager: 通过注入`AuthenticationManager`来开启**密码授权模式**。
- userDetailsService: 如果您注入一个UserDetailsService，或者以任何方式配置了一个UserDetailsService(例如在`GlobalAuthenticationConfigurerAdapter`中)，那么`刷新令牌授予`将包含对用户细节的检查，以确保帐户仍然是活动的
- authorizationCodeServices: 定义用于`授权码模式`的授权代码服务(AuthorizationCodeServices的实例)。
- implicitGrantService: 管理`imlpicit`授予期间的状态。
- tokenGranter: TokenGranter (完全控制授予并忽略上面的其他属性)

### 接口地址

`AuthorizationServerEndpointsConfigurer` 有一个pathMapping()方法。它有两个参数:

- 默认地址
- 自定义地址(必须以 '/' 开头)

:::tip
/oauth/authorize 默认授权地址
/oauth/token  获取token地址
/oauth/confirm_access 用户确认授权页面
/oauth/error 错误界面
/oauth/check_token 解码token,获取token对应的全部信息
/oauth/token_key 公钥令牌验证, 如果使用JWT令牌
:::

注意: 应该使用S pring安全保护授权端点/oauth/authorize (或其映射的替代方法)，以便只有经过身份验证的用户可以访问它。
例如使用一个标准的 Spring Security WebSecurityConfigurer:
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests().antMatchers("/login").permitAll().and()
        .authorizeRequests() // 除了登录接口,其他接口都得先登录后再访问
            .anyRequest().hasRole("USER")
}
```

:::tip
注意: 如果您的授权服务器也是资源服务器，那么还有另一个低优先级的安全过滤器链控制API资源。
对于那些被访问令牌保护的请求，你需要它们的路径不被面向用户的主过滤器链中的匹配，所以一定要包括一个请求匹配器，它只在上面的`WebSecurityConfigurer`中挑选出非api资源。
:::

### 个性化界面

大多数授权服务器端点主要由机器使用，但是有一些资源需要界面, 例如: /oauth/confirm_access, /oauth/error
它们是使用框架中的`whitelabel`实现提供的，因此授权服务器的大多数实际实例都希望提供它们自己的实例，以便它们能够控制样式和内容。
您所需要做的就是为这些端点提供一个带有 @RequestMapping的`Spring MVC Controller`，并且框架默认值在 dispatcher 中具有较低的优先级。

在`/oauth/confirm_access`端点，你可以期待一个授权请求绑定到会话，携带所有需要从用户寻求批准的数据(默认实现是WhitelabelApprovalEndpoint)
您可以从该请求中获取所有数据并以您喜欢的方式呈现它，然后用户需要做的就是用批准或拒绝授权的信息返回到/oauth/authorize。
请求参数被直接传递到`AuthorizationEndpoint`中的`UserApprovalHandler`，因此您可以根据自己的喜好解释数据。
默认的`UserApprovalHandler`取决于您是否在您的`AuthorizationServerEndpointsConfigurer`中提供了一个ApprovalStore, 有可能是
TokenStoreUserApprovalHandler, 或者是 ApprovalStoreUserApprovalHandler
标准审批处理程序接受以下操作:
TokenStoreUserApprovalHandler: 一个通过`user_oauth_approval`的简单的yes/no决定等于“真”或“假”。
ApprovalStoreUserApprovalHandler: 一组范围。 参数键与“*”等于被请求的范围。 参数的值可以是“true”或“approved”(如果用户批准了授权)，否则用户将被视为拒绝了该范围。 如果至少批准了一个范围，则授权成功。

:::tip
注意:不要忘记在为用户呈现的表单中包含CSRF保护。
Spring Security在默认情况下期待一个名为“_csrf”的请求参数(它在请求属性中提供该值)。
有关这方面的更多信息，请参阅Spring Security用户指南，或者参阅whitelabel实现以获得指导。
:::


## 资源服务器

Configuring Client Details
