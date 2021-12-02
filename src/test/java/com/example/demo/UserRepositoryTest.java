package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
//video 1:27:52 Spring Boot Registration and Login with MySQL Database, Bootstrap and HTML5
    //ima jos testova
    @Autowired
    UserRepository repo;


    @Test
    public void testFindUserByEmail() {

        String email = "medina.trumic@fet.ba";

        User user = repo.findByEmail(email);

       // assertThat(user).isNotNull();

    }


}
