//$Author$
//$Date$
//$Revision$
//
//
//Original Author: Yanis Charkiolakis
//Created on: Feb 13, 2021 4:52:09 PM
//-----------------------------------------------------------------------------
package com.mitsioulis.socialplatform;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.mitsioulis.socialplatform.user.User;
import com.mitsioulis.socialplatform.user.UserRepository;

/**
 * @author kleanthis mitsioulis
 *
 */
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEnityManager;

    @Autowired
    UserRepository    userRepository;

    @Test
    public void findByUsername_whenUserExists_returnUser() {

        User user = DataProvider.createValidUser();

        testEnityManager.persist(user);

        User inDB = userRepository.findByUsername("test-user");
        assertThat(inDB).isNotNull();
    }

    @Test
    public void findByUsername_whenUserDoesNotExists_returnNull() {

        User inDB = userRepository.findByUsername("test-user");
        assertThat(inDB).isNull();
    }

}