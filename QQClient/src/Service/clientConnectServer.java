package Service;

//该类包括 验证客户端与服务器的连接功能,实现与服务器的通讯

import qqCommon.Message;
import qqCommon.MessageType;
import qqCommon.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class clientConnectServer {
    private User user = new User();

    private Socket socket;

    //检查用户是否合法
    public boolean checkUser(String username,String passwd){
        boolean key = false;

        user.setName(username);
        user.setPasswd(passwd);
        //将用户名和密码保存在User对象里方便对象流传送

        //使用对象流 将信息发送给服务器 验证
        try {
             socket = new Socket(InetAddress.getLocalHost(), 8888);
            //User已经实现了Serializable，可序列化.
            // 可在网络上通过对象流ObjectOutputStream传输
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);

            //对象输出流传出 Message 对象获得 消息的内容和验证信息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message)ois.readObject();
//            System.out.println(msg.getMesType());
            if(msg.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){
                //如果成功的话就打开客户端和服务器连接的线程，并方法返回true
                key =true;
                clientConnectServerThread cCST = new clientConnectServerThread(socket);
                cCST.start();
                //添加线程到hashmap里面
                manageClientConnectServerThread.addThread(username,cCST);

            }
            else {
                socket.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return key;
    }

    //向服务器发送 拉取在线用户请求
    public void getFriendList(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getName());
        try {
//            socket = manageClientConnectServerThread.getThread(user.getName()).getSocket();
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(manageClientConnectServerThread.getThread(user.getName()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //发送退出连接消息
    public void clientExit(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getName());
        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(
                    manageClientConnectServerThread.getThread(user.getName()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToFriend(String senderName,String getterName,String contend){
        //将Message类型设置为普通消息，设置发送时间，发送方和接收方以及内容
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COM_MSG);//普通消息类型
        message.setSender(senderName);
        message.setGetter(getterName);
        message.setContend(contend);
        message.setSendTime(getCurrentDate());
        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(
                    manageClientConnectServerThread.getThread(user.getName()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //显示当前时间
    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // format返回String类型 yyyy-MM-dd HH:mm:ss
                return simpleDateFormat.format(date);
    }

    public void sendToAll(String contend){
        //设置Message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_ALL_MSG);
        message.setSender(user.getName());
        message.setContend(contend);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            ObjectOutputStream oos = new ObjectOutputStream(
//                    manageClientConnectServerThread.getThread(user.getName()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendFile(String getterName,String sourcePath,String getterPath){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_SEND_PICTURE);
        message.setDrc(getterPath);
        message.setSrc(sourcePath);
        message.setSender(user.getName());
        message.setGetter(getterName);

        FileInputStream fis =null;
        byte[] bytes = new byte[(int)new File(sourcePath).length()];

        try {
            fis =new FileInputStream(sourcePath);
            //读取文件的 字节数组
            fis.read(bytes);

            message.setBytes(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(user.getName()+"向"+getterName+"发送文件成功！");

    }


}
