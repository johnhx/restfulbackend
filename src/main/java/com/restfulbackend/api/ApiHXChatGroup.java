package com.restfulbackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulbackend.common.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hejiang on 14/12/13.
 */
public class ApiHXChatGroup {

    /*
    * 为群组加人的返回数据结构。
    * */
    public static class AddMemberData{
        public String action;
        public String result;
        public String groupid;
        public String user;
    }

    public static class AddMemberResponse{
        public String action;
        public String application;
        public String uri;
        public List<Object> entities;
        public AddMemberData data;
        public String timestamp;
        public String duration;
        public String organization;
        public String applicationName;
    }

    /*
    * 查看用户加入的群组的数据结构
    * */

    public static class JoinedGroupData{
        public String groupid;
        public String groupname;

        public JoinedGroupData(){
            this.groupid = "";
            this.groupname = "";
        }

        public JoinedGroupData(String groupId, String groupName){
            this.groupid = groupId;
            this.groupname = groupName;
        }
    }

    public static class JoinedGroupsResponse{
        public String action;
        public String uri;
        public List<Object> entities;
        public List<JoinedGroupData> data;
        public String timestamp;
        public String duration;
    }

    public JoinedGroupsResponse joinedGroups(String userPrimaryKey){

        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpGet get = new HttpGet(ApiHXCommon.getJoinedGroupsURL(userPrimaryKey));
        JoinedGroupsResponse joinedGroupsResponse = new JoinedGroupsResponse();

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

                joinedGroupsResponse = objectMapper.readValue(contentString, JoinedGroupsResponse.class);
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

        return joinedGroupsResponse;
    }

    public AddMemberResponse addMember(String groupId, String userPrimaryKey){

        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpPost post = new HttpPost(ApiHXCommon.getAddMemberURL(groupId, userPrimaryKey));
        AddMemberResponse addMemberResponse = new AddMemberResponse();

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

                addMemberResponse = objectMapper.readValue(contentString, AddMemberResponse.class);
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

        return addMemberResponse;
    }
}
