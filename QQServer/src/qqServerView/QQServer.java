package qqServerView;

import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class QQServer {
    private ServerSocket ss =null;

    private static HashMap<String,User> hm = new HashMap<>();

    static {
        hm.put("100",new User("100","123456"));
        hm.put("200",new User("200","123456"));
        hm.put("至尊宝",new User("至尊宝","123456"));
        hm.put("紫霞仙子",new User("紫霞仙子","123456"));
        hm.put("菩提老祖",new User("菩提老祖","123456"));
    }

    public boolean isValid(String username,String passwd){
        if(hm.get(username)==null)
            return false;
        else if(!hm.get(username).getPasswd().equals(passwd))
            return false;
        return true;
    }
    public QQServer(){

        try {
            System.out.println("服务器等待客户端连接。。。");
            ss = new ServerSocket(8888);
            while(true) {

                //监听客户端的消息
                Socket socket = ss.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User)ois.readObject();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message message = new Message();
                //先假定成立,如果成立发送信息给客户端
                if (isValid(user.getName(), user.getPasswd())){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //发送message
                    oos.writeObject(message);
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user, socket);
                    serverConnectClientThread.start();
                    ManageThread.addThread(user.getName(), serverConnectClientThread);

                }else{//登录失败
                    System.out.println("用户"+user.getName()+ " "+user.getPasswd() +"验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
