package com.javarpc.protocol;

import com.javarpc.common.Invocation;
import com.javarpc.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerHandler {

    public void handler(HttpServletRequest req, HttpServletResponse resp){
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            String interfaceName = invocation.getInterfaceName();

            Class classImpl = LocalRegister.get(interfaceName, invocation.getVersion());
            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            String  result = (String) method.invoke(classImpl.newInstance(), invocation.getParameters());

            IOUtils.write(result, resp.getOutputStream());

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
