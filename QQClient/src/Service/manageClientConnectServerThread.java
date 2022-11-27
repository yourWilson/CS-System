package Service;

import qqCommon.User;

import java.util.HashMap;

public class manageClientConnectServerThread {
    private static HashMap<String,clientConnectServerThread>  hm =new HashMap<>();

    //增加一个用户和所对应的线程
    public static void addThread(String username,clientConnectServerThread ccst){
        hm.put(username,ccst);
    }

    //通过用户名获得一个线程
    public static clientConnectServerThread getThread(String username){
        return hm.get(username);
    }
    public  static  void removeThread(String username){
        hm.remove(username);
    }

}
