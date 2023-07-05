package com.javarpc;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello1: " + name;
    }
}
