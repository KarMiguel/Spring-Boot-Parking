package io.github.KarMiguel.parkingapi;

import io.github.KarMiguel.parkingapi.web.dto.UserCreatedDTO;
import io.github.KarMiguel.parkingapi.web.dto.UserResponseDTO;
import io.github.KarMiguel.parkingapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/user/user-insert.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/user/user-delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public  void created_userUserandPasswordValid_return_user_created_status_201(){
        UserResponseDTO responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("bob@gmail.com","123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bob@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }
    @Test
    public  void createdUser_withUserRepeated_returnErrorMessageStatus409(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("ana@gmail.com","123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }
    @Test
    public  void created_userUserInvalid_returnErrorMessage422(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("bob@","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("bob@email","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
         org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public  void created_User_with_Password_returnErrorMessage422(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("",""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("bob@","12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreatedDTO("bob@email.com","1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public  void searchUser_withIdExist_returnUserWithStatus200(){
        UserResponseDTO responseBody = testClient
                .get()
                .uri("api/v1/users/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
    }
    @Test
    public  void searchUser_withIdInexist_returnErrorMessageWithStatus404(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/users/0")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

}
