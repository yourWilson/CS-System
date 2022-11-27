package qqclinet.view;

import Service.clientConnectServer;
import com.sun.javaws.IconUtil;

import java.util.Scanner;

//客户端的菜单界面
public class QQView {

    private String key = "";
    private int keycode = 0;
    private boolean loop = true;//

    private clientConnectServer ccs; //用于连接服务器和注册服务器的成员

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("退出客户端");
    }

    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("==========欢迎登录网络通讯系统==========");
            System.out.println("\t\t1.登录系统");
            System.out.println("\t\t9.退出系统");
            System.out.println("请输入你的选择：");
            Scanner scanner = new Scanner(System.in);
//            keycode = scanner.nextInt();
            key = scanner.next();

            switch (key) {
                case "1":
                    System.out.println("请输入你的用户号：");
                    String userId = scanner.next();
                    System.out.println("请输入你的密码：");
                    String userPasswd = scanner.next();
                    //此时到服务器端进行验证合法性
                    //此时写一个类 用于验证用户名和密码的合法性
                    clientConnectServer ccs = new clientConnectServer();
                    if (ccs.checkUser(userId, userPasswd)) {
                        while (loop) {
                            System.out.println("==========欢迎(" + userId + ")登陆==========");
                            System.out.println("\t\t1.显示在线用户列表");
                            System.out.println("\t\t2.群发消息");
                            System.out.println("\t\t3.私聊消息");
                            System.out.println("\t\t4.发送文件");
                            System.out.println("\t\t9.退出系统");

                            key = scanner.next();
                            System.out.println("请输入你的选择：");
                            switch (key) {
                                case "1":
                                    System.out.println("==========显示在线用户列表==========");
                                    ccs.getFriendList();
                                    break;
                                case "2":
                                    System.out.println("==========群发消息==========");
                                    System.out.println("请发送您想对大家说的话：");
                                    String contend =scanner.next();
                                    ccs.sendToAll(contend);
                                    break;
                                case "3":
                                    System.out.println("==========私聊消息==========");
//                                    ccs.getFriendList();//显示在线好友列表
                                    System.out.println("请选择好友名字:");
                                    String friendName = scanner.next();
                                    System.out.println("请发送您要说的话:");
                                    String messageContend = scanner.next();
                                    ccs.sendMessageToFriend(userId, friendName, messageContend);

                                    break;
                                case "4":
                                    System.out.println("==========发送文件==========");
                                    System.out.println("请输入接收文件的用户：");
                                    String userName = scanner.next();
                                    System.out.println("请输入发送文件的完整路径（形式：d:\\1\\XXX.jpg）：");
                                    String sourcePath =scanner.next();
                                    System.out.println("请输入接收文件的完整路径（形式：d:\\2\\XXX.jpg）：");
                                    String getterPath =scanner.next();
                                    ccs.sendFile(userName,sourcePath,getterPath);
                                    break;
                                case "9":
                                    System.out.println("==========退出系统==========");
                                    ccs.clientExit();
                                    loop = false;
                                    break;
                            }
                        }
                    } else
                        System.out.println("登录失败");
                    break;
                case "9":
                    System.out.println("退出成功");
                    loop = false;
                    break;
            }

        }

    }

}
