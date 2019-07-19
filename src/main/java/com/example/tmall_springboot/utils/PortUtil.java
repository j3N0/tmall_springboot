package com.example.tmall_springboot.utils;

import java.io.IOException;
import java.net.ServerSocket;

public class PortUtil {

    public static boolean checkPort(int port) {
        try {
            ServerSocket ss = new ServerSocket(port);
            ss.close();
            return false;
        } catch (java.net.BindException e) {
            return true;
        } catch (IOException e) {
            return true;
        }
    }
}
