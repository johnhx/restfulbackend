package com.restfulbackend.modules.sys.web;

import com.alibaba.fastjson.JSON;
import com.restfulbackend.api.ApiHXCommon;
import com.restfulbackend.api.ApiHXMessage;
import com.restfulbackend.common.util.JsonResponse;
import com.restfulbackend.modules.sys.entity.Message;
import com.restfulbackend.modules.sys.entity.Role;
import com.restfulbackend.modules.sys.entity.TextMessage;
import com.restfulbackend.modules.sys.entity.User;
import com.restfulbackend.modules.sys.service.MessageService;
import com.restfulbackend.modules.sys.service.RoleService;
import com.restfulbackend.modules.sys.service.UserService;
import com.restfulbackend.modules.sys.web.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by John_He4 on 11/19/2014.
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MessageController.class.getName());


    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/broadcast", method = RequestMethod.POST)
    public JsonResponse broadcastSendMessage(@RequestParam String fromUserName,
                                             @RequestParam String accessLevel,
                                             @RequestParam String role,
                                             @RequestParam String message){
        JsonResponse jsonResponse = new JsonResponse();
        ApiHXMessage apiHXMessage = new ApiHXMessage();

        // 判断角色是否存在
        Role roleEntity = roleService.getRoleByName(role);
        if(roleEntity == null){
            jsonResponse.setMessage(ResponseMessage.SEND_MESSAGE_ROLE_ERROR);
            return jsonResponse;
        }

        User fromUser = userService.getUserByUsername(fromUserName);
        User toUser = new User();

        // fromUser判断是否存在
        if(fromUser == null){
            jsonResponse.setMessage(ResponseMessage.SEND_MESSAGE_FROM_ERROR);
            return jsonResponse;
        }

        String targetType = ApiHXMessage.TARGET_TYPE_USER;

        List<User> userList = userService.getUsersByRole(String.valueOf(roleEntity.getId()));
        if(userList != null){
            List<String> toUsers = new ArrayList<String>();
            for(User user : userList){
                if(user.getAccessLevel() >= Integer.valueOf(accessLevel)){

                    // 把发送的消息插入到我们自己的数据表中
                    Message messageEntity = new Message();
                    messageEntity.setFromId(fromUser.getId());
                    messageEntity.setToId(user.getId());
                    messageEntity.setToTypeId(ApiHXMessage.TARGET_TYPE_USER_ID);
                    messageEntity.setMsgTypeId(ApiHXMessage.MSG_TYPE_TEXT_ID);
                    TextMessage textMessageEntity = new TextMessage();
                    textMessageEntity.setMsg(message);
                    long localMessageId = messageService.insertTextMessage(messageEntity, textMessageEntity);

                    if(localMessageId > 0){
                        toUsers.add(user.getUsername());
                    }
                }
            }

            // 将消息发送到环信
            if(toUsers.size() > 0){
                ApiHXMessage.SendMessageResponse sendMessageResponse = apiHXMessage.sendTextMessageToUsers(
                        targetType,
                        toUsers,
                        fromUserName,
                        message
                        );
                HashMap<String, String> sendResult = new HashMap<String, String>();
                for(String user : toUsers){
                    if(sendMessageResponse.data.containsKey(user)){
                        sendResult.put(user, sendMessageResponse.data.get(user).toString());
                    }
                    else{
                        sendResult.put(user, "failure");
                    }
                }

                if(sendResult.size() > 0){
                    jsonResponse.setData(sendResult);
                    jsonResponse.setError(false);
                }
                else{
                    jsonResponse.setData(null);
                    jsonResponse.setError(false);
                }
            }
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public JsonResponse sendMessageByPost(@RequestParam String fromUserName,
                                          @RequestParam String toUserName,
                                          @RequestParam String message){
        JsonResponse jsonResponse = new JsonResponse();
        ApiHXMessage apiHXMessage = new ApiHXMessage();

        logger.info("fromUserName: " + fromUserName);
        logger.info("toUserName: " + toUserName);

        User fromUser = userService.getUserByUsername(fromUserName);
        User toUser = new User();

        // fromUser判断是否存在
        if(fromUser == null){
            jsonResponse.setMessage(ResponseMessage.SEND_MESSAGE_FROM_ERROR);
            return jsonResponse;
        }

        String targetType = ApiHXMessage.TARGET_TYPE_USER;
        // 特殊处理：当是发往聊天大厅的信息时，直接放过。
        if(toUserName.equals(ApiHXCommon.CHAT_HALL_ID)){
            targetType = ApiHXMessage.TARGET_TYPE_GROUP;
            toUser.setId(Long.valueOf(0));
        }
        else {
            toUser = userService.getUserByUsername(toUserName);
            // 当是发往user的信息时，要判断toUser判断是否存在
            if (toUser == null) {
                jsonResponse.setMessage(ResponseMessage.SEND_MESSAGE_TO_ERROR);
                return jsonResponse;
            }
        }

        // 把发送的消息插入到我们自己的数据表中
        Message messageEntity = new Message();
        messageEntity.setFromId(fromUser.getId());
        messageEntity.setToId(toUser.getId());
        if(targetType.equals(ApiHXMessage.TARGET_TYPE_USER)) {
            messageEntity.setToTypeId(ApiHXMessage.TARGET_TYPE_USER_ID);
        }
        else{
            messageEntity.setToTypeId(ApiHXMessage.TARGET_TYPE_GROUP_ID);
        }
        messageEntity.setMsgTypeId(ApiHXMessage.MSG_TYPE_TEXT_ID);
        TextMessage textMessageEntity = new TextMessage();
        textMessageEntity.setMsg(message);
        long localMessageId = messageService.insertTextMessage(messageEntity, textMessageEntity);

        if(localMessageId > 0){
            // TODO: 由app将消息直接发给环信
            // 把消息通过环信接口发送出去
//            ApiHXMessage.SendMessageResponse sendMessageResponse = apiHXMessage.sendTextMessageToUser(targetType, toUserName, fromUserName, message);
//            if(sendMessageResponse.data != null) {
//                if (sendMessageResponse.data.get(toUserName).equals("success")) {
                    jsonResponse.setError(false);
                    jsonResponse.setData(null);
//                } else {
//                    jsonResponse.setMessage(ResponseMessage.MESSAGE_SEND_FAIL);
//                }
//            }
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/send/{fromUserName}/{toUserName}/{message}", method = RequestMethod.GET)
    public JsonResponse sendMessage(@PathVariable String fromUserName, @PathVariable String toUserName, @PathVariable String message){
        JsonResponse jsonResponse = new JsonResponse();
        ApiHXMessage apiHXMessage = new ApiHXMessage();

        User fromUser = userService.getUserByUsername(fromUserName);
        User toUser = new User();

        // fromUser判断是否存在
        if(fromUser == null){
            jsonResponse.setMessage(ResponseMessage.SEND_MESSAGE_FROM_ERROR);
            return jsonResponse;
        }

        String targetType = ApiHXMessage.TARGET_TYPE_USER;
        // 特殊处理：当是发往聊天大厅的信息时，直接放过。
        if(toUserName.equals(ApiHXCommon.CHAT_HALL_ID)){
            targetType = ApiHXMessage.TARGET_TYPE_GROUP;
            toUser.setId(Long.valueOf(0));
        }
        else {
            toUser = userService.getUserByUsername(toUserName);
            // 当是发往user的信息时，要判断toUser判断是否存在
            if (toUser == null) {
                jsonResponse.setMessage(ResponseMessage.SEND_MESSAGE_TO_ERROR);
                return jsonResponse;
            }
        }

        // 把发送的消息插入到我们自己的数据表中
        Message messageEntity = new Message();
        messageEntity.setFromId(fromUser.getId());
        messageEntity.setToId(toUser.getId());
        messageEntity.setToTypeId(ApiHXMessage.TARGET_TYPE_USER_ID);
        messageEntity.setMsgTypeId(ApiHXMessage.MSG_TYPE_TEXT_ID);
        TextMessage textMessageEntity = new TextMessage();
        textMessageEntity.setMsg(message);
        long localMessageId = messageService.insertTextMessage(messageEntity, textMessageEntity);

        if(localMessageId > 0){
            // 把消息通过环信接口发送出去
            ApiHXMessage.SendMessageResponse sendMessageResponse = apiHXMessage.sendTextMessageToUser(targetType, toUser.getUsername(), fromUser.getUsername(), message);
            if(sendMessageResponse.data.get(toUser.getUuid()).equals("success")) {
                jsonResponse.setError(false);
                jsonResponse.setData(sendMessageResponse);
            } else {
                jsonResponse.setMessage(ResponseMessage.MESSAGE_SEND_FAIL);
            }
        }

        return jsonResponse;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

//    public JsonResponse send
}
