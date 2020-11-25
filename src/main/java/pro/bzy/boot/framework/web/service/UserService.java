package pro.bzy.boot.framework.web.service;

import pro.bzy.boot.framework.web.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户 服务类
 * 使用自定义逆向生成模板, 模板文件位置classpath：templates/mybatisCodeGen/service.java.ftl
 * @author zhenyuan.bi
 * @since 2020-09-07
 */
public interface UserService extends IService<User> {
    
    /** 校验用户输入的用户名和密码是否正确 */
    boolean checkUsernameAndPassword(User user);

    /** 添加一个用户 然后处理一些添加后的必要事项 */
    void addUserThenHandlerSomething(User user);
    
    /** 删除一个用户 然后处理一些添加后的必要事项 */
    boolean removeUserByIdThenHandlerSomething(String id);
    
}
