package cn.flizi.ext.rbac;


import cn.flizi.auth.App;
import cn.flizi.core.util.R;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

@Log4j2
@SpringBootTest(classes = App.class)
class ExtRestApiTest {

    @Autowired
    private ExtRestApi extRestApi;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(extRestApi).isNotNull();
    }

    @Test
    @WithMockUser(username = "1", authorities = {"sys:menu:tree"})
    public void sysMenuTree() throws Exception {
        R<List<?>> listR = extRestApi.sysMenuTree();
        log.info(listR);
        Assertions.assertThat(listR).isNotNull();
    }
}