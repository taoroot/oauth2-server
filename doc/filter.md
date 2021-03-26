# Filter

本文是对[官方文档](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-architecture)的翻译

Spring Security 的 Servlet 支持是基于 Servlet 过滤器的，因此首先了解过滤器的作用。

下图显示了单个 HTTP 请求处理程序的典型分层。

![filterchain](./pic/filterchain.png)

客户端向应用程序发送一个请求，容器创建一个包含过滤器的过滤器链和根据请求URI的路径处理 HttpServletRequest 的 Servlet。
在 Spring MVC 应用程序中，Servlet 是 DispatcherServlet 的一个实例。
最多一个 Servlet 可以处理一个 HttpServletRequest 和 HttpServletResponse, 但是可以使用多个过滤器

以下是过滤器调用顺序

```java
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    // 在应用程序的其余部分之前做些什么
    chain.doFilter(request, response); // 调用应用程序的其余部分
    // 在应用程序的剩余部分完成后做些什么
}
```

`从中可以看出过滤器的调用顺序是很重要的.`


## DelegatingFilterProxy

Spring 提供了一个名为 DelegatingFilterProxy 的过滤器实现，它允许在 Servlet 容器的生命周期和 Spring 的 ApplicationContext 之间嫁接桥梁。
Servlet 容器允许使用它自己的标准注册过滤器，但是它不知道 Spring 定义的 bean。
可以通过标准 Servlet 容器机制注册 DelegatingFilterProxy，将所有工作委托给实现过滤器的 Spring Bean。
下面是如何将 DelegatingFilterProxy 融入过滤器和过滤器链的图片。

![](./pic/delegatingfilterproxy.png)

DelegatingFilterProxy 从 ApplicationContext 中查找 Bean Filter0，然后调用 Bean Filter0。下面可以看到 DelegatingFilterProxy 的伪代码。

```java
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    // 懒加载注册为 Spring Bean 的过滤器
    // 例如上图 在 DelegatingFilterProxy 中，委托的 Bean Filter0 的一个实例
    Filter delegate = getFilterBean(someBeanName);
    // 将工作委托给 Spring Bean
    delegate.doFilter(request, response);
}
```

**委托 DelegatingFilterProxy 的另一个好处是，它允许延迟查找过滤器 bean 实例。这很重要，因为容器需要在启动之前注册过滤器实例。
但是，Spring 通常使用 ContextLoaderListener 来加载 Spring bean，直到注册过滤器实例之后才会执行。**


## FilterChainProxy

Spring Security 的 Servlet 支持包含在 FilterChainProxy 中。
FilterChainProxy 是 Spring Security 提供的一个特殊过滤器，它允许通过 SecurityFilterChain 委托许多过滤器实例。
由于 FilterChainProxy 是一个 Bean，因此通常被包装在 DelegatingFilterProxy 中。如下图:

![](./pic/filterchainproxy.png)

## SecurityFilterChain

FilterChainProxy 使用 SecurityFilterChain 来确定应该为此请求调用哪个 SpringSecurityFilter。
SecurityFilterChain 中的 SpringSecurityFilter 通常是 bean，但是它们是通过 FilterChainProxy 注册的，而不是通过 DelegatingFilterProxy。

`tip FilterChainProxy 比直接注册 Servlet 容器或委托 DelegatingFilterProxy 有着更多优势:

首先，它为所有 Spring Security 的 Servlet 支持提供了一个起点。因此，如果您试图对 Spring Security 的 Servlet 支持进行故障排除，那么在 FilterChainProxy 中添加一个调试点是一个很好的起点

其次，由于 FilterChainProxy 是使用 Spring Security 的核心，所以它可以执行必须执行的任务. 例如，清除 SecurityContext 以避免内存泄漏。它还应用 Spring Security 的 HttpFirewall 来保护应用程序免受某些类型的攻击。

最后，它在决定何时应该调用 SecurityFilterChain 方面提供了更大的灵活性。在 Servlet 容器中，仅根据 URL 调用筛选器。然而，FilterChainProxy 可以通过利用 RequestMatcher 接口基于 HttpServletRequest 中的任何内容来确定调用。
`

