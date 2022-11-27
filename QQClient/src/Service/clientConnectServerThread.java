package Service;

import qqCommon.Message;
import qqCommon.MessageType;

import java.io.*;
import java.net.Socket;

//客户端与服务器连接的线程,负责接收服务器的消息
public class clientConnectServerThread extends Thread {
    private Socket socket;
    private boolean isLive = true;

    public clientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (isLive) {
            //等待获得服务器消息
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送消息，线程则会阻塞在这里 Message msg = (Message) ois.readObject();
                Message msg = (Message) ois.readObject();
                if (msg.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND))
                    System.out.println(msg.getContend());
                    //当接收到服务器的关闭 消息
                else if(msg.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    manageClientConnectServerThread.removeThread(msg.getGetter());
                    ois.close();
                    socket.close();
                    System.out.println("客户端"+msg.getGetter()+"关闭");
                    isLive =false;
                }//私聊消息
                else if(msg.getMesType().equals(MessageType.MESSAGE_COM_MSG)){
                    System.out.println(msg.getSender()+" "+msg.getSendTime());
                    System.out.println(msg.getContend());
                } else if (msg.getMesType().equals(MessageType.MESSAGE_ALL_MSG)) {
                    System.out.println(msg.getSender()+"对大家说："+msg.getContend());
                }//接收到文件后
                else if (msg.getMesType().equals(MessageType.MESSAGE_SEND_PICTURE)) {
                    byte[] bytes = msg.getBytes();
                    FileOutputStream fos = null;
                    fos = new FileOutputStream(msg.getDrc());
                    fos.write(bytes);

                    fos.close();
                    System.out.println("您已经接收到"+ msg.getSender()+"发来的文件到"+msg.getDrc());
                } else{

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
