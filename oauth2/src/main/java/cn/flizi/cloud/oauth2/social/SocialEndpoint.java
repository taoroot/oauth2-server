package cn.flizi.cloud.oauth2.social;

import cn.flizi.cloud.common.security.core.social.SocialClient;
import cn.flizi.cloud.common.security.core.social.SocialToken;
import cn.flizi.cloud.common.security.core.social.SocialUserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SocialEndpoint {
    private SocialDetailsService socialDetailsService;
    private HttpServletRequest request;

    @ApiOperation("社交账号绑定")
    @GetMapping(value = "/social/bind/{type}/{appId}")
    public String bindSocial(@PathVariable String type,
                             @PathVariable String appId,
                             Model model) {
        SocialClient socialClient = socialDetailsService.getClient(type, appId);

        SocialToken socialToken = socialClient.getToken(request);
        SocialUserInfo socialUserInfo = socialClient.getUserInfo(socialToken);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String socialName = socialUserInfo.getName();
        socialDetailsService.bind(username, socialName);

        return "redirect:/";
    }

    @ApiOperation("社交账号解绑")
    @GetMapping(value = "/social/unbind/{type}/{appId}")
    public String unbindSocial(@PathVariable String type,
                               @PathVariable String appId,
                               Model model) {

        return "redirect:/";
    }
}