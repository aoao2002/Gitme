package com.example.ooad.UTil;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    @Value("file.upload.dir")
    private String uploadFilePath;

    public String saveFile(MultipartFile file) {
        JSONObject result = new JSONObject();
        if (file.isEmpty()) {
            result.put("error", "empty file!");
            return result.toString();
        }

        // 文件名
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        File fileTempObj = new File(uploadFilePath + "/" + fileName);
        // 检测目录是否存在
        if (!fileTempObj.getParentFile().exists()) {
            fileTempObj.getParentFile().mkdirs();
        }
        // 使用文件名称检测文件是否已经存在
        if (fileTempObj.exists()) {
            result.put("error", "文件已经存在!");
            return result.toString();
        }

        try {
            // 写入文件:方式1
            file.transferTo(fileTempObj);
            // 写入文件:方式2
//            FileUtil.writeBytes(file.getBytes(), fileTempObj);
        } catch (Exception e) {
            result.put("error", e.getMessage());
            return result.toString();
        }

        result.put("success", "文件上传成功!");
        return result.toString();
    }

    public String fileDownload(HttpServletResponse response, String fileName) throws JSONException, IOException {
        JSONObject result = new JSONObject();

        File file = new File(uploadFilePath + '/' + fileName);
        if (!file.exists()) {
            result.put("error", "下载文件不存在!");
            return result.toString();
        }

        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        // 原生的方式
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            result.put("error", e.getMessage());
            return result.toString();
        }
        result.put("success", "下载成功!");
        return result.toString();
    }

    public Map<String, Map> getFileSrc(String fileName){
        // 建立当前目录中文件的File对象
        File targetFile = new File(uploadFilePath + "/" + fileName);
        // 取得代表目录中所有文件的File对象数组
        File[] list = targetFile.listFiles();

        return null;

    }

//    文件复制 https://blog.csdn.net/u014263388/article/details/52098719
//    Having repository: repo\aoao@qq.com\theon\.git
//java.io.FileNotFoundException: repo\hermitian@git.com\theon\.git (拒绝访问。)
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        try (FileChannel outputChannel = new FileOutputStream(dest).getChannel();FileChannel inputChannel = new FileInputStream(source).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }
    }

    public static boolean isText(String filePath) {
        //打表判断文件是否为文本文件
        String[] textType = {".txt", ".java", ".c", ".cpp", ".h", ".py", ".html", ".css", ".js",
                ".json", ".xml", ".md", ".sql", ".yml", ".properties", ".sh", ".bat", ".log", ".conf", ".ini", ".gitignore"};
        for (String type : textType) {
            if (filePath.endsWith(type)) {
                return true;
            }
        }
//        Path path = Paths.get(filePath);
//        try{
//            String contentType = Files.probeContentType(path);
//            if (contentType != null && contentType.startsWith("text")) {
//                return true;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return false;
    }
}
