package com.restfulbackend.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulbackend.common.util.HttpClientUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * Created by John_He4 on 11/19/2014.
 */
public class ApiHXChatfile {

    public static class FileMetadata {
        @JsonProperty("content-type")
        public String contentType;

        @JsonProperty("last-modified")
        public String lastModified;

        @JsonProperty("content-length")
        public String contentLength;
    }

    public static class Chatfile {
        public String uuid;
        public String type;
        public String created;
        public String modified;

        @JsonProperty("file-metadata")
        public FileMetadata fileMetadata;

        @JsonProperty("share-secret")
        public String shareSecret;
    }

    public static class ChatfileResponse {
        public String action;
        public String application;
        public String path;
        public String uri;
        public List<Chatfile> entities;
        public String timestamp;
        public String duration;
        public String organization;
        public String applicationName;
    }

    private final static char[] MULTIPART_CHARS =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    .toCharArray();

    protected static String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }

//    public ChatfileResponse upload(String filePath){
    public ChatfileResponse upload(MultipartFile fileToUpload){
        CloseableHttpClient client = HttpClientUtil.createSSLClientDefault();
        HttpPost post = new HttpPost(ApiHXCommon.getChatfileURL());
        ChatfileResponse chatfileResponse = new ChatfileResponse();
//        SendMessageResponse sendMessageResponse = new SendMessageResponse();

        try {
            String boundary = generateBoundary();
            post.setHeader("Content-Type", "multipart/form-data;charset=utf-8;boundary=" + boundary);
            post.setHeader("restrict-access", "true");
            post.setHeader("Authorization", "Bearer " + ApiHXCommon.getAppTokenResponse().access_token);

//            File file = new File(filePath);
//            byte[] fileData = FileUtils.readFileToByteArray(file);
//            ByteArrayBody byteArrayBody = new ByteArrayBody(fileData, file.getName());
////            MultipartEntity multipartEntity = new MultipartEntity();
////            multipartEntity.addPart("file", byteArrayBody);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setBoundary(boundary);
//            builder.addPart("file", byteArrayBody);
//            ByteArrayBody byteArrayBody = new ByteArrayBody(fileToUpload.getBytes(), fileToUpload.getName() + ".jpg");
//            builder.addTextBody(fileToUpload.getName() + ".jpg", byteArrayBody.toString(), ContentType.create("image/jpeg"));

            builder.addBinaryBody("file", fileToUpload.getBytes(), ContentType.create("image/jpeg"), fileToUpload.getName() + ".jpg");
//            builder.addPart("image", byteArrayBody);

            post.setEntity(builder.build());
            CloseableHttpResponse httpResponse = client.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK
                    .value()) {
                HttpEntity responseEntity = httpResponse
                        .getEntity();
                String contentString = EntityUtils.toString(responseEntity);
                ObjectMapper objectMapper = new ObjectMapper();
                chatfileResponse = objectMapper.readValue(contentString, ChatfileResponse.class);
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

        return chatfileResponse;
    }
}
