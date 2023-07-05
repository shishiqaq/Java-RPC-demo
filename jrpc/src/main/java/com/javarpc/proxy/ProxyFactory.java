package com.javarpc.proxy;

import com.javarpc.common.Invocation;
import com.javarpc.common.URL;
import com.javarpc.loadbalance.LoadBalance;
import com.javarpc.protocol.HttpClient;
import com.javarpc.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass, String version){

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                String mock = System.getProperty("mock");
                if(mock != null && mock.startsWith("return:")){
                    return mock.replace("return:","");
                }

                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(),
                method.getParameterTypes(), args, version);
                System.out.println("version: "+invocation.getVersion());
                HttpClient httpClient = new HttpClient();

                // service discovery
                List<URL> list = MapRemoteRegister.get(interfaceClass.getName());


                String result = "";
                int maxRetries = 3;
                int retryCount = 0;
                List<URL> invokedUrls = new ArrayList<>();

                while (retryCount < maxRetries) {
                    // load balance
                    list.removeAll(invokedUrls);

                    try {
                        URL url = LoadBalance.random(list);
                        invokedUrls.add(url);
                        result = httpClient.send(url.getHostName(), url.getPort(), invocation);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("URL list is empty.");
                        break;
                    } catch (Exception e) {
                        retryCount++;
                        System.out.println("Exception occurred when httpClient.send, retrying... (Retry count: " + retryCount + ")");
                        e.printStackTrace();
                    }
                }
                return result;
            }
        });
        return (T) proxyInstance;
    }
}
