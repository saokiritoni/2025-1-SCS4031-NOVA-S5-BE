package nova.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;

@Configuration
public class SwaggerConfig {

    @Value("${api.base-url}")
    private String baseUrl;

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Ecok API Document")
                .description("Ecok API 명세서입니다.")
                .version("v1.0.0");

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(HEADER)
                .bearerFormat("JWT")
                .scheme("Bearer");

        Components components = new Components().addSecuritySchemes("token", securityScheme);

        Server server = new Server();
        server.setUrl(baseUrl);

        return new OpenAPI()
                .info(info)
                .components(components)
                .addServersItem(server)
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("token"))
                ;
    }
}
