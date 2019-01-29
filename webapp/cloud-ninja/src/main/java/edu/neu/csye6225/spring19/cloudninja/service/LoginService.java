package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

public interface LoginService {
	public boolean checkCredentials(String authHeader);
	public String registerUser(UserCredentials userCredentials);
}
