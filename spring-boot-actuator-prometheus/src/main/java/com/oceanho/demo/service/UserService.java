package com.oceanho.demo.service;

import com.oceanho.demo.monitor.promethues.PromMetrics;
import com.oceanho.demo.repository.UserRepository;
import com.oceanho.demo.weixin.Auth;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private Auth auth;
    private PromMetrics prom;
    private UserRepository userRepo;

    public UserService(
            Auth auth,
            UserRepository userRepo, PromMetrics prom) {
        this.auth = auth;
        this.prom = prom;
        this.userRepo = userRepo;
    }

    public boolean login(String userName, String password) {
        // 调用腾讯微信登录耗时埋点
        this.prom.recordLibCoreFuncLatency(
                "com.oceanho.demo.weixin", "Auth.getAccessToken", () -> {
                    // 调用腾讯微信执行结果埋点。
                    try {
                        Thread.sleep(2000);
                        this.prom.reportLibCoreFuncExecuteResult("com.oceanho.demo.weixin", "Auth.getAccessToken", "success", "");
                    } catch (Exception ex) {
                        this.prom.reportLibCoreFuncExecuteResult("com.oceanho.demo.weixin", "Auth.getAccessToken", "fail", ex.getMessage());
                    } finally {
                    }
                });

        // 执行数据库查询耗时埋点
        this.prom.recordLibCoreFuncLatency(
                "com.oceanho.demo.repository", "UserRepository.checkUser", () -> {
                    // 执行数据库查询埋点
                    try {
                        Thread.sleep(1500);
                        this.userRepo.checkUser(userName, password);
                        this.prom.reportLibCoreFuncExecuteResult("com.oceanho.demo.repository", "UserRepository.checkUser", "success", "");
                    } catch (Exception ex) {
                        this.prom.reportLibCoreFuncExecuteResult("com.oceanho.demo.repository", "UserRepository.checkUser", "fail", ex.getMessage());
                    } finally {
                    }
                });


        // 执行缓存操作耗时埋点
        this.prom.recordLibCoreFuncLatency(
                "com.oceanho.demo.cache", "UserCache.storeSession", () -> {
                    // 执行缓存操作结果埋点
                    try {
                        Thread.sleep(800);
                        this.prom.reportLibCoreFuncExecuteResult("com.oceanho.demo.cache", "UserCache.storeSession", "success", "");
                    } catch (Exception ex) {
                        this.prom.reportLibCoreFuncExecuteResult("com.oceanho.demo.cache", "UserCache.storeSession", "fail", ex.getMessage());
                    } finally {
                    }
                });

        return true;
    }
}
