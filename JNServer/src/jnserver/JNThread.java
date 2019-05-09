package jnserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class JNThread extends Thread {

    Socket client;
    Socket server;
    InputStream cis;
    OutputStream cos;
    InputStream sis;
    OutputStream sos;
    int len = 1;

    public JNThread(Socket client, Socket server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            cis = client.getInputStream();
            cos = client.getOutputStream();
            sis = server.getInputStream();
            sos = server.getOutputStream();
        } catch (IOException ex) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    byte[] data = new byte[len];
                    while (sis.read(data) != -1) {
                        cos.write(data);
                    }
                } catch (IOException ex) {
                    try {
                        client.close();
                        server.close();
                    } catch (IOException ex1) {
                    }
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    byte[] data = new byte[len];
                    while (cis.read(data) != -1) {
                        sos.write(data);
                    }
                } catch (IOException ex) {
                    try {
                        client.close();
                        server.close();
                    } catch (IOException ex1) {
                    }
                }
            }
        }.start();
    }
}
