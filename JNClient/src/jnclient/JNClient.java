package jnclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class JNClient {

    private final String outIp;
    private final Integer outPort;
    private final String serverIp;
    private final Integer serverPort;
    private final Integer connectPort;
    private final Integer needPort;
    private Socket mainSocket;

    public JNClient(String outIp, Integer outPort, String serverIp, Integer serverPort, Integer needPort) {
        this.outIp = outIp;
        this.outPort = outPort;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.needPort = needPort;
        connectPort = needPort + 1;
    }

    public void start() throws IOException {
        mainSocket = new Socket(serverIp, serverPort);
        System.out.println("Connect to " + serverIp + ":" + serverPort);
        write(mainSocket, ("LISTENPORT&" + needPort + "\r\n"));
        while (!mainSocket.isClosed()) {
            String s = read(mainSocket);
            String[] strs = s.split("\r\n");
            for (String ss : strs) {
                doCommand(ss);
            }
        }
    }

    public void doCommand(String com) throws IOException {
        String name = com;
        if (com.contains("&")) {
            name = com.split("&")[0];
        }
        if (name.contains("NEEDCONNECT")) {
            System.out.println(name);
            Socket s1 = new Socket(serverIp, connectPort);
            Socket s2 = new Socket(outIp, outPort);
            new JNThread(s1, s2).start();
        }
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
