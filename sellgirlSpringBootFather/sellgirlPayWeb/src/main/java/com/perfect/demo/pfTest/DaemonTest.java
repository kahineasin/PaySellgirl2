package com.perfect.demo.pfTest;

import static java.util.concurrent.TimeUnit.*;   
public class DaemonTest {   
    public static void main(String[] args) throws InterruptedException {   
        Runnable r = new Runnable() {   
            public void run() {   
                for (int time = 10; time > 0; --time) {   
                    System.out.println("Time #" + time);   
                    try {   
                        SECONDS.sleep(2);   
                    } catch (InterruptedException e) {   
                        e.printStackTrace();   
                    }   
                }   
            }   
        };   
           
        Thread t = new Thread(r);   
        t.setDaemon(false);  // false时，可以守护主进程，true时不行
        t.start();   
           
        System.out.println("Main thread waiting...");   
        SECONDS.sleep(6);   
        System.out.println("Main thread exited.");   
    }   
} 