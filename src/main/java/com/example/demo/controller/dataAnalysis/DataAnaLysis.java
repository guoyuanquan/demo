package com.example.demo.controller.dataAnalysis;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author：guoyq
 * @name：DataAnaLysis
 * @Date：2025/7/3 9:45
 * @Describetion:
 */
public class DataAnaLysis {

    public void  analysis(String msg){
//        判断
        String headMsg = msg.substring(0,5);
        if ("!BD2P".equals(headMsg)||"!BD1P".equals(headMsg)){
            JSONObject bdpData = BDPDataJson(msg.substring(5),headMsg);
        }
    }

//    解析
    public JSONObject BDPDataJson(String mainMsg, String headMsg){
        String bdpMsg = parseHexStr2Byte(mainMsg);
        JSONObject object = new JSONObject();
        object.put("trackId",Long.parseLong("0"+bdpMsg.substring(0,63), 2));
        object.put("deviceId",Integer.parseInt("00"+bdpMsg.substring(63, 93), 2));
        object.put("shipType",Integer.parseInt(bdpMsg.substring(93, 101), 2));
        object.put("workType",Integer.parseInt(bdpMsg.substring(101, 109), 2));
        object.put("workWay",Integer.parseInt(bdpMsg.substring(109, 117), 2));
        object.put("sendTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .format(new Date(Long.parseLong(bdpMsg.substring(117,181), 2))));
        object.put("locationTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .format(new Date(Long.parseLong(bdpMsg.substring(181,245), 2))));
        object.put("online",Integer.parseInt("0000000" + bdpMsg.substring(245, 246), 2));
        object.put("shipLength",Short.parseShort("000" + bdpMsg.substring(246, 259), 2)/10.0);
        object.put("shipWidth",Short.parseShort("0000" + bdpMsg.substring(259, 271), 2)/10.0);
        object.put("texture",Integer.parseInt(bdpMsg.substring(271, 279), 2));
        object.put("longitude",(double)Integer.parseInt("000"+ bdpMsg.substring(279, 308), 2)/1000000);
        object.put("latitude",(double)Integer.parseInt("0000" + bdpMsg.substring(308, 336), 2)/1000000);
        object.put("direction",Short.parseShort("0000" + bdpMsg.substring(336, 348), 2)/10.0);
        object.put("speed",Short.parseShort("000000" + bdpMsg.substring(348, 358), 2)/10.0);
        object.put("positionStatus",Short.parseShort("000000"+bdpMsg.substring(358,360),2));
        object.put("tiltState",Short.parseShort(bdpMsg.substring(360,368),2));
        object.put("kwh",Integer.parseInt("000000" + bdpMsg.substring(368, 376), 2));
        return object;
    }
    /**
     * 16进制字符串转二进制字符串
     * @param hexStr
     * @return
     */
    public String parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            bytes[i] = (byte) (high * 16 + low);
        }

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            //补齐8位
            result.append(String.format(
                    "%08d",
                    Integer.parseInt(Long.toString(bytes[i] & 0xff, 2))
            ));
        }
        return result.toString();
    }

}
