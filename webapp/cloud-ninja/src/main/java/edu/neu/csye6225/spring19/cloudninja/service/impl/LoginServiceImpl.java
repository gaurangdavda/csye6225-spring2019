package edu.neu.csye6225.spring19.cloudninja.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.ResponseBody;
import edu.neu.csye6225.spring19.cloudninja.model.TimeStampWrapper;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;
import edu.neu.csye6225.spring19.cloudninja.service.AuthService;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import edu.neu.csye6225.spring19.cloudninja.util.AuthServiceUtil;
import edu.neu.csye6225.spring19.cloudninja.util.CommonUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private AuthServiceUtil authServiceUtil;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TimeStampWrapper timeStampWrapper;

	@Autowired
	private ResponseBody responseBody;

	@Autowired
	private AuthService authService;

	private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);

	@Override
	public TimeStampWrapper getTimestamp(String authHeader) throws ValidationException, UnAuthorizedLoginException {
		// Authenticating User before proceeding
		try {
			authService.authenticateUser(authService.extractCredentialsFromHeader(authHeader));

			timeStampWrapper.setTimeStamp(
					new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(System.currentTimeMillis())));
			logger.log(Level.INFO, "Login successful.");
			return timeStampWrapper;
		} catch (Exception e) {
			logger.log(Level.ERROR, commonUtil.stackTraceString(e));
			throw e;
		}
	}

	public ResponseBody registerUser(UserCredentials userCredential) throws ValidationException {

		try {
			authServiceUtil.isValidEmail(userCredential.getEmailId());
			String emailId = userCredential.getEmailId().toLowerCase();

			List<UserCredentials> credentialList = userRepository.findByEmailId(emailId);
			if (credentialList == null || credentialList.size() == 0) {
				authServiceUtil.checkPasswordStrength(userCredential.getPassword());
				String password = authServiceUtil.encryptPassword(userCredential.getPassword());
				userCredential.setEmailId(emailId);
				userCredential.setPassword(password);
				userRepository.save(userCredential);
			} else {
				throw new ValidationException("User already exists. Kindly login.");
			}
			responseBody.setResponseMessage("User created successfully.");
			return responseBody;
		} catch (Exception e) {
			logger.log(Level.ERROR, commonUtil.stackTraceString(e));
			throw e;
		}
	}

}
