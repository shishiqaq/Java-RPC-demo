package com.javarpc.register;

import com.javarpc.common.URL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRemoteRegister {
    private static Map<String, List<URL>> map = new HashMap<>();

    public static void regist(String interfaceName, URL url){
        List<URL> list = map.get(interfaceName);
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(url);

        map.put(interfaceName, list);
        saveFile();
    }

    public static List<URL> get(String interfaceName){
        map = getFile();
        return map.get(interfaceName);
    }

    private static void saveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./temp.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, List<URL>> getFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("./temp.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Map<String, List<URL>>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
