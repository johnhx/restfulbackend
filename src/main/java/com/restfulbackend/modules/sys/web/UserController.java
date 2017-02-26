package com.restfulbackend.modules.sys.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.restfulbackend.api.ApiHXChatGroup;
import com.restfulbackend.api.ApiHXCommon;
import com.restfulbackend.common.util.FileManager;
import com.restfulbackend.common.util.Password;
import com.restfulbackend.mail.ApplicationMailer;
import com.restfulbackend.modules.sys.entity.UserProfile;
import com.restfulbackend.modules.sys.web.common.AccessLevel;
import com.restfulbackend.modules.sys.web.common.Configuration;
import com.restfulbackend.modules.sys.web.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.restfulbackend.api.ApiHXUser;
import com.restfulbackend.api.ApiHXUser.RegisterResponse;
import com.restfulbackend.common.util.JsonResponse;
import com.restfulbackend.modules.sys.service.UserService;
import com.restfulbackend.modules.sys.entity.User;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user")
public class UserController implements ServletContextAware {

	public static class UploadedImage{
		public String url;
	}

	static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UserController.class.getName());

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationMailer mailService;

	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Resource
	public void setMailService(ApplicationMailer mailService){
		this.mailService = mailService;
	}

	@RequestMapping("/test")
	public String test(){
		return "test";
	}

