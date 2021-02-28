//$Author$
//$Date$
//$Revision$
//
//
//Original Author: Yanis Charkiolakis
//Created on: Feb 13, 2021 5:13:50 PM
//-----------------------------------------------------------------------------
package com.mitsioulis.socialplatform.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author kleanthis mitsioulis
 *
 */
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUserName {

    String message() default "{socialplatform.constraints.username.Duplicate.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
