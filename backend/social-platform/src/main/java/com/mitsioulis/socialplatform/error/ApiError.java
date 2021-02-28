//$Author$
//$Date$
//$Revision$
//
//
//Original Author: Yanis Charkiolakis
//Created on: Feb 9, 2021 9:21:53 PM
//-----------------------------------------------------------------------------
package com.mitsioulis.socialplatform.error;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kleanthis mitsioulis
 *
 */
@Data
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ApiError {

	private long                timestamp = new Date().getTime();
	private int                 status;
	private String              message;
	private String              url;
	private Map<String, String> validationErrors;

	public ApiError(int status, String message, String url) {

		super();
		this.status = status;
		this.message = message;
		this.url = url;
	}

}
