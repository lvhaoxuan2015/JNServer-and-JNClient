package jnserver;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("向穿透客户端开放的Port:");
        Integer port = sc.nextInt();
        System.out.println("run...");
        JNServer jns = new JNServer(port);
        jns.start();
    }

}
