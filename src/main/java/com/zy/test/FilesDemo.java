package com.zy.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FilesDemo {
    public static void main(String[] args) throws Exception{
        FilesDemo filesDemo = new FilesDemo();
        filesDemo.run();
    }
    private void run() throws IOException {
        File file = new File("/usr/");
//        System.out.println(file.getFreeSpace());
//        System.out.println(file.getTotalSpace());
//        System.out.println(file.getUsableSpace());
        System.out.println("*****************");
        File[] files = file.listFiles();
        for (File file1 : files) {
            System.out.println("name："+"\t"+file1.getName());
            System.out.println("是目录吗？" +"\t"+ file1.isDirectory());
            System.out.println("是文件吗？" +"\t"+ file1.isFile());
            System.out.println(FileUtils.sizeOf(file1));
        }


    }
}
