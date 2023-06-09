package org.archguard.spec.lang

import org.archguard.spec.base.verifier.FakeRuleVerifier
import org.archguard.spec.element.RestApiElement
import org.junit.jupiter.api.Test

class RestRestApiSpecTest {
    private val governance = rest_api {
        uri_construction {
            pattern("/api\\/[a-zA-Z0-9]+\\/v[0-9]+\\/[a-zA-Z0-9\\/\\-]+")
            example("/api/petstore/v1/pets/dogs")
        }

        http_action("GET", "POST", "PUT", "DELETE")
        status_code(200, 201, 202, 204, 400, 401, 403, 404, 500, 502, 503, 504)

        security(
            """
Token Based Authentication (Recommended) Ideally, microservices should be stateless so the service instances can be scaled out easily and the client requests can be routed to multiple independent service providers. A token based authentication mechanism should be used instead of session based authentication
            """.trimIndent()
        )

        misc("""""")
    }

    @Test
    fun spec_checking() {

        val restApi = RestApiElement("/api/petstore/v1/pets/dogs", "GET", listOf(200, 500))
        governance.setVerifier(FakeRuleVerifier())
        governance.exec(restApi)
    }
}
