package com.restfulbackend.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulbackend.common.util.HttpClientUtil;

public class ApiHXUser {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserEntity{
		public UserEntity(){}
		public String uuid;
		public String type;
		public String created;
		public String modified;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public String nickname;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public String username;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public String activated;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public String notification_display_style;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public String notification_no_disturbing;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public String notifier_name;

		@JsonIgnoreProperties(ignoreUnknown = true)
		public String device_token;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RegisterResponse {
		public RegisterResponse(){
			entities = new ArrayList<UserEntity>();
		}

		public String action;
		public String application;
		//public Params params;
		public String path;
		public String uri;
		public List<UserEntity> entities;
		public String timestamp;
		public String duration;
		public String organization;
		public String applicationName;
	}

	public static class StatusResponse {
		public String action;
		public String application;
		public String uri;
		public List<UserEntity> entities;
		public HashMap data;
		public String timestamp;
		public String duration;
		public String organization;
		public String applicationName;
	}

	public static class ResetPwdResponse{
		public String action;
		public String timestamp;
		public String duration;
	}

	public ResetPwdResponse resetPwd(String username, String newPassword){
		CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
		HttpPut put = new HttpPut(ApiHXCommon.getResetPwdURL(username));
		ResetPwdResponse resetPwdResponse = new ResetPwdResponse();

		JSONObject params = new JSONObject();
		params.put("newpassword", newPassword);

		try {
			StringEntity strEntity = new StringEntity(params.toJSONString(), "UTF-8");
			put.setHeader("Content-Type", "application/json");
			put.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);
			put.setEntity(strEntity);
			CloseableHttpResponse httpResponse = client.execute(put);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
					.value()) {
				HttpEntity responseEntity = httpResponse
						.getEntity();
				String contentString = EntityUtils.toString(responseEntity);

				ObjectMapper objectMapper = new ObjectMapper();
				resetPwdResponse = objectMapper.readValue(contentString, ResetPwdResponse.class);
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

		return resetPwdResponse;
	}
	
	public RegisterResponse register(String username, String password){
		CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
		HttpPost post = new HttpPost(ApiHXCommon.getRegisterUrl());
		RegisterResponse registerResponse = new RegisterResponse();

		JSONObject params = new JSONObject();
		params.put("username", username);
		params.put("password", password);
		
		try {
			StringEntity strEntity = new StringEntity(params.toJSONString(), "UTF-8");
			post.setHeader("Content-Type", "application/json");
			post.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);
			post.setEntity(strEntity);
			CloseableHttpResponse httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
					.value()) {
				HttpEntity responseEntity = httpResponse
						.getEntity();
				String contentString = EntityUtils.toString(responseEntity);

				ObjectMapper objectMapper = new ObjectMapper();
 				registerResponse = objectMapper.readValue(contentString, RegisterResponse.class);
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
		
		return registerResponse;
	}

	public RegisterResponse getIMUserByUUID(String UUID){
		CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
		HttpGet get = new HttpGet(ApiHXCommon.getGetIMUserByPrimaryKeyURL(UUID));
		RegisterResponse registerResponse = new RegisterResponse();

		try {
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);
			CloseableHttpResponse httpResponse = client.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
					.value()) {
				HttpEntity responseEntity = httpResponse
						.getEntity();
				String contentString = EntityUtils.toString(responseEntity);

				ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				registerResponse = objectMapper.readValue(contentString, RegisterResponse.class);
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

		return registerResponse;
	}

	public RegisterResponse getIMUserByPrimaryKey(String primaryKey){
		CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
		HttpGet get = new HttpGet(ApiHXCommon.getGetIMUserByPrimaryKeyURL(primaryKey));
		RegisterResponse registerResponse = new RegisterResponse();

		try {
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);
			CloseableHttpResponse httpResponse = client.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
					.value()) {
				HttpEntity responseEntity = httpResponse
						.getEntity();
				String contentString = EntityUtils.toString(responseEntity);

				ObjectMapper objectMapper = new ObjectMapper();
				registerResponse = objectMapper.readValue(contentString, RegisterResponse.class);
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

		return registerResponse;
	}

	public StatusResponse getUserStatus(String primaryKey){
		CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
		HttpGet get = new HttpGet(ApiHXCommon.getGetUserStatusURL(primaryKey));
		StatusResponse statusResponse = new StatusResponse();

		try {
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);
			CloseableHttpResponse httpResponse = client.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
					.value()) {
				HttpEntity responseEntity = httpResponse
						.getEntity();
				String contentString = EntityUtils.toString(responseEntity);

				ObjectMapper objectMapper = new ObjectMapper();
				statusResponse = objectMapper.readValue(contentString, StatusResponse.class);
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

		return statusResponse;
	}
}
