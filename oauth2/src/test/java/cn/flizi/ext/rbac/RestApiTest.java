package cn.flizi.ext.rbac;


import cn.flizi.auth.App;
import cn.flizi.core.util.R;
import cn.flizi.ext.rbac.entity.SysRole;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

@Log4j2
@SpringBootTest(classes = App.class)
class RestApiTest {

    @Autowired
    private RestApi restApi;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(restApi).isNotNull();
    }

    @Test
    @WithMockUser(username = "1", authorities = {"sys:menu:tree"})
    public void sysMenuTree() throws Exception {
        R<List<?>> listR = restApi.sysMenuTree();
        log.info(listR);
        Assertions.assertThat(listR).isNotNull();
    }

    @Test
    @WithMockUser(username = "1", authorities = {"sys:role:add"})
    public void sysRoleAdd() throws Exception {
        SysRole sysRole = new SysRole();
        sysRole.setName("test_name");
        sysRole.setRemark("test_remark");
        R r = restApi.sysRoleAdd(sysRole);
        Assertions.assertThat(r.getCode().equals(R.OK)).isTrue();
        Assertions.assertThat(sysRole.getId()).isNotNull();
    }


}