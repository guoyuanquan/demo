package com.example.demo.controller.image;


import com.aliyun.openservices.shade.org.apache.commons.lang3.StringUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.example.demo.controller.elastaticsearch.EsUtils;
import com.example.demo.model.ImageExifDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ImageController {

    @Resource
    private EsUtils esUtils;

    public  void getImageDto(String imageUrl) throws Exception{
        File file = new File(imageUrl);
        if(file.getName().endsWith(".JPG")){
            Map exifMap = readPicExifInfo(file);
            String fileName= file.getName();
            String absolutePath = file.getAbsolutePath();
            ImageExifDto imageExifDto = printPicExifInfo(exifMap);
            imageExifDto.setAbsolutePath(absolutePath);
            imageExifDto.setFileName(fileName);
            String url = absolutePath.replace("/app/wrj/ydyp/","74.10.28.82:8888/images/");
            imageExifDto.setUrl(url);
            esUtils.save(imageExifDto);
        }
    }

    public static Map<String, String> readPicExifInfo(File file) throws Exception{
        Map<String, String> map = new LinkedHashMap<>();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                // 输出所有属性
//                System.out.format(
//                        "[%s] - %s = %s\n", directory.getName(), tag.getTagName(), tag.getDescription());
                map.put(tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }
        return map;
    }



    public static ImageExifDto printPicExifInfo(Map<String,String> map){
        ImageExifDto dto = new ImageExifDto();
        String[] strings = new String[]{"GPS Version ID","GPS Latitude","GPS Longitude","GPS Altitude", "Date/Time"};
        String[] names = new String[]{"版本","经度","纬度","高度","UTC时间截","gps日期","拍摄时间"};
        for(int i = 0;i < strings.length;i++){
            if(map.containsKey(strings[i])){
                if ("GPS Latitude".equals(strings[i])){
                    dto.setLatitude(latLng2Decimal(map.get(strings[i])));
                }else if("GPS Longitude".endsWith(strings[i])){
                    dto.setLongitude(latLng2Decimal(map.get(strings[i])));
                }else if("GPS Altitude".equals(strings[i])){
                    dto.setAltitude(Double.valueOf(StringUtils.substringBefore(map.get(strings[i]),"metres")));
                }else if (("Date/Time").equals(strings[i])){
                    dto.setDateTime(map.get(strings[i]));
                }
            }
        }
        return dto;
    }

    public static double latLng2Decimal(String gps) {
        String a = gps.split("°")[0].replace(" ", "");
        String b = gps.split("°")[1].split("'")[0].replace(" ", "");
        String c = gps.split("°")[1].split("'")[1].replace(" ", "").replace("\"", "");
        double gps_dou = Double.parseDouble(a) + Double.parseDouble(b) / 60 + Double.parseDouble(c) / 60 / 60;
        return gps_dou;
    }

}
