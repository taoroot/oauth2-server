package cn.flizi.cloud.account.web;

import cn.flizi.cloud.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetApi {

    @ApiOperation(value = "用户基本信息")
    @GetMapping(value = "/user_base")
    public R<Authentication> userBase() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return R.ok(authentication);
    }

}
