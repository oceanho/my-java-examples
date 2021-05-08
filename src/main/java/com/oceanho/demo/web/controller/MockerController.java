package com.oceanho.demo.web.controller;

import com.oceanho.demo.monitor.promethues.PromMetrics;
import com.oceanho.demo.monitor.promethues.tag.common.Http;
import com.oceanho.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockerController {

    private PromMetrics prom;
    private UserService userSvc;
    public MockerController(PromMetrics prom, UserService userSvc){
        this.prom = prom;
        this.userSvc = userSvc;
    }

    @ResponseBody
    @RequestMapping("/mock-metrics")
    public String mockMetrics() {

        this.prom.incUserLoginParamCheckErr("empty-account");
        this.prom.incUserLoginParamCheckErr("invalid-format-account");

        this.prom.incUserLoginParamCheckErr("empty-password");
        this.prom.incUserLoginParamCheckErr("invalid-format-password");

        this.prom.incUserLoginWxErr("timeout","https://api.open.weixin.com/api/v1/oauth/token", Http.MethodPost);
        this.prom.incUserLoginWxErr("invalid-access-token","https://api.open.weixin.com/api/v1/oauth/user/info", Http.MethodPost);

        this.prom.incUserLoginInternalSystemErr("mysql","timeout");
        this.prom.incUserLoginInternalSystemErr("mysql","syntax-err");

        this.prom.incUserLoginInternalSystemErr("redis","timeout");

        this.userSvc.login("admin","admin");
        return "ok";
    }
}
