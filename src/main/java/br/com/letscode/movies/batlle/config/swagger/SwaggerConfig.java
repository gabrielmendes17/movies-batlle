package br.com.letscode.movies.batlle.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by IntelliJ IDEA.
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 9/4/17
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * Every Docket bean is picked up by the swagger-mvc framework - allowing for multiple
     * swagger groups i.e. same code base multiple swagger resource listings.
     */
    @Bean
    public Docket salesApiSwagger() {
      return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("br.com.letscode.movies.batlle.presenter.rest.controllers"))
          .paths(PathSelectors.any())
          .build().apiInfo(this.metaData());
    }
  

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
            .title("Movies Batlle")
            .description("Game to guess movies with the highest score.")
            .version("1.0")
            .contact(new Contact("Gabriel Mendes", "https://github.com/gabrielmendes17",
                "gabrielmendes17@gmail.com"))
            .license("Apache License Version 2.0")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
            .build();
      }
}