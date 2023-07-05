package com.javarpc;

public class HelloServiceImpl2 implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello2: " + name;
    }
}
