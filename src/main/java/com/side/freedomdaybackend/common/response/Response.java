package com.side.freedomdaybackend.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

    private boolean success;

    private String code;

    private String message;

}
