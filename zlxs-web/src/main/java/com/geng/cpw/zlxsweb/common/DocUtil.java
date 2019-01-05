package com.geng.cpw.zlxsweb.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

public class DocUtil {
    public static void download(HttpServletRequest request, HttpServletResponse response, String newWordName, Map dataMap) {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");                                       //注意这里要设置编码

        //模板文件word.xml是放在WebRoot/template目录下的
        System.out.println(ResourceUtils.CLASSPATH_URL_PREFIX);

        try {
            configuration.setDirectoryForTemplateLoading(ResourceUtils.getFile("classpath:wordtemplate"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Template t = null;
        try {
            //word.xml是要生成Word文件的模板文件
            t = configuration.getTemplate("word.xml","utf-8");                  // 文件名 还有这里要设置编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        File outFile = null;
        Writer out = null;
        String filename = newWordName;
        try {
            outFile = new File(newWordName);
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFile),"utf-8"));                 //还有这里要设置编码

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            t.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(outFile));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            filename = URLEncoder.encode(filename, "utf-8");                                  //这里要用URLEncoder转下才能正确显示中文名称
            response.addHeader("Content-Disposition", "attachment;filename=" + filename+"");
            response.addHeader("Content-Length", "" + outFile.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(fis!=null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(toClient!=null){
                    toClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(outFile!=null&&outFile.exists()){
                outFile.delete();
            }

        }
    }
}


