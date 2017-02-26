package com.restfulbackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulbackend.common.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hejiang on 14/11/22.
 */
public class ApiHXContact {

    public static class ContactResponse {
        public String action;
        public String application;
        public String path;
        public String uri;
        public List<ApiHXUser.UserEntity> entities;
        public String timestamp;
        public String duration;
        public String organization;
        public String applicationName;
    }

    public ContactResponse addFriendtoUser(String ownerUsername, String friendUsername){
        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpPost post = new HttpPost(ApiHXCommon.getContactFriendURL(ownerUsername, friendUsername));
        ContactResponse contactResponse = new ContactResponse();

        try {
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);

            CloseableHttpResponse httpResponse = client.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
                    .value()) {
                HttpEntity responseEntity = httpResponse
                        .getEntity();
                String contentString = EntityUtils.toString(responseEntity);
                ObjectMapper objectMapper = new ObjectMapper();

                contactResponse = objectMapper.readValue(contentString, ContactResponse.class);
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

        return contactResponse;
    }

    public ContactResponse removeFriendfromUser(String ownerUsername, String friendUsername){
        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpDelete delete = new HttpDelete(ApiHXCommon.getContactFriendURL(ownerUsername, friendUsername));
        ContactResponse contactResponse = new ContactResponse();

        try {
            delete.setHeader("Content-Type", "application/json");
            delete.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);

            CloseableHttpResponse httpResponse = client.execute(delete);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
                    .value()) {
                HttpEntity responseEntity = httpResponse
                        .getEntity();
                String contentString = EntityUtils.toString(responseEntity);
                ObjectMapper objectMapper = new ObjectMapper();

                contactResponse = objectMapper.readValue(contentString, ContactResponse.class);
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

        return contactResponse;
    }
}
