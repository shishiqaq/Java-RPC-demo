package com.javarpc;

import java.util.HashMap;
import java.util.Map;

public class Provider {

    public static void main(String[] args) {
        Map<String, Class> serviceMap = new HashMap<>();
        serviceMap.put("1.0", HelloServiceImpl.class);
        serviceMap.put("2.0", HelloServiceImpl2.class);
        Bootstrap.start(serviceMap);

    }
}
