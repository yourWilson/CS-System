package qqServerView;

import java.util.HashMap;
import java.util.Set;

public class ManageThread {
    private static HashMap<String,ServerConnectClientThread> hm =new HashMap<>();

    public static void addThread(String username,ServerConnectClientThread sccThread){
        hm.put(username, sccThread);
    }

    public static  ServerConnectClientThread getThread(String username){
        return hm.get(username);
    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    public static void setHm(HashMap<String, ServerConnectClientThread> hm) {
        ManageThread.hm = hm;
    }
    public static void removeThread(String username){
        hm.remove(username);
    }

    public static Set getAllUser(){
        return hm.keySet();
    }
}
