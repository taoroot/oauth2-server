package cn.flizi.cloud.common.security.core;

import cn.flizi.cloud.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandler {
    @Autowired
    private HttpServletRequest request;

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handler(HttpRequestMethodNotSupportedException e) {
        return R.errMsg("不支持 " + e.getMethod() + " 请求方式");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoHandlerFoundException.class)
    public R handler(NoHandlerFoundException e) {
        return R.errMsg("不存在 " + e.getRequestURL() + " 路径");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BindException.class)
    public R validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        String basePath = request.getScheme()
                + "://" + request.getServerName() + ":" + request.getServerPort() + "/swagger-ui/index.html";
        return R.error(basePath, message.toString());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public R<String> handler(Exception e) {
        return R.errMsg(e.getMessage());
    }
}