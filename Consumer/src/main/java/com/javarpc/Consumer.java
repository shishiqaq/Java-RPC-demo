package com.javarpc;

import com.javarpc.proxy.ProxyFactory;

public class Consumer {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class, "1.0");
        String result = helloService.sayHello("hello");
        System.out.println(result);
    }
}
