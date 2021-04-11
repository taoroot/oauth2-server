package cn.flizi.auth.config;

import cn.flizi.auth.properties.SocialProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * 定义全局变量
 */
@ControllerAdvice
public class GlobalConfig {

    @Value("${baseinfo.title}")
    private String appName;

    @Value("${baseinfo.beian}")
    private String beian;

    @Autowired
    private SocialProperties socialProperties;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("app_name", appName);
        model.addAttribute("beian", beian);
        model.addAttribute("wx_mp", socialProperties.getWxMp().getKey());
        model.addAttribute("wx_open", socialProperties.getWxOpen().getKey());
    }

}