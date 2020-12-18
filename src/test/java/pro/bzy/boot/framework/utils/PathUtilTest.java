package pro.bzy.boot.framework.utils;


import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class PathUtilTest {

    @Test
    void test() throws IOException {
        PathUtil.getWebappResourcePath("/WEB-INF/log/system-log/log-archive");
    }

}
