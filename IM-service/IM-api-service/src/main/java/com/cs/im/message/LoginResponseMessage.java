package com.cs.im.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseMessage extends Message {

    int status;
    long identityId;

    @Override
    int get_OrderType() {
        return LoginResponseOpType;
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
