package com.side.freedomdaybackend.domain;

import com.side.freedomdaybackend.common.exception.CustomException;
import com.side.freedomdaybackend.common.exception.ErrorCode;
import com.side.freedomdaybackend.common.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ApiResponse<String> getMappingTest() {
        String message = "테스트 성공";
        return new ApiResponse<String>(message);
    }

    @GetMapping("graph1")
    public ApiResponse<ArrayList<TestDto>> graph() {
        ArrayList<TestDto> list = new ArrayList<>();
        list.add(new TestDto("생활비", 9900000));
        list.add(new TestDto("학자금", 19800000));
        list.add(new TestDto("자동차", 9900000));
        list.add(new TestDto("기타", 59400000));
        list.add(new TestDto("주택자금", 99000000));
        return new ApiResponse<ArrayList<TestDto>>(list);
    }

    @GetMapping("/error")
    public void errorTest() {
        String message = "테스트 성공";
        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}

@Data
@AllArgsConstructor
class TestDto {
    String name;
    long amount;
}
