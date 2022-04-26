package com.alwertus.spassistent.common.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomStringGenerator {
    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateString(int length) {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

}
