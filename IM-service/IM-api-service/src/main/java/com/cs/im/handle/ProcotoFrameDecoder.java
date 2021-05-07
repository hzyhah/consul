package com.cs.im.handle;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProcotoFrameDecoder extends LengthFieldBasedFrameDecoder {
    public ProcotoFrameDecoder(){
        super(1024,12,4,0,0);
    }
}
