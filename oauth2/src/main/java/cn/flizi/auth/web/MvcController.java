package cn.flizi.auth.web;

import cn.flizi.auth.entity.Sms;
import cn.flizi.auth.entity.User;
import cn.flizi.auth.mapper.SmsMapper;
import cn.flizi.auth.mapper.UserMapper;
import cn.flizi.auth.properties.SocialProperties;
import cn.flizi.auth.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@ApiIgnore
@Controller
public class MvcController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SmsMapper smsMapper;
    @Autowired
    private SocialProperties socialProperties;
    @Autowired
    private UserService userService;

    @ApiOperation("首页")
    @GetMapping(value = "/")
    public String index(Model model, @RequestParam(defaultValue = "false") Boolean sms) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userMapper.loadUserByUserId(authentication.getName());
        model.addAttribute("phone", "手机号: " + user.getPhone());
        model.addAttribute("name", "用户ID: " + user.getUserId());
        if (StringUtils.hasLength(user.getWxOpenid()) || StringUtils.hasLength(user.getWxUnionid())) {
            model.addAttribute("wx", "微信: 已绑定");
        } else {
            model.addAttribute("wx", "微信: 未绑定");
        }
        return "index";
    }

    @ApiOperation("登录页")
    @GetMapping(value = "/login")
    public String login(Model model) {
        return "login";
    }

    @ApiOperation("注册界面")
    @GetMapping(value = "/signup")
    public String signup(Model model) {
        return "signup";
    }


    @ApiOperation("绑定界面")
    @GetMapping(value = "/bind")
    public String bindSmsPost(@RequestParam(defaultValue = "false") Boolean wx, Model model) {
        model.addAttribute("wx_auto", wx);
        return "bind";
    }


    @ApiOperation("重置密码")
    @GetMapping(value = "/reset")
    public String reset(Model model) {
        return "reset";
    }


    @ApiOperation("授权页面")
    @GetMapping("/oauth/confirm_access")
    public String confirm(HttpServletRequest request, HttpSession session, Model model) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) session.getAttribute("authorizationRequest");
        if (authorizationRequest != null) {
            model.addAttribute("client_id", authorizationRequest.getClientId());
            model.addAttribute("scopes", authorizationRequest.getScope());
        }
        return "confirm_access";
    }

    @ApiOperation("微信 OAuth2 回调界面")
    @GetMapping(value = "/auth-redirect")
    public String authRedirect(Model model) {
        return "auth-redirect";
    }

    @ApiOperation("复用微信登录")
    @GetMapping(value = "/weixin-code")
    public String weixinCode(Model model) {
        return "weixin-code";
    }


    @ApiOperation("表单提交-手机号注册")
    @PostMapping(value = "/signup")
    public String signup(@RequestParam Map<String, String> params, Model model) {
        String phone = params.get("phone");
        String code = params.get("code");
        String passwordStr = params.get("password");
        String passwordStr1 = params.get("password1");

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

        Sms sms = smsMapper.getCodeByPhone(phone);

        // 短信过期检查
        if (sms == null || !sms.getCode().equals(code)
                || new Date().getTime() - sms.getCreateTime().getTime() > 60 * 1000) {
            model.addAttribute("error", "验证码错误");
            return "signup";
        }

        User user = userMapper.loadUserByColumn("phone", phone);
        String password = "{bcrypt}" + new BCryptPasswordEncoder().encode(passwordStr);
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


    @ApiOperation("微信绑定")
    @GetMapping(value = "/bind-wx-mp")
    public String bindWxMp(@RequestParam Map<String, String> params, Model model) {
        String code = params.get("code");
        if (!StringUtils.hasLength(code)) {
            model.addAttribute("error", "参数错误");
            return "redirect:/";
        }

        Map<String, String> map = userService.wxMpHandler(code);
        String unionId = map.get("unionid");
        String openId = map.get("openid");
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!StringUtils.hasLength(unionId)) {
            model.addAttribute("error", "参数错误");
            return "bind";
        }
        userMapper.updateWxOpenId(userId, openId);
        userMapper.updateWxUnionId(userId, unionId);
        return "redirect:/";
    }

    @ApiOperation("绑定开放平台")
    @GetMapping(value = "/bind-wx-open")
    public String bindWxOpen(@RequestParam Map<String, String> params, Model model) {
        String code = params.get("code");
        if (!StringUtils.hasLength(code)) {
            model.addAttribute("error", "参数错误");
            return "redirect:/";
        }
        String unionId = userService.wxOpenHandler(code);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!StringUtils.hasLength(unionId)) {
            model.addAttribute("error", "参数错误");
            return "bind";
        }
        userMapper.updateWxUnionId(userId, unionId);
        return "redirect:/";
    }

    @ApiOperation("绑定手机号码")
    @PostMapping(value = "/bind-sms")
    public String bindSms(@RequestParam Map<String, String> params, Model model) {
        String phone = params.get("phone");
        String code = params.get("code");
        model.addAttribute("wx_auto", false);

        if (!StringUtils.hasLength(phone)) {
            model.addAttribute("error", "手机号错误");
            return "bind";
        }

        if (!StringUtils.hasLength(code)) {
            model.addAttribute("error", "验证码错误");
            return "bind";
        }

        User user = userMapper.loadUserByColumn("phone", phone);

        if (user != null) {
            model.addAttribute("error", "手机号已被绑定");
            return "bind";
        }

        Sms sms = smsMapper.getCodeByPhone(phone);

        if (sms == null || !sms.getCode().equals(code)
                || new Date().getTime() - sms.getCreateTime().getTime() > 60 * 1000) {
            model.addAttribute("error", "验证码已过期");
            return "bind";
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userMapper.updatePhone(userId, phone);

        return "redirect:/";
    }
}
