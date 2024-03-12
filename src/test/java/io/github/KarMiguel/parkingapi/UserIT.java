package io.github.KarMiguel.parkingapi;

import io.github.KarMiguel.parkingapi.web.dto.UserCreatedDTO;
import io.github.KarMiguel.parkingapi.web.dto.UserPasswordDTO;
import io.github.KarMiguel.parkingapi.web.dto.UserResponseDTO;
import io.github.KarMiguel.parkingapi.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

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
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@gmail.com","123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = testClient
                .get()
                .uri("api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@gmail.com","123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("joao@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void searchUser_withIdInexist_ReturnErrorMessageComStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }
    @Test
    public void buscarUsuario_ComUsuarioClienteBuscandoOutroCliente_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void editarSenha_ComDadosValidos_RetornarStatus204() {
        testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNoContent();

        testClient
                .patch()
                .uri("/api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void editarSenha_ComCamposInvalidos_RetornarErrorMessageComStatus422() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("12345", "12345", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("12345678", "12345678", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void editarSenha_ComSenhaInvalidas_RetornarErrorMessageComStatus400() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("123456", "123456", "000000"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("/api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDTO("000000", "123456", "123456"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void listarUsuarios_ComUsuarioComPermissao_RetornarListaDeUsuariosComStatus200() {
        List<UserResponseDTO> responseBody = testClient
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);
    }

    @Test
    public void listarUsuarios_ComUsuarioSemPermissao_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
