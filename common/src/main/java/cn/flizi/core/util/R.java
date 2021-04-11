
package cn.flizi.core.util;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String OK = "SUCCESS";
    public static final String ERROR = "ERROR";

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    public static <T> R<T> ok() {
        return instance(null, OK, null);
    }

    public static <T> R<T> ok(T data) {
        return instance(data, OK, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return instance(data, OK, msg);
    }

    public static <T> R<T> okMsg(String msg) {
        return instance(null, OK, msg);
    }

    public static <T> R<T> error() {
        return instance(null, ERROR, null);
    }

    public static <T> R<T> error(T data) {
        return instance(data, ERROR, null);
    }

    public static <T> R<T> error(T data, String msg) {
        return instance(data, ERROR, msg);
    }

    public static <T> R<T> errMsg(String msg) {
        return instance(null, ERROR, msg);
    }

    public static <T> R<T> instance(T data, String code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
