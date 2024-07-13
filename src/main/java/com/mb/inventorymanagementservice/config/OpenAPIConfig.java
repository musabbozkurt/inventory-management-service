package com.mb.inventorymanagementservice.config;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.money.MonetaryAmount;
import java.util.Arrays;
import java.util.Iterator;

@Configuration
public class OpenAPIConfig implements ModelConverter {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Management Service OpenAPI")
                        .version("0.0")
                        .description("Inventory Management Service Swagger")
                        .contact(new Contact()
                                .name("Musab Bozkurt")
                                .url("https://github.com/musabbozkurt")
                                .email("musab.bozkurt@mb.com")))
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").in(SecurityScheme.In.HEADER).name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }

    @Override
    public Schema<?> resolve(AnnotatedType annotatedType, ModelConverterContext modelConverterContext, Iterator<ModelConverter> modelConverterIterator) {
        if (annotatedType.isSchemaProperty()) {
            JavaType javaType = Json.mapper().constructType(annotatedType.getType());
            if (javaType != null) {
                Class<?> clazz = javaType.getRawClass();
                if (MonetaryAmount.class.isAssignableFrom(clazz)) {
                    return new ObjectSchema()
                            .addProperty("amount", new NumberSchema())
                            .addProperty("currency", new StringSchema());
                }
            }
        }
        return (modelConverterIterator.hasNext()) ? modelConverterIterator.next().resolve(annotatedType, modelConverterContext, modelConverterIterator) : null;
    }
}
