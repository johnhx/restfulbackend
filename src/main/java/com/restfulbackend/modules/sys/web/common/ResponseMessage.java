package com.restfulbackend.modules.sys.web.common;

import javax.print.DocFlavor;

/**
 * Created by hejiang on 14/11/17.
 */
public class ResponseMessage {
    public static String REGISTER_SUCCESS = "注册成功";
    public static String REGISTER_FAILURE = "注册失败";
    public static String REGISTER_DUPLICATE_USERNAME = "用户名已被注册";

    public static String USER_UPLOAD_AVATAR_SUCCESS = "头像上传成功";
    public static String USER_UPLOAD_AVATAR_FILE_EMPTY = "上传文件为空";

    public static String USER_PROFILE_EMAIL_ALREADY_EXIST = "电子邮件地址已被绑定！";

    public static String USER_VALIDATE_SUCCESS = "验证成功";
    public static String USER_VALIDATE_FAILURE = "验证失败";

    public static String MESSAGE_SEND_FAIL = "发送失败";

    public static String USER_UPDATE_PROFILE_SUCCESS = "个人信息更新成功";

    public static String SEND_MESSAGE_FROM_ERROR = "fromUser不存在！";
    public static String SEND_MESSAGE_TO_ERROR = "toUser不存在！";

    public static String USER_NAME_NOT_EXIST = "用户名不存在！";

    public static String SEND_MESSAGE_ROLE_ERROR = "role不存在！";
}
