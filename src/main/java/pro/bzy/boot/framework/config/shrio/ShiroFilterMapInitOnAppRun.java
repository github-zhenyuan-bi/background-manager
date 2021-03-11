package pro.bzy.boot.framework.config.shrio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import pro.bzy.boot.framework.config.shrio.service.ShiroService;

@Component
public class ShiroFilterMapInitOnAppRun implements ApplicationRunner{

    @Autowired
    private ShiroService shiroService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        shiroService.updateFilterChainDefinitionMap();
    }

}
