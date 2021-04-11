package cn.flizi.core.annition;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    public String value() default "";

    public int businessType() default 0;

    public int operatorType() default 0;

    public boolean saveParam() default true;
    public boolean saveResult() default false;
}
