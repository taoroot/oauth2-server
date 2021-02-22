package cn.flizi.auth.web;

import cn.flizi.auth.entity.Sms;
import cn.flizi.auth.entity.User;
import cn.flizi.auth.mapper.SmsMapper;
import cn.flizi.auth.mapper.UserMapper;
import cn.flizi.auth.properties.SocialProperties;
import cn.flizi.auth.security.AuthUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;

@Controller
public class MvcController {
    private final UserMapper userMapper;
    private final SmsMapper smsMapper;
    private final SocialProperties socialProperties;

    @Value("${spring.application.name}")
    private String appName;

    public MvcController(UserMapper userMapper, SmsMapper smsMapper, SocialProperties socialProperties) {
        this.userMapper = userMapper;
        this.smsMapper = smsMapper;
        this.socialProperties = socialProperties;
    }


    @GetMapping(value = "/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUser principal = (AuthUser) authentication.getPrincipal();
        User user = userMapper.loadUserByUserId(principal.getUsername());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("name", user.getUserId());
        model.addAttribute("wxOpenid", user.getWxOpenid());
        model.addAttribute("wxUnionid", user.getWxUnionid());
        return "index";
    }

    @GetMapping(value = "/login")
    public String login(Model model, @RequestParam(defaultValue = "false") Boolean wx) {
        model.addAttribute("app_name", appName);
        model.addAttribute("wx_mp", socialProperties.getWxMp().getKey());
        model.addAttribute("wx_open", socialProperties.getWxOpen().getKey());
        model.addAttribute("wx_auto", wx);
        return "login";
    }

    @GetMapping(value = "/signup")
    public String signup(Model model) {
        model.addAttribute("app_name", appName);
        model.addAttribute("wx_mp", socialProperties.getWxMp().getKey());
        model.addAttribute("wx_open", socialProperties.getWxOpen().getKey());
        return "signup";
    }

    @GetMapping(value = "/reset")
    public String reset() {
        return "reset";
    }

    @GetMapping(value = "/auth-redirect")
    public String authRedirect() {
        return "auth-redirect";
    }


    @PostMapping(value = "/signup")
    public String signup(@RequestParam Map<String, String> params, Model model) {
        String phone = params.get("phone");
        String code = params.get("code");
        String passwordStr = params.get("password");
        String passwordStr1 = params.get("password1");
        model.addAttribute("app_name", appName);
        model.addAttribute("wx_mp", socialProperties.getWxMp().getKey());
        model.addAttribute("wx_open", socialProperties.getWxOpen().getKey());


        if (!StringUtils.hasLength(phone)) {
            model.addAttribute("error", "手机号错误");
            return "signup";
        }
        if (!StringUtils.hasLength(code)) {
            model.addAttribute("error", "验证码错误");
            return "signup";
        }

        if (!StringUtils.hasLength(passwordStr) || passwordStr.length() < 6) {
            model.addAttribute("error", "密码至少6位");
            return "signup";
        }
        if (!passwordStr.equals(passwordStr1)) {
            model.addAttribute("error", "密码不一致");
            return "signup";
        }

        String password = "{bcrypt}" + new BCryptPasswordEncoder().encode(passwordStr);
        Sms sms = smsMapper.getCodeByPhone(phone);

        if (sms == null || !sms.getCode().equals(code)
                || new Date().getTime() - sms.getCreateTime().getTime() > 60 * 1000) {
            model.addAttribute("error", "验证码错误");
            return "signup";
        }

        User user = userMapper.loadUserByColumn("phone", phone);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setPassword(password);
            userMapper.insert(user);
        } else {
            userMapper.updatePassword(phone, password);
        }
        return "redirect:login?signup";
    }
}
