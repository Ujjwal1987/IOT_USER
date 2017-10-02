package com.example.chase.iot_user;

import java.net.Socket;

/**
 * Created by chase on 01-05-2017.
 */

public class Socket_handler {

    private static Socket socket;

    public static synchronized Socket getSocket() {
        return socket;
    }

    public static synchronized Socket_handler setSocket(Socket socket) {
        Socket_handler.socket = socket;
        return null;
    }
}
