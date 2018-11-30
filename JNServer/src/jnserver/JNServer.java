package jnserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class JNServer {

    int selfPort;

    public JNServer(int selfPort) {
        this.selfPort = selfPort;
    }

    public void start() throws IOException {
        ServerSocket ss = new ServerSocket(selfPort);
        while (!ss.isClosed()) {
            Socket s = ss.accept();
            String com = read(s);
            String[] strs = com.split("\r\n");
            for (String sss : strs) {
                doCommand(sss, s);
            }
        }
    }

    public void doCommand(String com, Socket s) throws IOException {
        new Thread() {
            @Override
            public void run() {
                String name = com;
                if (com.contains("&")) {
                    name = com.split("&")[0];
                }
                if (name.equalsIgnoreCase("LISTENPORT")) {
                    int port = Integer.parseInt(com.split("&")[1].trim());
                    if (port < 65534) {
                        System.out.println(name + "&" + port);
                        try {
                            new PortListener(port, s).start();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        }.start();
    }

    public void write(Socket s, String str) throws IOException {
        s.getOutputStream().write(str.getBytes());
    }

    public String read(Socket s) throws IOException {
        byte[] data = new byte[1024];
        InputStream is = s.getInputStream();
        while (is.read(data) != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append(new String(data));
            return sb.toString();
        }
        return null;
    }
}
