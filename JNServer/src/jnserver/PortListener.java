package jnserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class PortListener {

    ServerSocket ss1 = null;
    int port;
    Socket clientSocket;

    public PortListener(int port, Socket clientSocket) throws IOException {
        this.port = port;
        this.clientSocket = clientSocket;
    }

    public void start() throws IOException {
        ServerSocket ss;
        try {
            ss = new ServerSocket(port);
        } catch (IOException ex) {
            return;
        }
        while (!ss.isClosed() && !clientSocket.isClosed()) {
            Socket s = ss.accept();
            write(clientSocket, "NEEDCONNECT\r\n");
            if (ss1 == null) {
                ss1 = new ServerSocket(port + 1);
            }
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (!ss1.isClosed() && !clientSocket.isClosed()) {
                            Socket s1 = ss1.accept();
                            new JNThread(s1, s).start();
                            break;
                        }
                    } catch (IOException ex) {
                    }
                }
            }.start();
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
