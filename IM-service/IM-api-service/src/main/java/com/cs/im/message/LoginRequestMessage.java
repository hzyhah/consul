package com.cs.im.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginRequestMessage extends Message {

    String name;
    String password;
    String nickname;

    public LoginRequestMessage() {
    }

    @Override
    int get_OrderType() {
        return LoginRequestOpType;
    }

    @Override
    int get_sequenceId() {
        return 7;
    }

    @Override
    String get_Content() {
        return null;
    }
}
