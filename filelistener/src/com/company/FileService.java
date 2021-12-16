package com.company;

import java.io.*;
import java.nio.file.*;
import java.util.Timer;
import java.util.TimerTask;

public class FileService {

    public static String path = "/opt/allison/images";

    public static void getFile() throws FileNotFoundException, IOException {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                WatchKey key;
                try {
                    WatchService watchService = FileSystems.getDefault().newWatchService();
                    Paths.get(path).register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                    while (true) {
                        File file = new File(path);//path为监听文件夹
                        //File[] files = file.listFiles();
                        System.out.println("****Start listening..****");
                        key = watchService.take();//没有文件增加时，阻塞在这里
                        for (WatchEvent<?> event : key.pollEvents()) {
                            String fileName = path+"/"+event.context();
                            System.out.println("****File added through: "+ fileName + "****");
                            //File file1 = files[files.length-1];//获取最新文件
                            File file1 = new File(fileName);
                            System.out.println(file1.getName());//根据后缀判断
                            uploadFile(file1,uploadAddres);//上传服务器
                        }if (!key.reset()) {
                            break; //中断循环
                        }
                    }
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 2000 , 3000);
    }

    final static String uploadAddres = "/data1/testUpload";
    public static String uploadFile(File file , String dir) throws FileNotFoundException, IOException {
        String imgURL = null;
        try {
            InputStream in = new FileInputStream(file);
            System.out.println("****Directory：" + dir+"****");
            // 获取文件名称
            String fileName = file.getName();
            // 路径和文件名丢进file对象里
            File uploadFile = new File(dir, fileName);
            // 输出流
            OutputStream out = new FileOutputStream(uploadFile);
            // 设置文件大小1MB
            byte[] buffer = new byte[1024 * 1024];
            int length;
            // 用循环从流中读取文件的大小
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            // 设置图片标题和全路径
            imgURL = "UploadFile/" + fileName;

            System.out.println("****Absolute address: "+imgURL+"****");
            // 关闭输入流输出流，释放内存
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgURL;
    }
}
