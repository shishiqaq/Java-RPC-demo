package com.javarpc;

import com.javarpc.common.URL;
import com.javarpc.protocol.HttpServer;
import com.javarpc.register.LocalRegister;
import com.javarpc.register.MapRemoteRegister;

import java.util.Map;

public class Bootstrap {

    public static void start(Map<String, Class> services){
        // local register
        for (Map.Entry<String, Class> entry : services.entrySet()) {
            System.out.println("register " + entry.getKey() + ":" + entry.getValue());
            LocalRegister.regist(HelloService.class.getName(), entry.getKey(), entry.getValue());
        }
        // remote register
        URL url = new URL("localhost", 8080);
        MapRemoteRegister.regist(HelloService.class.getName(), url);

        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostName(), url.getPort());
    }
}
