package pro.bzy.boot.framework.config.mvc;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;


@Configuration
public class RequestErrorHandler {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                //ErrorPage errorPage302 = new ErrorPage(HttpStatus.FOUND, "/login");
                //ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST, "/login");
                ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
                //ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/login");
                //factory.addErrorPages(errorPage302, errorPage400, errorPage404, errorPage500);
                factory.addErrorPages(errorPage404);
            }
        };
    }
}
