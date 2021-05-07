package com.cs.im.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatSendMessage extends Message {

    long listener;
    String msg;


    @Override
    int get_OrderType() {
        return chatSendOpType;
    }

    @Override
    int get_sequenceId() {
        return 0;
    }

    @Override
    String get_Content() {
        return null;
    }
}
