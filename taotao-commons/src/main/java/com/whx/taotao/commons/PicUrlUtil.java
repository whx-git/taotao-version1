package com.whx.taotao.commons;

public class PicUrlUtil {
    public static String genGroupPaths(String imageUrls,String nginxUrl){
        StringBuilder stringBuilder=new StringBuilder();
        String[] imageUrl = imageUrls.split(",");
        for (String s : imageUrl) {
            String replace = s.replace(nginxUrl, "");
            stringBuilder.append(replace);
            stringBuilder.append(",");
        }
        String result = stringBuilder.toString();
        String substring = result.substring(0, result.length() - 1);
        return substring;
    }
    public static String genPullPaths(String imageUrls,String nginxUrl){
        StringBuilder stringBuilder=new StringBuilder();
        String[] imageUrl = imageUrls.split(",");
        for (String s : imageUrl) {
            String url=nginxUrl+s;
            stringBuilder.append(url);
            stringBuilder.append(",");
        }
        String result = stringBuilder.toString();
        String substring = result.substring(0, result.length() - 1);
        return substring;
    }
}
