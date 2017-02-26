package com.restfulbackend.modules.sys.web;

import com.restfulbackend.api.ApiHXContact;
import com.restfulbackend.api.ApiHXMessage;
import com.restfulbackend.common.util.JsonResponse;
import com.restfulbackend.modules.sys.entity.User;
import com.restfulbackend.modules.sys.entity.UserFriend;
import com.restfulbackend.modules.sys.entity.UserFriendList;
import com.restfulbackend.modules.sys.service.UserService;
import com.restfulbackend.modules.sys.web.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;

/**
 * Created by John_He4 on 11/21/2014.
 */
@RestController
@RequestMapping("/contact")
public class ContactController implements ServletContextAware {
    @Autowired
    private UserService userService;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Resource
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/friends/{ownerUuid}", method = RequestMethod.GET)
    public JsonResponse getFriends(@PathVariable String ownerUuid){
        JsonResponse jsonResponse = new JsonResponse();
        UserFriendList userFriendList = userService.getUserFriends(ownerUuid);
        jsonResponse.setData(userFriendList.getUserFriends());

        return jsonResponse;
    }

    // TODO: It can be removed once testing done.
    @RequestMapping(value = "/friend/{ownerUuid}/{friendUuid}", method = RequestMethod.GET)
    public JsonResponse addFriendByGet(@PathVariable String ownerUuid, @PathVariable String friendUuid){
        JsonResponse jsonResponse = new JsonResponse();
        ApiHXContact apiHXContact = new ApiHXContact();

        // TODO: 在我们的系统中插入好友信息。
        User ownerUser = userService.getUserByUUID(ownerUuid);
        User friendUser = userService.getUserByUUID(friendUuid);
        if(ownerUser == null){
            jsonResponse.setMessage("Owner不存在!");
            return jsonResponse;
        }

        if(friendUser == null){
            jsonResponse.setMessage("Friend不存在!");
            return jsonResponse;
        }

        UserFriend userFriend = new UserFriend();
        userFriend.setFriendUuid(friendUser.getUuid());
        userFriend.setOwnerUuid(ownerUser.getUuid());

        Long newId = userService.insertUserFriend(userFriend);
        if(newId == 0){
            jsonResponse.setMessage("新增好友关系失败!");
            return jsonResponse;
        }
        userFriend.setId(newId);

        ApiHXContact.ContactResponse contactResponse = apiHXContact.addFriendtoUser(ownerUser.getUsername(), friendUser.getUsername());
        if(contactResponse.entities.size() != 0
                && contactResponse.entities.get(0) != null
                && contactResponse.entities.get(0).uuid.equals(friendUuid)){
            jsonResponse.setError(false);
            jsonResponse.setData(contactResponse.entities);
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/friend", method = RequestMethod.POST)
    public JsonResponse addFriend(@RequestParam("ownerUuid") String ownerUuid, @RequestParam("friendUuid") String friendUuid){
        JsonResponse jsonResponse = new JsonResponse();
        ApiHXContact apiHXContact = new ApiHXContact();

        // TODO: 在我们的系统中插入好友信息。
        User ownerUser = userService.getUserByUUID(ownerUuid);
        User friendUser = userService.getUserByUUID(friendUuid);
        if(ownerUser == null){
            jsonResponse.setMessage("Owner不存在!");
            return jsonResponse;
        }

        if(friendUser == null){
            jsonResponse.setMessage("Friend不存在!");
            return jsonResponse;
        }

        UserFriend userFriend = new UserFriend();
        userFriend.setFriendUuid(friendUser.getUuid());
        userFriend.setOwnerUuid(ownerUser.getUuid());

        Long newId = userService.insertUserFriend(userFriend);
        if(newId == 0){
            jsonResponse.setMessage("新增好友关系失败!");
            return jsonResponse;
        }
        userFriend.setId(newId);

        ApiHXContact.ContactResponse contactResponse = apiHXContact.addFriendtoUser(ownerUser.getUsername(), friendUser.getUsername());
        if(contactResponse.entities.size() != 0
                && contactResponse.entities.get(0) != null
                && contactResponse.entities.get(0).uuid.equals(friendUuid)){
            jsonResponse.setError(false);
            jsonResponse.setData(contactResponse.entities);
        }

        return jsonResponse;
    }
}
