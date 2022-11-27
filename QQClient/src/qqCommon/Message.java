package qqCommon;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID =1L;//版本号

    private String sender;  //发送方
    private String getter;  //接收方
    private String contend; //发送内容
    private String sendTime; //发送时间
    private String mesType; //消息类型

    private byte[] bytes;

    private int lens=0;



    private String drc;  //目标路径

    private String src; //源文件路径

    public int getLens() {
        return lens;
    }

    public void setLens(int lens) {
        this.lens = lens;
    }
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getDrc() {
        return drc;
    }

    public void setDrc(String drc) {
        this.drc = drc;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContend() {
        return contend;
    }

    public void setContend(String contend) {
        this.contend = contend;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
