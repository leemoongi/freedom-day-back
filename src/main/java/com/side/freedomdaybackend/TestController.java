package com.side.freedomdaybackend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/test")
public class TestController {

    @GetMapping
    public String getMappingTest() {
        String message = "테스트 성공";
        return message;
    }

}
