package cn.tju.tdwy.utils;

import java.io.*;
import java.net.Socket;

public class HashcodeUtils {

    public static boolean sendPicture(OutputStream outputStream, String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        return false;
    }

    public static String getMessage(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        int num = inputStream.read(bytes);
        String message = new String(bytes, 0, num, "UTF-8");
        System.out.println(message);
        return message;
    }

    public static String getJsonData(InputStream inputStream, int size) throws IOException {
        byte[] bytes = new byte[1024];
        StringBuilder stringBuilder = new StringBuilder();
        int len = 0;
        int hasReceive = 0;
        while (hasReceive != size) {
//            System.out.println(hasReceive);
            len = inputStream.read(bytes);
            hasReceive += len;
            stringBuilder.append(new String(bytes, 0, len, "UTF-8"));
        }
//        while ((len = inputStream.read(bytes)) != -1) {
//            System.out.println(len);
//            stringBuilder.append(new String(bytes, 0, len, "UTF-8"));
//        }
        return String.valueOf(stringBuilder);
    }

    public static boolean sendMessage(OutputStream outputStream, String message) throws IOException {
        System.out.println(message);
        outputStream.write(message.getBytes("UTF-8"));
        return false;
    }


    public static String getPictureHashcode(String filePath) {
        Socket socket = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        String jsonData2 = null;
        try {
            socket = new Socket("192.168.199.212", 9999);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            getMessage(inputStream);
            String fileInfo = new File(filePath).getName() + "|" + new File(filePath).length();
            sendMessage(outputStream, fileInfo);
            getMessage(inputStream);
            sendPicture(outputStream, filePath);
            getMessage(inputStream);
            sendMessage(outputStream, "OK!");
            String jsonSize = getMessage(inputStream);
            int size = Integer.parseInt(jsonSize);
            sendMessage(outputStream, "ready!");
            String jsonData = getJsonData(inputStream, size);
            jsonData2 = jsonData.substring(2,jsonData.length()-2);
            System.out.println(jsonData2);
            System.out.println(jsonData.getClass());

//        JSONObject jsonObject = JSONObject.fromObject(jsonData);
//        Map record = (Map)jsonObject.get(new File(filePath).getName());

            sendMessage(outputStream, "Goodbye!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e1) {

            }
            try {
                outputStream.close();
            } catch (IOException e2) {

            }
            try {
                socket.close();
            } catch (IOException e3) {

            }
        }
        return jsonData2;
    }

    public void traverseFolder2(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
            } else {
                for (File file2 : files) {
                    System.out.println("文件:" + file2.getAbsolutePath());
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

}
