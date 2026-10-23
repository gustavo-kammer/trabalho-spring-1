package com.example.ricceta_mia;

import com.example.ricceta_mia.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SwaggerIntegrationTests {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private UserRepository userRepository;

    private final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void swaggerUiAndDefinitionArePublic() throws Exception {
        var ui = request("GET", "/swagger-ui.html", null, null);
        assertEquals(200, ui.statusCode());
        assertTrue(ui.body().contains("Swagger UI"));
        assertEquals(200, request("GET", "/swagger-ui/swagger-ui-bundle.js", null, null).statusCode());
        var config = request("GET", "/v3/api-docs/swagger-config", null, null);
        assertEquals(200, config.statusCode());
        assertEquals("/v3/api-docs", json.readTree(config.body()).path("url").asText());
        assertEquals(200, request("GET", "/v3/api-docs.yaml", null, null).statusCode());
    }

    @Test
    void documentsEveryOperationWithCorrectSecurityAndSchemas() throws Exception {
        var response = request("GET", "/v3/api-docs", null, null);
        assertEquals(200, response.statusCode());
        JsonNode spec = json.readTree(response.body());
        assertEquals("Ricceta Mia API", spec.path("info").path("title").asText());
        assertEquals("bearer", spec.at("/components/securitySchemes/bearerAuth/scheme").asText());
        assertEquals("JWT", spec.at("/components/securitySchemes/bearerAuth/bearerFormat").asText());
        Set<String> actual = new HashSet<>();
        Set<String> tags = new HashSet<>();
        var paths = spec.path("paths").fields();
        while (paths.hasNext()) {
            var path = paths.next();
            var operations = path.getValue().fields();
            while (operations.hasNext()) {
                var entry = operations.next();
                String key = entry.getKey().toUpperCase() + " " + path.getKey();
                actual.add(key);
                JsonNode operation = entry.getValue();
                assertFalse(operation.path("summary").asText().isBlank(), key);
                assertFalse(operation.path("description").asText().isBlank(), key);
                operation.path("tags").forEach(tag -> tags.add(tag.asText()));
                if (path.getKey().startsWith("/auth/")) {
                    assertTrue(operation.path("security").isMissingNode() || operation.path("security").isEmpty(), key);
                    assertTrue(spec.path("security").isMissingNode() || spec.path("security").isEmpty());
                } else {
                    assertTrue(operation.path("security").get(0).has("bearerAuth"), key);
                    assertTrue(operation.path("responses").has("401"), key);
                }
                for (JsonNode parameter : operation.path("parameters")) {
                    assertFalse(Set.of("currentUser", "email").contains(parameter.path("name").asText()), key);
                }
                JsonNode noContent = operation.path("responses").path("204");
                if (!noContent.isMissingNode()) {
                    assertTrue(noContent.path("content").isMissingNode() || noContent.path("content").isEmpty(), key);
                }
            }
        }
        assertEquals(Set.of(
                "POST /auth/login", "POST /auth/register",
                "GET /users", "GET /users/me", "PUT /users/me", "DELETE /users/me", "GET /users/{id}", "GET /users/me/favorites",
                "GET /posts", "POST /posts", "GET /posts/{id}", "PUT /posts/{id}", "DELETE /posts/{id}", "PATCH /posts/{id}/status",
                "GET /posts/{postId}/comments", "POST /posts/{postId}/comments", "PUT /comments/{id}", "DELETE /comments/{id}",
                "GET /posts/{postId}/likes", "POST /posts/{postId}/likes", "DELETE /posts/{postId}/likes",
                "POST /posts/{postId}/favorites", "DELETE /posts/{postId}/favorites",
                "POST /users/{userId}/follow", "DELETE /users/{userId}/follow", "GET /users/{userId}/followers", "GET /users/{userId}/following"
        ), actual);
        assertEquals(7, tags.size());
        JsonNode schemas = spec.path("components").path("schemas");
        assertFalse(schemas.has("JWTUserData"));
        assertFalse(schemas.path("UserResponse").path("properties").has("password"));
        assertFalse(schemas.path("PublicUserResponse").path("properties").has("email"));
        assertTrue(schemas.path("PostResponse").path("properties").has("totalLikes"));
        assertTrue(schemas.path("PostResponse").path("properties").has("likedByMe"));
        assertTrue(schemas.path("PostResponse").path("properties").has("favoritedByMe"));
        assertEquals("maria@example.com", schemas.at("/LoginRequest/properties/email/example").asText());
        assertEquals(8, schemas.at("/RegisterUserRequest/properties/password/minLength").asInt());
        assertEquals("array", spec.at("/paths/~1posts/get/responses/200/content/application~1json/schema/type").asText());
        assertEquals("#/components/schemas/PostRequest", spec.at("/paths/~1posts/post/requestBody/content/application~1json/schema/$ref").asText());
        assertEquals("#/components/schemas/ProblemDetail", spec.at("/paths/~1auth~1register/post/responses/409/content/application~1problem+json/schema/$ref").asText());
        assertEquals("#/components/schemas/ProblemDetail", spec.at("/paths/~1users~1me/get/responses/401/content/application~1problem+json/schema/$ref").asText());
    }

    @Test
    void documentedPublicLoginAndBearerAuthenticationWork() throws Exception {
        var denied = request("GET", "/users/me", null, null);
        assertEquals(401, denied.statusCode());
        assertTrue(denied.headers().firstValue("content-type").orElse("").contains("application/problem+json"));
        assertEquals("Não autorizado", json.readTree(denied.body()).path("title").asText());
        assertEquals(401, request("GET", "/users/me", null, "invalid-token").statusCode());
        assertEquals(400, request("POST", "/auth/register", "{}", null).statusCode());
        String email = "swagger-" + UUID.randomUUID() + "@example.com";
        String password = UUID.randomUUID().toString();
        Long createdId = null;
        try {
            var registration = request("POST", "/auth/register", json.writeValueAsString(Map.of(
                    "firstName", "Teste Swagger", "email", email, "password", password)), null);
            assertEquals(201, registration.statusCode());
            createdId = json.readTree(registration.body()).path("id").asLong();
            assertEquals(401, request("POST", "/auth/login", json.writeValueAsString(Map.of(
                    "email", email, "password", "senha-incorreta")), null).statusCode());
            var login = request("POST", "/auth/login", json.writeValueAsString(Map.of("email", email, "password", password)), null);
            assertEquals(200, login.statusCode());
            String token = json.readTree(login.body()).path("token").asText();
            assertFalse(token.isBlank());
            var profile = request("GET", "/users/me", null, token);
            assertEquals(200, profile.statusCode());
            assertEquals(email, json.readTree(profile.body()).path("email").asText());
            var publicProfile = request("GET", "/users/" + createdId, null, token);
            assertEquals(200, publicProfile.statusCode());
            assertFalse(json.readTree(publicProfile.body()).has("email"));

            var created = request("POST", "/posts", json.writeValueAsString(Map.of(
                    "title", "Receita de teste", "ingredients", "farinha", "instructions", "misture")), token);
            assertEquals(201, created.statusCode());
            JsonNode newPost = json.readTree(created.body());
            assertEquals(0, newPost.path("totalLikes").asLong());
            assertFalse(newPost.path("likedByMe").asBoolean());
            assertFalse(newPost.path("favoritedByMe").asBoolean());
            long postId = newPost.path("id").asLong();
            assertEquals(200, request("POST", "/posts/" + postId + "/likes", null, token).statusCode());
            assertEquals(204, request("POST", "/posts/" + postId + "/favorites", null, token).statusCode());
            JsonNode post = json.readTree(request("GET", "/posts/" + postId, null, token).body());
            assertEquals(1, post.path("totalLikes").asLong());
            assertTrue(post.path("likedByMe").asBoolean());
            assertTrue(post.path("favoritedByMe").asBoolean());
            JsonNode listed = json.readTree(request("GET", "/posts?userId=" + createdId, null, token).body()).get(0);
            assertEquals(1, listed.path("totalLikes").asLong());
            assertTrue(listed.path("favoritedByMe").asBoolean());
        } finally {
            if (createdId != null) {
                userRepository.deleteById(createdId);
            }
        }
    }

    private HttpResponse<String> request(String method, String path, String body, String token) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path));
        if (body != null) builder.header("Content-Type", "application/json");
        if (token != null) builder.header("Authorization", "Bearer " + token);
        builder.method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
