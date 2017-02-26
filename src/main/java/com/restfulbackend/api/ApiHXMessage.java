package com.restfulbackend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulbackend.common.util.HttpClientUtil;
import com.restfulbackend.common.util.JsonResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import javax.xml.soap.Text;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by John_He4 on 11/19/2014.
 */
public class ApiHXMessage {

    public static int TARGET_TYPE_USER_ID = 1;
    public static String TARGET_TYPE_USER = "users";
    public static int TARGET_TYPE_GROUP_ID = 2;
    public static String TARGET_TYPE_GROUP = "chatgroups";
    public static int MSG_TYPE_TEXT_ID = 1;
    public static String MSG_TYPE_TEXT = "txt";
    public static int MSG_TYPE_IMG_ID = 2;
    public static String MSG_TYPE_IMG = "img";
    public static int MSG_TYPE_AUDIO_ID = 3;
    public static String MSG_TYPE_AUDIO = "audio";

    public static class SendMessageRequest<T> {
        public SendMessageRequest(){
            target = new ArrayList<String>();
        }

        public String target_type;
        public List<String> target;
        public T msg;
        public String from;
    }

    public static class TextMessage {
        public String type;
        public String msg;
    }

    public static class ImgMessage {
        public String type;
        public String url;
        public String filename;
        public String secret;
    }

    public static class AudioMessage{
        public String type;
        public String url;
        public String filename;
        public String length;
        public String secret;

        public AudioMessage(){
            this.type = MSG_TYPE_AUDIO;
        }
    }

    public static class SendMessageResponse {
        public String action;
        public String application;
        public String uri;
        public List<Object> entities;
        public HashMap data;
        public String timestamp;
        public String duration;
        public String organization;
        public String applicationName;
    }

    public SendMessageResponse sendTextMessageToUsers(String targetType, List<String> toUserPrimaryKeys, String fromUserPrimaryKey, String message){
        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpPost post = new HttpPost(ApiHXCommon.getMessageURL());
        SendMessageResponse sendMessageResponse = new SendMessageResponse();

        try {
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);

            ObjectMapper objectMapper = new ObjectMapper();
            SendMessageRequest<TextMessage> sendMessageRequest = new SendMessageRequest<TextMessage>();
            sendMessageRequest.target_type = targetType;
            sendMessageRequest.target = toUserPrimaryKeys;
            TextMessage tm = new TextMessage();
            tm.type = MSG_TYPE_TEXT;
            tm.msg = message;
            sendMessageRequest.msg = tm;
            // TODO: SET FROM?
            sendMessageRequest.from = fromUserPrimaryKey;
            StringEntity strEntity = new StringEntity(objectMapper.writeValueAsString(sendMessageRequest), "UTF-8");
            post.setEntity(strEntity);

            CloseableHttpResponse httpResponse = client.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
                    .value()) {
                HttpEntity responseEntity = httpResponse
                        .getEntity();
                String contentString = EntityUtils.toString(responseEntity);

                sendMessageResponse = objectMapper.readValue(contentString, SendMessageResponse.class);
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

        return sendMessageResponse;
    }

    public SendMessageResponse sendTextMessageToUser(String targetType, String toUserPrimaryKey, String fromUserPrimaryKey, String message){

        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpPost post = new HttpPost(ApiHXCommon.getMessageURL());
        SendMessageResponse sendMessageResponse = new SendMessageResponse();

        try {
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);

            ObjectMapper objectMapper = new ObjectMapper();
            SendMessageRequest<TextMessage> sendMessageRequest = new SendMessageRequest<TextMessage>();
            sendMessageRequest.target_type = targetType;
            sendMessageRequest.target.add(toUserPrimaryKey);
            TextMessage tm = new TextMessage();
            tm.type = MSG_TYPE_TEXT;
            tm.msg = message;
            sendMessageRequest.msg = tm;
            // TODO: SET FROM?
            sendMessageRequest.from = fromUserPrimaryKey;
            StringEntity strEntity = new StringEntity(objectMapper.writeValueAsString(sendMessageRequest), "UTF-8");
            post.setEntity(strEntity);

            CloseableHttpResponse httpResponse = client.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
                    .value()) {
                HttpEntity responseEntity = httpResponse
                        .getEntity();
                String contentString = EntityUtils.toString(responseEntity);

                sendMessageResponse = objectMapper.readValue(contentString, SendMessageResponse.class);
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

        return sendMessageResponse;
    }

