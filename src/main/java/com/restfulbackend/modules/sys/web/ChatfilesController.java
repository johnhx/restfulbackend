package com.restfulbackend.modules.sys.web;

import com.restfulbackend.api.ApiHXChatfile;
import com.restfulbackend.api.ApiHXCommon;
import com.restfulbackend.api.ApiHXMessage;
import com.restfulbackend.common.util.JsonResponse;
import com.restfulbackend.modules.sys.entity.User;
import com.restfulbackend.modules.sys.service.UserService;
import com.restfulbackend.modules.sys.web.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by John_He4 on 11/19/2014.
 */
@RestController
@RequestMapping("/chatfiles")
public class ChatfilesController {

    @Autowired
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResponse uploadFile(MultipartHttpServletRequest request){
//                                       @RequestParam("toUserName") String toUserName,
//                                   @RequestParam("fromUserName") String fromUserName,
//                                   @RequestParam("file") CommonsMultipartFile file){

        //MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = request.getFile("file");
        String toUserName = request.getParameter("toUserName");
        String fromUserName = request.getParameter("fromUserName");
        JsonResponse jsonResponse = new JsonResponse();
        ApiHXChatfile apiHXChatfile = new ApiHXChatfile();
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

        if(!file.isEmpty()){
            ApiHXChatfile.ChatfileResponse uploadResponse = apiHXChatfile.upload(file);
            if(uploadResponse != null){
                ApiHXMessage.SendMessageResponse sendResponse =
                        apiHXMessage.sendImgMessageToUser(toUserName, fromUserName, uploadResponse);

                if( sendResponse.data.size() != 0
                        && sendResponse.data.get(toUser) != null
                        && sendResponse.data.get(toUser).toString().equals("success")){
                    jsonResponse.setError(false);
                    jsonResponse.setData(sendResponse.data);
                }
            }
        }

        return jsonResponse;
    }
}
