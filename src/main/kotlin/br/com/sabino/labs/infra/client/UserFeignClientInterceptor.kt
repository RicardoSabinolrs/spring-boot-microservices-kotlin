package br.com.sabino.labs.infra.client

import br.com.sabino.labs.infra.security.getCurrentUserJWT
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER_TOKEN_TYPE = "Bearer"

@Component
class UserFeignClientInterceptor : RequestInterceptor {

    override fun apply(template: RequestTemplate) =
        getCurrentUserJWT().ifPresent { s -> template.header(AUTHORIZATION_HEADER, "$BEARER_TOKEN_TYPE $s") }
}
