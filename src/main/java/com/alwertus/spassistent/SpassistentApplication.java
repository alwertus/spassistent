package com.alwertus.spassistent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class SpassistentApplication {

    public static void main(String[] args) {
        String path = "testfile.txt";
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("ERROR! Cannot create testfile");
            e.printStackTrace();
        }
        SpringApplication.run(SpassistentApplication.class, args);
    }

}
