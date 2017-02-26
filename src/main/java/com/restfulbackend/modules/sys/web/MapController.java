package com.restfulbackend.modules.sys.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulbackend.api.ApiHXCommon;
import com.restfulbackend.common.util.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john_he4 on 11/25/2014.
 */
@RestController
@RequestMapping("/map")
public class MapController {
    public static class MapElement{
        public Long id;
        public String type;
        public Long typeId;
        public String posX;
        public String posY;
        public String posZ;
    }

    public static class MapLayout {
        public List<MapElement> mapElements;
    }

    @RequestMapping(value = "/layout", method = RequestMethod.GET)
    public JsonResponse getLayout() throws IOException {
        JsonResponse jsonResponse = new JsonResponse();
        MapLayout mapLayout = new MapLayout();

//        if(mapLayout == null){
            File tokenFile = new File("mapLayout");
            if(!tokenFile.isFile() || !tokenFile.exists()){
                //getHttpToken();
                jsonResponse.setError(true);
                jsonResponse.setMessage("mapLayout文件不存在!");
                return jsonResponse;
            }
            FileReader fr = new FileReader(tokenFile);
            char[] tokenContent = new char[4096];
            fr.read(tokenContent);
            fr.close();
            String tokenStr = new String(tokenContent);
            ObjectMapper objectMapper = new ObjectMapper();
            mapLayout = objectMapper.readValue(tokenStr, MapLayout.class);

            jsonResponse.setError(false);
            jsonResponse.setData(mapLayout.mapElements);

//			appTokenResponse.access_token = "YWMtP-4tpmtSEeSMTOdECM5HqQAAAUre-pqSfzvgngkXi5NrMy5gCWRTskUnang";
//			appTokenResponse.application = "b5485ea0-6995-11e4-b6c6-c119727ee8d7";
//			appTokenResponse.expires_in = "5184000";
//        }

        return jsonResponse;
    }
}
