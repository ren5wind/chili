package com.topunion.chili.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/9/2.
 */
@Entity
public class Notifiy extends BaseData {
    @Id
    private Long id;
    public String title;
    public String msg;
    public long time;
    public String url;
    @Generated(hash = 981085357)
    public Notifiy(Long id, String title, String msg, long time, String url) {
        this.id = id;
        this.title = title;
        this.msg = msg;
        this.time = time;
        this.url = url;
    }
    @Generated(hash = 155137460)
    public Notifiy() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
