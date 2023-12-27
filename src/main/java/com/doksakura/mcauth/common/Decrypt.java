package com.doksakura.mcauth.common;

public class Decrypt {

    public static String encrypt(String raw) {
        StringBuilder result = new StringBuilder();

        for(char c : raw.toCharArray()) {
            result.append((char) (c + 10));
        }
        result.reverse();

        return result.toString();
    }

    public static String decrypt(String raw) {
        StringBuilder result = new StringBuilder();

        for(char c : raw.toCharArray()) {
            result.append((char) (c - 10));
        }
        result.reverse();

        return result.toString();
    }
}