    // TODO: 需要测试！！！
    public SendMessageResponse sendImgMessageToUser(String toUserPrimaryKey, String fromUserPromaryKey, ApiHXChatfile.ChatfileResponse uploadFile){

        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpPost post = new HttpPost(ApiHXCommon.getMessageURL());
        SendMessageResponse sendMessageResponse = new SendMessageResponse();

        try {
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);

            ObjectMapper objectMapper = new ObjectMapper();
            SendMessageRequest<ImgMessage> sendMessageRequest = new SendMessageRequest<ImgMessage>();
            sendMessageRequest.target_type = TARGET_TYPE_USER;
            sendMessageRequest.target.add(toUserPrimaryKey);
            ImgMessage tm = new ImgMessage();
            tm.type = MSG_TYPE_IMG;
            // TODO: 如何处理多个上传文件的情况？
            tm.url = ApiHXCommon.getChatfileURL() + "/" + uploadFile.entities.get(0).uuid;

            // TODO: 此处的文件名需要和上传文件时的文件名保持一致吗？
            tm.filename = "test.jpg";
            tm.secret = uploadFile.entities.get(0).shareSecret;
            sendMessageRequest.msg = tm;
            // TODO: 设置FROM字段?
            sendMessageRequest.from = fromUserPromaryKey;
            StringEntity strEntity = new StringEntity(objectMapper.writeValueAsString(sendMessageRequest), "UTF-8");
            post.setEntity(strEntity);

            CloseableHttpResponse httpResponse = client.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
                    .value()) {
                HttpEntity responseEntity = httpResponse
                        .getEntity();
                String contentString = EntityUtils.toString(responseEntity);

                sendMessageResponse = objectMapper.readValue(contentString, SendMessageResponse.class);
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

        return sendMessageResponse;
    }

    // TODO: 需要测试！！！
    public SendMessageResponse sendAudioMessageToUser(String toUserPrimaryKey, ApiHXChatfile.ChatfileResponse uploadFile){

        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpPost post = new HttpPost(ApiHXCommon.getMessageURL());
        SendMessageResponse sendMessageResponse = new SendMessageResponse();

        try {
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);

            ObjectMapper objectMapper = new ObjectMapper();
            SendMessageRequest<AudioMessage> sendMessageRequest = new SendMessageRequest<AudioMessage>();
            sendMessageRequest.target_type = TARGET_TYPE_USER;
            sendMessageRequest.target.add(toUserPrimaryKey);
            AudioMessage tm = new AudioMessage();
            // http://easemob.com/docs/rest/sendmessage/#sendimgmsg
            // TODO: 如何处理多个上传文件的情况？
            tm.url = ApiHXCommon.getChatfileURL() + "/" + uploadFile.entities.get(0).uuid;
            // TODO: 此处的文件名需要和上传文件时的文件名保持一致吗？length怎么配？
            tm.filename = "test";
            tm.length = "10";
            tm.secret = uploadFile.entities.get(0).shareSecret;
            sendMessageRequest.msg = tm;
            // TODO: 设置FROM字段?
            StringEntity strEntity = new StringEntity(objectMapper.writeValueAsString(sendMessageRequest), "UTF-8");
            post.setEntity(strEntity);

            CloseableHttpResponse httpResponse = client.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
                    .value()) {
                HttpEntity responseEntity = httpResponse
                        .getEntity();
                String contentString = EntityUtils.toString(responseEntity);

                sendMessageResponse = objectMapper.readValue(contentString, SendMessageResponse.class);
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

        return sendMessageResponse;
    }
}
