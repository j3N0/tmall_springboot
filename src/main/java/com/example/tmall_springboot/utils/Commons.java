package com.example.tmall_springboot.utils;

import java.util.Random;

public class Commons {

    static Random r = new Random();

    public static int RandomInt(int max) {
        return r.nextInt(max);
    }
}
