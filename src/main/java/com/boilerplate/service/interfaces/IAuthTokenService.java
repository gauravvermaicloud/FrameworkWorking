package com.boilerplate.service.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.NotFoundException;

public interface IAuthTokenService {
	public String getAuthToken()  throws NotFoundException, IOException ;

}