//	@RequestMapping("/profile/{userId}")
//	public JsonResponse getProfileByUserId(@PathVariable long userId){
//		UserProfile userProfile = userService.getUserProfile(userId);
//
//		JsonResponse response = new JsonResponse();
//		if(userProfile != null) {
//			response.setError(false);
//			response.setMessage(JsonResponse.SUCCESS);
//			response.setData(userProfile);
//		}
//		return response;
//	}

	@RequestMapping("/{id}")
	public JsonResponse showUser(@PathVariable long id) {
		User user = userService.getUserById(id);

		JsonResponse response = new JsonResponse();
		if(user != null) {
			response.setError(false);
			response.setMessage(JsonResponse.SUCCESS);
			response.setData(user);
		}
		return response;
	}

	@RequestMapping("/info/{userName}")
	public JsonResponse getInfo(@PathVariable String userName){
		User user = userService.getUserByUsername(userName);

		JsonResponse response = new JsonResponse();
		if(user != null){
			response.setError(false);
			response.setMessage(JsonResponse.SUCCESS);
			response.setData(user);
		}
		return response;
	}

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
//	@RequestMapping(value = "/validate/{username}/{password}", method = RequestMethod.GET)
	public JsonResponse validateUser(@RequestParam String username, @RequestParam String password) {
//	public JsonResponse validateUser(@PathVariable String username, @PathVariable String password) {
		JsonResponse response = new JsonResponse();

		User user = new User();
		user.setUsername(username);
		user.setPassword(Password.createPassword(password));
		User result = userService.validateUser(user);

		if (result != null) {
			// 验证用户在环信是否存在
			ApiHXUser apiHXUser = new ApiHXUser();
			RegisterResponse regResponse = apiHXUser.getIMUserByUUID(result.getUuid());
			logger.info("regResponse entities size: " + regResponse.entities.size());
			if(regResponse.entities.size() != 0) {
				response.setData(result);
				response.setError(false);
				response.setMessage(ResponseMessage.USER_VALIDATE_SUCCESS);

				// 验证用户是否加入了默认的聊天大厅组
				ApiHXChatGroup apiHXChatGroup = new ApiHXChatGroup();
				ApiHXChatGroup.JoinedGroupsResponse joinedGroupsResponse = apiHXChatGroup.joinedGroups(username);
				if(!joinedGroupsResponse.data.contains(ApiHXCommon.CHAT_HALL)){
					ApiHXChatGroup.AddMemberResponse addMemberResponse = apiHXChatGroup.addMember(ApiHXCommon.CHAT_HALL_ID, username);
				}
			}
		}



		return response;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	@RequestMapping(value = "/register/{username}/{password}", method = RequestMethod.GET)
	public JsonResponse registerUser(@RequestParam String username, @RequestParam String password) {
//	public JsonResponse registerUser(@PathVariable String username, @PathVariable String password) {
		JsonResponse response = new JsonResponse();
		response.setMessage(ResponseMessage.REGISTER_FAILURE);

		User user = new User();
		user.setUsername(username);
		user.setPassword(Password.createPassword(password));
		user.setAccessLevel(AccessLevel.LEVEL_1);

		User existingUser = userService.getUserByUsername(username);
		if(existingUser != null){
			response.setMessage(ResponseMessage.REGISTER_DUPLICATE_USERNAME);
			return response;
		}

		int result = userService.insertUser(user);

		if (result != 0) {
			ApiHXUser apiHXUser = new ApiHXUser();
			RegisterResponse regResponse = apiHXUser.register(username, password);

			if(regResponse.entities.size() > 0){
				user.setUuid(regResponse.entities.get(0).uuid);
				userService.updateUser(user);
				response.setData(user);
				response.setError(false);
				response.setMessage(ResponseMessage.REGISTER_SUCCESS);

				// 验证用户是否加入了默认的聊天大厅组
				ApiHXChatGroup apiHXChatGroup = new ApiHXChatGroup();
				ApiHXChatGroup.AddMemberResponse addMemberResponse = apiHXChatGroup.addMember(ApiHXCommon.CHAT_HALL_ID, username);
			}
		}

		return response;
	}


//	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@RequestMapping(value = "/register/{username}/{password}", method = RequestMethod.GET)
//	public JsonResponse registerUser(@RequestParam String username, @RequestParam String password) {
	public JsonResponse registerUserByGet(@PathVariable String username, @PathVariable String password) {
		JsonResponse response = new JsonResponse();
		response.setMessage(ResponseMessage.REGISTER_FAILURE);

		User user = new User();
		user.setUsername(username);
		user.setPassword(Password.createPassword(password));
		user.setAccessLevel(AccessLevel.LEVEL_1);

		User existingUser = userService.getUserByUsername(username);
		if(existingUser != null){
			response.setMessage(ResponseMessage.REGISTER_DUPLICATE_USERNAME);
			return response;
		}

		int result = userService.insertUser(user);

		if (result != 0) {
			ApiHXUser apiHXUser = new ApiHXUser();
			RegisterResponse regResponse = apiHXUser.register(username, password);

			if(regResponse.entities.size() > 0){
				user.setUuid(regResponse.entities.get(0).uuid);
				userService.updateUser(user);
				response.setData(user);
				response.setError(false);
				response.setMessage(ResponseMessage.REGISTER_SUCCESS);
			}
		}

		return response;
	}

	@RequestMapping(value = "/access", method = RequestMethod.POST)
	public JsonResponse updateAccessLevel(@RequestParam String userName, @RequestParam String accessLevel){
		JsonResponse jsonResponse = new JsonResponse();

		User user = userService.getUserByUsername(userName);
		if(user == null){
			jsonResponse.setMessage(ResponseMessage.USER_NAME_NOT_EXIST);
			return jsonResponse;
		}

		user.setAccessLevel(Integer.parseInt(accessLevel));
		int result = userService.updateUserAccessLevel(user);
		jsonResponse.setData(user);
		jsonResponse.setError(false);

		return jsonResponse;
	}

	// TODO: 待测试
	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public JsonResponse uploadAvatar(MultipartHttpServletRequest request){
		MultipartFile file = request.getFile("file");
		String userName = request.getParameter("userName");

		JsonResponse jsonResponse = new JsonResponse();
		User user = userService.getUserByUsername(userName);

		if(user == null){
			jsonResponse.setMessage(ResponseMessage.USER_NAME_NOT_EXIST);
			return jsonResponse;
		}

		if(!file.isEmpty()){
			try{
//				byte[] bytes = file.getBytes();
//				String uploadFilePath = FileManager.getAvatarPath(user.getId());
//				String avatarFile = uploadFilePath
//						+ file.getName()
//						+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//				logger.info("Avatar File: " + avatarFile);
//
//				File uploadedFile = new File(avatarFile);
//				if(!uploadedFile.getParentFile().exists()){
//					uploadedFile.getParentFile().mkdirs();
//				}
//
//				if(!uploadedFile.exists()){
//					uploadedFile.createNewFile();
//				}
				String avatarRelativeFile = "/upload/"
						+ user.getId() + "/avatar/" + file.getOriginalFilename();
				String avatarFile = request.getSession().getServletContext().getRealPath("/") + avatarRelativeFile;
				File uploadedFile = new File(avatarFile);
				logger.info("Avatar File: " + avatarFile);
				if(!uploadedFile.getParentFile().exists()){
					uploadedFile.getParentFile().mkdirs();
				}

				if(!uploadedFile.exists()){
					uploadedFile.createNewFile();
				}

				file.transferTo(uploadedFile);
//				BufferedOutputStream stream =
//						new BufferedOutputStream(new FileOutputStream(new File(avatarFile)));
//				stream.write(bytes);
//				stream.close();
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
				String avatarUrl = basePath + avatarRelativeFile;

				UserProfile userProfile = user.getUserProfile();
				if(userProfile == null){
					// Create Profile
					userProfile = new UserProfile();
					userProfile.setAvatar(avatarUrl);
					int result = userService.insertUserProfile(userProfile);
					if(result != 0) {
						user.setUserProfileId(userProfile.getId());
						userService.updateUserProfileId(user);
					}
				}
				else{
					// Update existing profile
					userProfile = user.getUserProfile();
					userProfile.setAvatar(avatarUrl);
					userService.updateUserProfileAvatar(userProfile);
				}

				UploadedImage uploadedImage = new UploadedImage();
				uploadedImage.url = avatarUrl;
				jsonResponse.setData(uploadedImage);
				jsonResponse.setMessage(ResponseMessage.USER_UPLOAD_AVATAR_SUCCESS);
				jsonResponse.setError(false);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		else{
			jsonResponse.setMessage(ResponseMessage.USER_UPLOAD_AVATAR_FILE_EMPTY);
			jsonResponse.setError(true);
		}

		return jsonResponse;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public JsonResponse updateProfile(@RequestParam("userName") String userName,
									  @RequestParam(value = "accessLevel", required = false) int accessLevel,
									  @RequestParam(value = "email", required = false) String email,
									  @RequestParam(value = "firstName", required = false) String firstName,
									  @RequestParam(value = "lastName", required = false) String lastName,
									  @RequestParam(value = "nickName", required = false) String nickName,
									  @RequestParam(value = "hobby", required = false) String hobby) {
		JsonResponse response = new JsonResponse();

		User user = userService.getUserByUsername(userName);
		UserProfile userProfile = new UserProfile();
		userProfile.setEmail(email);
		userProfile.setFirstName(firstName);
		userProfile.setLastName(lastName);
		userProfile.setNickName(nickName);
		userProfile.setHobby(hobby);

		logger.info("firstName: " + firstName);
		logger.info("lastName: " + lastName);
		logger.info("hobby: " + hobby);

		UserProfile emailUserProfile = userService.getUserProfileByEmail(email);
		if (emailUserProfile != null){
			logger.info("emailUserProfile Id: " + emailUserProfile.getId());
		}
		if(emailUserProfile != null && !emailUserProfile.getId().equals(user.getUserProfileId())){
			response.setMessage(ResponseMessage.USER_PROFILE_EMAIL_ALREADY_EXIST);
			return response;
		}

		if(user != null){
			logger.info("user profile id: " + user.getUserProfileId());
			user.setAccessLevel(accessLevel);
			userService.updateUser(user);

			if(user.getUserProfile() == null
					|| (user.getUserProfile() != null && user.getUserProfile().getId().equals(Long.valueOf(0)))){
				logger.info("user.getUserProfile() is null");

				// Create Profile
				int result = userService.insertUserProfile(userProfile);
				if(result != 0) {
					user.setUserProfileId(userProfile.getId());
					userService.updateUserProfileId(user);
				}
			}
			else{
				logger.info("user.getUserProfile() is not null");

				// Update existing profile
				userProfile = user.getUserProfile();
				userProfile.setEmail(email);
				userProfile.setFirstName(firstName);
				userProfile.setLastName(lastName);
				userProfile.setNickName(nickName);
				userProfile.setHobby(hobby);
				userService.updateUserProfile(userProfile);
			}
			response.setError(false);
			response.setMessage(ResponseMessage.USER_UPDATE_PROFILE_SUCCESS);
		}

		return response;
	}

//	// TODO: 待测试
//	@RequestMapping(value = "/upload", method = RequestMethod.POST)
//	public JsonResponse uploadAvatar(@RequestParam("userId") long userId, @RequestParam("file")CommonsMultipartFile file){
//		JsonResponse response = new JsonResponse();
//		if(file != null){
//			String path = this.servletContext.getRealPath(Configuration.UPLOAD_PATH);
//			File localFile = new File(path + userId);
//			try {
//				file.getFileItem().write(localFile);
//				User user = userService.getUserById(userId);
//				UserProfile userProfile = user.getUserProfile();
//				userProfile.setAvatar(localFile.getCanonicalPath());
//				userService.updateUserProfile(userProfile);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			response.setMessage(ResponseMessage.USER_UPLOAD_AVATAR_SUCCESS);
//		}
//
//		return response;
//	}

//	@RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
//	public JsonResponse getProfile(@PathVariable long id){
//		JsonResponse response = new JsonResponse();
//		UserProfile result = userService.getUserProfileById(id);
//
//		if (result != null) {
//			response.setData(result);
//			response.setError(false);
//			response.setMessage("");
//		}
//
//		return response;
//
//	}

	@RequestMapping(value = "/status/{id}", method = RequestMethod.GET)
	public JsonResponse getStatus(@PathVariable long id){
		JsonResponse response = new JsonResponse();

		User user = userService.getUserById(id);
		if(user != null){
			ApiHXUser apiHXUser = new ApiHXUser();
			ApiHXUser.StatusResponse statusResponse = apiHXUser.getUserStatus(user.getUsername());
			response.setData(statusResponse.data);
			response.setError(false);
		}

		return response;
	}

	@RequestMapping(value = "/nick/{userName}", method = RequestMethod.GET)
	public JsonResponse getStatus(@PathVariable String userName){
		JsonResponse response = new JsonResponse();

		String nick = userService.getNickByUserName(userName);
		String result = nick + "(" + userName + ")";
//		if(user != null){
//			ApiHXUser apiHXUser = new ApiHXUser();
//			ApiHXUser.StatusResponse statusResponse = apiHXUser.getUserStatus(user.getUsername());
//			response.setData(statusResponse.data);
//			response.setError(false);
//		}
		response.setData(result);
		response.setError(false);

		return response;
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ModelAndView actualResetPassword(HttpServletRequest request){
		String newPassword = request.getParameter("newPassword");
		String userName = request.getParameter("userName");

		int result = 0;
		User user = userService.getUserByUsername(userName);
		if(user != null){
			user.setPassword(Password.createPassword(newPassword));
			result = userService.updateUserPassword(user);

			// 重置环信密码！
			if(result != 0){
				ApiHXUser apiHXUser = new ApiHXUser();
				ApiHXUser.ResetPwdResponse resetPwdResponse = apiHXUser.resetPwd(userName, newPassword);
			}
		}

		if(result == 0) {
			return new ModelAndView("resetPasswordFail");
		}
		else{
			return new ModelAndView("resetPasswordSucceed");
		}
	}

	@RequestMapping(value = "/reset/{sid}/{userName}", method = RequestMethod.GET)
	public ModelAndView resetPassword(HttpServletRequest request,
									  HttpServletResponse response,
									  @PathVariable String sid, @PathVariable String userName) throws IOException {
		ModelAndView model = new ModelAndView("resetPassword");
		User user = userService.getUserByUsername(userName);

		if(user != null){
			String forgetDig = user.getForgetDigest();
			if(forgetDig.equals(sid)){
				model.addObject("userName", userName);
				return model;
			}
		}

		return model;
	}

	@RequestMapping(value = "/forget", method = RequestMethod.POST)
	public JsonResponse forgetPassword(HttpServletRequest request, String email){
		JsonResponse jsonResponse = new JsonResponse();

		User user = userService.getUserByEmail(email);
		if(user != null) {
			try {
//				// 密钥
				String secretKey = UUID.randomUUID().toString();

				// 30分钟后过期
				Timestamp outDate = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000);

				// 忽略毫秒数
				long date = outDate.getTime() / 1000 * 1000;
				String key = user.getUsername() + "$" + date + "$" + secretKey;
				String digitalSignature = MessageDigest.getInstance("MD5").digest(key.getBytes()).toString();
				user.setForgetDigest(digitalSignature);
				userService.updateUser(user);

				String emailTitle = "重设密码";

				String path = request.getContextPath();
				String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
				String resetPassHref = basePath + "user/reset/" + digitalSignature + "/" + user.getUsername();
				String emailContent = "请勿回复本邮件.点击下面的链接,重设密码<br/><a href=" + resetPassHref + " target='_BLANK'>点击我重新设置密码</a>" +
						"<br/>本邮件超过30分钟,链接将会失效。";
				System.out.print(resetPassHref);
				mailService.sendMail(email, emailTitle, emailContent);
				jsonResponse.setMessage("操作成功,已经发送找回密码链接到您邮箱。请在30分钟内重置密码");
//				logInfo(request, userName, "申请找回密码");
			} catch (Exception e) {
				e.printStackTrace();
//				msg = "邮箱不存在？未知错误,联系管理员吧。";
			}
		}

		return jsonResponse;
	}
}
