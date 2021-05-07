package com.cs.im.client.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginLock  {

    private CountDownLatch countDownLatch;

    public LoginLock(int latch){
        countDownLatch = new CountDownLatch(latch);
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }

    private AtomicBoolean LOGIN_ATOMOC = new AtomicBoolean(false);

    public void setLock(Boolean bool){
        LOGIN_ATOMOC.set(bool);
        if (bool){
            countDownLatch.countDown();
        }
    }
}