实际上，可以使用 FilterChainProxy 来确定应该使用哪个 SecurityFilterChain。这允许为不同的应用程序片提供完全独立的配置。

![](./pic/multi-securityfilterchain.png)

只有第一个匹配的 SecurityFilterChain 才会被调用。如果请求一个 /api/messages/ 的 URL，它将首先匹配 SecurityFilterChain-0 的 /api/** ，因此即使它也匹配 SecurityFilterChain-n，也只会调用 SecurityFilterChain-0。

如果请求的URL为/messages/，它将与SecurityFilterChain-0的 /api/** 不匹配，因此 FilterChainProxy 将继续尝试每个 SecurityFilterChain。假设没有其他实例，则将调用与 SecurityFilterChain-n 匹配的 SecurityFilterChain 实例。

`tip 请注意
SecurityFilterChain-0 只配置了三个 SpringSecurityFilter 实例。但是，SecurityFilterChain-n 配置了四个 SpringSecurityFilter。需要注意的是，每个 SecurityFilterChain 可以是惟一的，并且是隔离配置的。
事实上，如果应用程序希望 Spring security 忽略某些请求，SecurityFilterChain 可能没有安全过滤器。
`

# ExceptionTranslationFilter

处理安全异常

ExceptionTranslationFilter 允许将 AccessDeniedException 和 AuthenticationException 转成 HTTP 响应。

ExceptionTranslationFilter 作为一个 SpringSecurityFilter 插入到 FilterChainProxy 中, 需下图所示

![exceptiontranslationfilter](./pic/exceptiontranslationfilter.png)

1. 首先，ExceptionTranslationFilter 调用 FilterChain.doFilter(request, response) 来调用应用程序的其余部分。

2. 如果用户没有经过身份验证，或者它是 AuthenticationException 异常，则启动身份验证。

- SecurityContextHolder 被清除
- HttpServletRequest 保存在 RequestCache 中。当用户成功进行身份验证时，将使用 RequestCache 重试原始请求。
- AuthenticationEntryPoint 用于获取客户端请求凭据入口。例如，它可能重定向到登录页面或发送WWW-Authenticate头。前后端分离的情况下, 就直接提示未登录

3. 否则，如果它是 AccessDeniedException，那么访问被拒绝。调用 AccessDeniedHandler 处理被拒绝的访问。

```java
try {
    filterChain.doFilter(request, response); 
} catch (AccessDeniedException | AuthenticationException e) {
    if (!authenticated || e instanceof AuthenticationException) {
        startAuthentication(); 
    } else {
        accessDenied(); 
    }
}
```

## tip 内置过滤器

使用 SecurityFilterChain API 将安全过滤器插入到 FilterChainProxy 中。
过滤器的顺序很重要。通常没有必要知道 Spring Security 过滤器的顺序。然而，有时知道顺序是有益的

- ChannelProcessingFilter
- ConcurrentSessionFilter
- WebAsyncManagerIntegrationFilter
- SecurityContextPersistenceFilter
- HeaderWriterFilter
- CorsFilter
- CsrfFilter
- LogoutFilter
- OAuth2AuthorizationRequestRedirectFilter
- Saml2WebSsoAuthenticationRequestFilter
- X509AuthenticationFilter
- AbstractPreAuthenticatedProcessingFilter
- CasAuthenticationFilter
- OAuth2LoginAuthenticationFilter
- Saml2WebSsoAuthenticationFilter
- UsernamePasswordAuthenticationFilter
- ConcurrentSessionFilter
- OpenIDAuthenticationFilter
- DefaultLoginPageGeneratingFilter
- DefaultLogoutPageGeneratingFilter
- DigestAuthenticationFilter
- BearerTokenAuthenticationFilter
- BasicAuthenticationFilter
- RequestCacheAwareFilter
- SecurityContextHolderAwareRequestFilter
- JaasApiIntegrationFilter
- RememberMeAuthenticationFilter
- AnonymousAuthenticationFilter
- OAuth2AuthorizationCodeGrantFilter
- SessionManagementFilter
- ExceptionTranslationFilter
- FilterSecurityInterceptor
- SwitchUserFilter
