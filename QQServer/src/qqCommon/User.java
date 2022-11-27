package qqCommon;

import java.io.Serializable;

public class User implements Serializable {
    //保持版本一致
    private static final long serialVersionUID =1L;
    private String name;
    private String passwd;

    public User(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
