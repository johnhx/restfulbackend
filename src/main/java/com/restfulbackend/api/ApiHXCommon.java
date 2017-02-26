package com.restfulbackend.api;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulbackend.common.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import java.io.*;

public class ApiHXCommon {
	public static String HOST = "https://a1.easemob.com";

	public static String APP_NAME = "amuse";

	// 腾讯云数据库使用的关联环信账户
	public static String ORG_NAME = "johnhx";
	public static String CLIENT_ID = "";
	public static String CLIENT_SECRET = "";

	public static String CHAT_HALL_ID = "";
	public static String CHAT_HALL_NAME = "";
	public static ApiHXChatGroup.JoinedGroupData CHAT_HALL = new ApiHXChatGroup.JoinedGroupData(CHAT_HALL_ID, CHAT_HALL_NAME);

	private static AppTokenResponse appTokenResponse;

	public static class AppTokenResponse {
		public String access_token;
		public String expires_in;
		public String application;
	}
	
	public static AppTokenResponse getAppTokenResponse() throws IOException {
		if(appTokenResponse == null){
//			appTokenResponse = getHttpToken();
			appTokenResponse = new AppTokenResponse();
			File tokenFile = new File("token");
			if(!tokenFile.isFile() || !tokenFile.exists()){
				getHttpToken();
			}
			FileReader fr = new FileReader(tokenFile);
			char[] tokenContent = new char[1024];
			fr.read(tokenContent);
			fr.close();
			String tokenStr = new String(tokenContent);
			ObjectMapper objectMapper = new ObjectMapper();
			appTokenResponse = objectMapper.readValue(tokenStr, AppTokenResponse.class);

//			appTokenResponse.access_token = "YWMtP-4tpmtSEeSMTOdECM5HqQAAAUre-pqSfzvgngkXi5NrMy5gCWRTskUnang";
//			appTokenResponse.application = "b5485ea0-6995-11e4-b6c6-c119727ee8d7";
//			appTokenResponse.expires_in = "5184000";
		}
		
		return appTokenResponse;
	}
	
	public static String getTokenUrl(){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/token";
	}

	public static String getRegisterUrl(){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/users";
	}

	public static String getGetIMUserByPrimaryKeyURL(String primaryKey){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/users/" + primaryKey;
	}

	public static String getGetUserStatusURL(String primaryKey){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/users/" + primaryKey + "/status";
	}

	public static String getMessageURL(){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/messages";
	}

	public static String getChatfileURL(){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/chatfiles";
	}

	public static String getContactFriendURL(String ownerUsername, String friendUsername){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/users/" + ownerUsername
				+ "/contacts/users/" + friendUsername;
	}

	public static String getJoinedGroupsURL(String userPrimaryKey){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/users/" + userPrimaryKey
				+ "/joined_chatgroups";
	}

	public static String getAddMemberURL(String groupId, String userPrimaryKey){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/chatgroups/" + groupId
				+ "/users/" + userPrimaryKey;
	}

	public static String getResetPwdURL(String userPrimaryKey){
		return HOST + "/" + ORG_NAME + "/" + APP_NAME + "/users/" + userPrimaryKey
				+ "/password";
	}

	private static AppTokenResponse getHttpToken() {
		CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
		HttpPost post = new HttpPost(ApiHXCommon.getTokenUrl());
		AppTokenResponse appToken = new AppTokenResponse();
		ObjectMapper objectMapper = new ObjectMapper();

		JSONObject params = new JSONObject();
		params.put("grant_type", "client_credentials");
		params.put("client_id", ApiHXCommon.CLIENT_ID);
		params.put("client_secret", ApiHXCommon.CLIENT_SECRET);
		
		try {
			StringEntity strEntity = new StringEntity(params.toJSONString(), "UTF-8");
			post.setHeader("Content-Type", "application/json");
			post.setEntity(strEntity);
			CloseableHttpResponse httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
					.value()) {
				HttpEntity responseEntity = (HttpEntity) httpResponse
						.getEntity();
				String contentString = EntityUtils.toString(responseEntity);
				InputStream input = ((org.apache.http.HttpEntity) responseEntity)
						.getContent();

				appToken = objectMapper.readValue(contentString, AppTokenResponse.class);
				File tokenFile = new File("token");
				if(tokenFile.createNewFile()){
					FileWriter fw = new FileWriter(tokenFile);
					fw.write(contentString);
					fw.flush();
					fw.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return appToken;
	}
}
