//$Author$
//$Date$
//$Revision$
//
//
//Original Author: Yanis Charkiolakis
//Created on: Feb 13, 2021 5:17:52 PM
//-----------------------------------------------------------------------------
package com.mitsioulis.socialplatform.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kleanthis mitsioulis
 *
 */
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUserName, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        User inDB = userRepository.findByUsername(value);
        if (inDB == null) {
            return true;
        }

        return false;
    }

}
