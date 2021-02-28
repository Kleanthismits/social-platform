//$Author$
//$Date$
//$Revision$
//
//
//Original Author: Yanis Charkiolakis
//Created on: Feb 13, 2021 4:57:12 PM
//-----------------------------------------------------------------------------
package com.mitsioulis.socialplatform;

import com.mitsioulis.socialplatform.user.User;

/**
 * @author kleanthis mitsioulis
 */
public class DataProvider {

	public static User createValidUser() {
		User user = new User();
		user.setUsername("test-user");
		user.setDisplayName("test-display");
		user.setPassword("P@Ssw0rd");
		user.setImage("profile-image.png");
		return user;
	}
}