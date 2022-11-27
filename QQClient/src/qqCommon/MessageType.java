package qqCommon;

public interface MessageType {
    //1.在接口中定义了一些常量
    //2.不同的常量值，表示不同的消息类型

    String MESSAGE_LOGIN_SUCCEED = "1"; //表示登录成功
    String MESSAGE_LOGIN_FAIL = "2"; //表示登录失败
    String MESSAGE_COM_MSG ="3"; //普通的信息包
    String MESSAGE_GET_ONLINE_FRIEND ="4"; //拉取用户在线列表
    String MESSAGE_RET_ONLINE_FRIEND ="5";//返回在线列表
    String MESSAGE_CLIENT_EXIT ="6";//客户端请求退出
    String MESSAGE_ALL_MSG="7"; //群发消息
    String MESSAGE_SEND_PICTURE="8";//发送文件


}
