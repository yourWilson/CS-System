package qqServerView;

import com.sun.deploy.net.proxy.ProxyUnavailableException;
import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class ServerConnectClientThread extends Thread{
    User user;
    Socket socket;

    boolean isLive =true;
    public ServerConnectClientThread(User user, Socket socket) {
        this.user = user;
        this.socket = socket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    @Override
    public void run() {
        while(isLive){
            try {
                System.out.println("服务器保持与客户端"+user.getName()+"通讯。。。");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(user.getName()+" 1");
                    String m="";
                    //将 返回信息的类型改为 返回在线列表 类型
                    message.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message.setGetter(message.getSender());
                    HashMap<String, ServerConnectClientThread> hm = ManageThread.getHm();
                    Set<String> strings = hm.keySet();
                    for(String s:strings){
                        m += s+" ";
                    }

                    message.setContend(m);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                }
                //客户端请求关闭Socket时
                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(user.getName()+" 2");
                    //将消息的类型设置为EXIT，将接收者设置 为发送者名字，并将信息打包好发给客户端
                    message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
                    message.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);

                    //将与该客户端连接的线程从hashmap中移除，并关闭资源
                    ManageThread.removeThread(message.getSender());
                    oos.close();
                    socket.close();
                    System.out.println("客服端"+message.getSender()+"断开与服务器的连接");
                    System.out.println("剩余的客服端有："+ManageThread.getAllUser());
                    isLive =false;
                }//普通私聊
                else if (message.getMesType().equals(MessageType.MESSAGE_COM_MSG)){
                    if(!ManageThread.getHm().containsKey(user.getName()))
                        System.out.println("用户不存在");
                    else{
                        System.out.println(user.getName()+" 3");
                        // &&ManageThread.getHm().get(message.getGetter())!=null) {//接收方在线
                        System.out.println("服务器将消息转发。。");
                        //为获得接收方的socket，需要获得对应的线程
                        Socket socket1 = ManageThread.getThread(message.getGetter()).getSocket();
                        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
                        oos.writeObject(message);
                    }

                }//群发消息
                else if (message.getMesType().equals(MessageType.MESSAGE_ALL_MSG)) {
                    Set<String> strings = ManageThread.getHm().keySet();
                    //遍历所有socket,并发送群发消息
                    for (String userName:strings) {
                        if(userName.equals(message.getSender()))
                            continue;
                        Socket socket1 =ManageThread.getThread(userName).getSocket();
                        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
                        oos.writeObject(message);
                    }
                    System.out.println(message.getSender()+"群发成功");
                }else if (message.getMesType().equals(MessageType.MESSAGE_SEND_PICTURE)){
                    Socket socket1 =ManageThread.getThread(message.getGetter()).getSocket();
                    ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
                    oos.writeObject(message);
                    System.out.println("服务器转发文件成功");
                }
                else {
                    System.out.println("暂时不做处理。。。");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
