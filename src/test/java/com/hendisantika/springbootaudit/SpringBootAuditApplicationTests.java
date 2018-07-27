package com.hendisantika.springbootaudit;

import com.hendisantika.springbootaudit.domain.User;
import com.hendisantika.springbootaudit.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootAuditApplicationTests {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void create() {
        user = userRepository.save(
                new User().setName("Uzumaki Naruto").setUsername("naruto")
        );

        assertThat(user.getCreated())
                .isNotNull();

        assertThat(user.getModified())
                .isNotNull();

        assertThat(user.getCreatedBy())
                .isEqualTo("Mr. Auditor");

        assertThat(user.getModifiedBy())
                .isEqualTo("Mr. Auditor");
    }

    @Test
    public void update() {
        LocalDateTime created = user.getCreated();
        LocalDateTime modified = user.getModified();

        userRepository.save(
                user.setUsername("naruto")
        );

        userRepository.findById(user.getId())
                .ifPresent(updatedUser -> {

                    assertThat(updatedUser.getUsername())
                            .isEqualTo("naruto");

                    assertThat(updatedUser.getCreated())
                            .isEqualTo(created);

                    assertThat(updatedUser.getModified())
                            .isAfter(modified);
                });
    }

}
