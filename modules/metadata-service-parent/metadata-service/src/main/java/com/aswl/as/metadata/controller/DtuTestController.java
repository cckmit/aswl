package com.aswl.as.metadata.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("dtuTest")
@Api("DTU测试")
@Slf4j
public class DtuTestController {

    @Autowired
    public SimpMessagingTemplate template;

    @PostMapping("/data")
    @ApiOperation("删试打印")
    private Map data(@RequestBody String data){
        log.info("data >>> {}", data);
        template.convertAndSend("/queue/dtuMsg", data);
        Map response = new HashMap();
        response.put("code", 0);
        response.put("msg", "success");
        return response;
    }
}
