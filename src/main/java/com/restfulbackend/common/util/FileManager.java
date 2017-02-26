package com.restfulbackend.common.util;

import com.restfulbackend.modules.sys.web.common.Configuration;

/**
 * Created by John_He4 on 12/9/2014.
 */
public class FileManager {

    public static String getAvatarPath(Long userId){
        return Configuration.UPLOAD_PATH + userId + "/avatar/";
    }
}
