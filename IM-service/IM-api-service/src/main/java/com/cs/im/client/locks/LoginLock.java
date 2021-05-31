package com.cs.im.client.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginLock  {

    private CountDownLatch countDownLatch;
    private AtomicBoolean LOGIN_ATOMOC = new AtomicBoolean(false);

    public LoginLock(int latch){
        countDownLatch = new CountDownLatch(latch);
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }

    public void setLock(Boolean bool){
        LOGIN_ATOMOC.set(bool);
        if (bool){
            countDownLatch.countDown();
        }
    }

    public void countDown(){
        countDownLatch.countDown();
    }

    public boolean getLoginLockStatus(){
        return LOGIN_ATOMOC.get();
    }
}
