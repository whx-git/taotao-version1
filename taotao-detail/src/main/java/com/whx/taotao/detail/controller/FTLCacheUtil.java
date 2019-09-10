package com.whx.taotao.detail.controller;

import freemarker.template.Template;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FTLCacheUtil {

    private String cachePath;
    private FreeMarkerConfigurer freeMarkerConfigurer;
    private ThreadLocal<Map> root=new ThreadLocal<>();

    public FTLCacheUtil(String cachePath, FreeMarkerConfigurer freeMarkerConfigurer) {
        this.cachePath = cachePath;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    public void addAttribute(String key,Object value){
        if (root.get()==null) {
            root.set(new HashMap());
        }
        root.get().put(key, value);
    }

    public void processFtl(String cacheKey, String ftlName, HttpServletResponse response) throws Exception{
        File cacheFile=new File(cachePath,cacheKey+ftlName);
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (cacheFile.exists()){

            BufferedReader bufferedReader=new BufferedReader(new FileReader(cacheFile));
            String temp=null;
            while ((temp=bufferedReader.readLine())!=null){
                writer .println(temp);
            }
            writer.flush();
        }else {



            //加载模板文件
            Template t1 = freeMarkerConfigurer.getConfiguration().getTemplate(ftlName);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            t1.process(root.get(), new OutputStreamWriter(byteArrayOutputStream));
            byte[] bytes = byteArrayOutputStream.toByteArray();

            String result=new String(bytes);

            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(cacheFile));
            bufferedWriter.write(result);
            bufferedWriter.close();


            writer.write(result);
            writer.flush();
        }

    }


}
