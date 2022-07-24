package com.aswl.as.metadata.websocket.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

@Data
@AllArgsConstructor
public class SocketUser implements Principal {

    private String userName;

    @Override
    public String getName() {
        return userName;
    }
}
