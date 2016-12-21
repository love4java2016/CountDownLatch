package com;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        // 倒数计数器
        final CountDownLatch begin = new CountDownLatch(1);
        // 倒数计数器
        final CountDownLatch end = new CountDownLatch(10);
        // 十名枪手
        ExecutorService execu = Executors.newFixedThreadPool(10);
        // 模拟招募10名枪手到齐后开动大巴，送往“战场”
        for (int i = 0; i < 10; i++) {
            final int NO = i + 1;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 等待招募者下令招募
                        begin.await();
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("枪手:" + NO + "到了");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // 每到达一个枪手，计数器减1
                        end.countDown();
                    }
                }
            };
            execu.submit(r);
        }
        // begin倒数计数器减1为0，则开始下招募令，一声令下
        begin.countDown();
        System.out.println("开始招募。。。。");
        // 等待所有的枪手到达
        end.await();
        // 所有枪手到达
        System.out.println("所有枪手到达,开动大巴,送往目的地。。。。");
        execu.shutdown();
    }
}