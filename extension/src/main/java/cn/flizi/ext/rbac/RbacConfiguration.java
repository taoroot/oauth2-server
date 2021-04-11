package cn.flizi.ext.rbac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "cn.flizi.ext.rbac.mapper")
@ComponentScan("cn.flizi.ext.rbac")
public class RbacConfiguration {

}