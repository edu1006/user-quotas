package com.vicarius.quotas;

import com.vicarius.quotas.model.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    private static final String PATH = "/api/v1/users";
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource elasticDataSource;

    static String idForUpdate = null;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setFirstName("Test User");

        ResponseEntity<User> response = restTemplate.postForEntity(PATH, user, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Test User");
        idForUpdate = response.getBody().getId();
    }

    @Test
    public void testGetAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(PATH, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }


    @Test
    public void testGetUserById() {
        ResponseEntity<User> response = restTemplate.getForEntity(PATH+"/"+idForUpdate, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(idForUpdate);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(idForUpdate);
        user.setFirstName("Updated User");
        ResponseEntity<User> response = restTemplate.exchange(PATH + "/" + idForUpdate, HttpMethod.PUT, new HttpEntity<>(user), User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Updated User");

    }


    public void testDeleteUser() {
        restTemplate.delete("/users/" + idForUpdate);
        ResponseEntity<User> response = restTemplate.getForEntity("/users/" + idForUpdate, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testConsumeQuota() {
        ResponseEntity<User> response = restTemplate.postForEntity(PATH + "/" + idForUpdate + "/consumeQuota", null, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getQuota() == 1);
        assertThat(response.getBody().getLastLoginTimeUtc() != null);
        assertThat(response.getBody().getId()).isEqualTo(idForUpdate);
    }

    @Test
    public void testConsumeQuotaExceeded() {
        int cont = 0;
        while (cont++ <=5){

            if (cont == 5){
                ResponseEntity<String> response = restTemplate.exchange(PATH + "/" + idForUpdate + "/consumeQuota", HttpMethod.POST, null, String.class);
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
                break;
            }else{
                ResponseEntity<User> response = restTemplate.postForEntity(PATH + "/" + idForUpdate + "/consumeQuota", null, User.class);

            }

        }
    }

    @Test
    public void testGetUsersQuota() {
        ResponseEntity<List<User>> response = restTemplate.exchange(PATH + "/quotas", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
