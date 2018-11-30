package jnclient;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String a, b;
        Integer c, d, e;
        System.out.print("本地IP:");
        a = sc.next();
        System.out.print("本地Port:");
        c = sc.nextInt();
        System.out.print("服务器IP:");
        b = sc.next();
        System.out.print("服务器Port:");
        d = sc.nextInt();
        System.out.print("需要监听的Port:");
        e = sc.nextInt();
        JNClient jnc = new JNClient(a, c, b, d, e);
        jnc.start();
    }

}
