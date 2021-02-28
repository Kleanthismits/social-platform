//$Author$
//$Date$
//$Revision$
//
//
//Original Author: Yanis Charkiolakis
//Created on: Feb 13, 2021 8:41:08 PM
//-----------------------------------------------------------------------------
package com.mitsioulis.socialplatform.user;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import com.mitsioulis.socialplatform.shared.validationGroups.FirstValidation;
import com.mitsioulis.socialplatform.shared.validationGroups.SecondValidation;
import com.mitsioulis.socialplatform.shared.validationGroups.ThirdValidation;

/**
 * @author kleanthis mitsioulis
 *
 */
public class UserGroupSequenceProvider implements DefaultGroupSequenceProvider<User> {

    @Override
    public List<Class<?>> getValidationGroups(User object) {

        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(User.class);
        defaultGroupSequence.add(FirstValidation.class);
        defaultGroupSequence.add(SecondValidation.class);
        defaultGroupSequence.add(ThirdValidation.class);
        return defaultGroupSequence;
    }

}
