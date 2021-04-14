package cn.flizi.auth.web;

import cn.flizi.auth.entity.Sms;
import cn.flizi.auth.entity.User;
import cn.flizi.auth.mapper.SmsMapper;
import cn.flizi.auth.mapper.UserMapper;
import cn.flizi.auth.properties.CaptchaProperties;
import cn.flizi.auth.properties.SmsProperties;
import cn.flizi.auth.security.AuthUser;
import cn.flizi.auth.security.CaptchaService;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Api(tags = "开放接口")
@RestController
public class RestApiController {
    private final SmsMapper smsMapper;
    private final UserMapper userMapper;
    private final CaptchaService captchaService;
    private final CaptchaProperties captchaProperties;
    private final SmsProperties smsProperties;

    private final static Executor executorPool = Executors.newSingleThreadExecutor();

    public RestApiController(SmsMapper smsMapper, UserMapper userMapper, CaptchaService captchaService, CaptchaProperties captchaProperties, SmsProperties smsProperties) {
        this.smsMapper = smsMapper;
        this.userMapper = userMapper;
        this.captchaService = captchaService;
        this.captchaProperties = captchaProperties;
        this.smsProperties = smsProperties;
    }

    @ApiOperation(value = "用户基本信息", notes = "{name: 用户id}")
    @GetMapping(value = "/user_base")
    public HashMap<String, Object> userBase() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", "SUCCESS");
        result.put("msg", "SUCCESS");
        result.put("name", userId);
        return result;
    }


    @ApiOperation(value = "用户详情,包含手机号", notes = "scope 必须包含 all, 内容: {name: userid, phone: phone}")
    @PreAuthorize("#oauth2.hasScope('all')")
    @GetMapping(value = "/user_info")
    public HashMap<String, Object> userInfo() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userMapper.loadUserByUserId(userId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", "SUCCESS");
        result.put("msg", "SUCCESS");
        result.put("name", userId);
        result.put("email", user.getEmail());
        result.put("phone", user.getPhone());
        AuthUser authentication = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        result.put("tenant_id", authentication.getTenant());
        return result;
    }

    @ApiOperation("图形验证码")
    @GetMapping(value = "/captcha")
    public void captcha(HttpServletResponse response, @RequestParam("key") String key) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("cache-Control", "no-cache, must-revalidate");
        response.addHeader("cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (ServletOutputStream out = response.getOutputStream()) {
            RandomGenerator randomGenerator = new RandomGenerator(captchaProperties.getBaseStr(), captchaProperties.getLength());
            LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);
            captcha.setGenerator(randomGenerator);
            captcha.createCode();
            captcha.write(out);
            out.flush();
            captchaService.set(key, captcha.getCode(), 120);
        }
    }

    @ApiOperation("发送手机验证码")
    @PostMapping(value = "/sms")
    public HashMap<String, Object> sms(@RequestParam String phone) {
        HashMap<String, Object> result = new HashMap<>();
        if (!captchaProperties.getEnable()) {
            result.put("code", "FAIL");
            result.put("msg", "SMS_ENABLE_IS_FALSE");
            return result;
        }

        Sms sms = smsMapper.getCodeByPhone(phone);
        if (sms != null && new Date().getTime() - sms.getCreateTime().getTime() < 60 * 1000) {
            result.put("code", "FAIL");
            result.put("msg", "发送失败,请稍后重试");
            return result;
        }
        sms = new Sms();
        String code = String.format("%04d", new Random().nextInt(10000));
        sms.setCode(code);
        sms.setPhone(phone);
        smsMapper.insert(sms);
        executorPool.execute(() -> sendSms(phone, "+86", code));
        result.put("code", "SUCCESS");
        result.put("msg", "发送成功");
        return result;
    }

    public void sendSms(String phone, String area, String code) {
        String SECRET_ID = smsProperties.getSecretId();
        String SECRET_KEY = smsProperties.getSecretKey();
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        SmsClient client = new SmsClient(cred, "", clientProfile);
        SendSmsRequest req = new SendSmsRequest();
        String appid = smsProperties.getAppId();
        req.setSmsSdkAppid(appid);
        String sign = smsProperties.getSign();
        req.setSign(sign);
        req.setSessionContext(code);
        String templateID = smsProperties.getTemplateId();
        req.setTemplateID(templateID);
        String[] phoneNumbers = {area + phone};
        req.setPhoneNumberSet(phoneNumbers);
        String[] templateParams = {code};
        req.setTemplateParamSet(templateParams);
        try {
            SendSmsResponse res = client.SendSms(req);
            SendSmsResponse.toJsonString(res);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}