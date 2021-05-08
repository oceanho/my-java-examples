package com.oceanho.demo.monitor.promethues;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PromMetrics {
    private final String prefix="cloudlive.";
    private final MeterRegistry registry;

    public PromMetrics(MeterRegistry registry){
        this.registry = registry;
        this.registry.config().meterFilter(new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                return id;
                //return id.withName(prefix+"."+id.getName());
            }
        });
    }

    /***
     * 用户登录【参数校验】失败错误记数
     * @param err 指定参数校验错误的关键字，该参数应该定义成固定字符串，比如 invalid-format-account
     */
    public void incUserLoginParamCheckErr(String err){
        this.registry.counter(prefix+"user.login.param.check.err","err", err).increment();
    }

    /***
     * 用户登录【微信交互】失败错误记数
     * @param err 指定错误的关键字，该参数应该定义成固定字符串，比如 timeout/invalid-token
     * @param url 请求全地址
     * @param method 请求方法，比如 http GET
     */
    public void incUserLoginWxErr(String err, String url, String method){
        this.registry.counter(prefix+"user.login.wx.err","err", err, "url", url, "method", method).increment();
    }

    /***
     * 用户登录【系统内部业务逻辑失败】失败错误记数
     * @param type 指定错误的类型，比如 mysql, redis
     * @param err 指定错误的关键字，比如 timeout, syntax-err
     */
    public void incUserLoginInternalSystemErr(String type, String err){
        this.registry.counter(prefix+"user.login.internal.system.err","type", type, "err", err).increment();
    }


    /***
     * 公用【函数】执行结果记数
     * @param pkg 执行函数的包名，比如 com.oceanho.demo.monitor.promethues
     * @param func 执行函数名，比如 OrderService.createOrder()
     * @param result 函数执行结果，success/fail
     * @param reason 直接结果描述字符串，比如 connect to mysql timeout.
     */
    public void reportLibCoreFuncExecuteResult(String pkg, String func, String result, String reason){
        this.registry.counter(prefix+"core.func.result",
                "pkg", pkg, "func", func, "result", result, "reason", reason).increment();
    }

    /***
     * 公用【核心函数】执行耗时统计
     * @param pkg 执行函数的包名，比如 com.oceanho.demo.monitor.promethues
     * @param func 执行函数名，比如 OrderService.createOrder()
     * @param runnable 需要执行耗时记录的函数，() => myfunc()
     */
    public void recordLibCoreFuncLatency(String pkg, String func,Runnable runnable){
        this.registry.timer(prefix+"core.func.latency",
                "pkg", pkg, "func", func).record(runnable);
    }

    /***
     * 公用【核心函数】执行耗时统计
     * @param pkg 执行函数的包名，比如 com.oceanho.demo.monitor.promethues
     * @param func 执行函数名，比如 OrderService.createOrder()
     * @param duration 执行函数耗时，() => myfunc()
     */
    public void recordLibCoreFuncLatencyDuration(String pkg, String func,Duration duration){
        this.registry.timer(prefix+"core.func.latency",
                "pkg", pkg, "func", func).record(duration);
    }
}
