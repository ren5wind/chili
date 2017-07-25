package com.topunion.chili.net.request_interface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Shawn on 7/19/17.
 *
 * 9.31	新增极光用户
 9.31.1	请求格式
 URL	/app/push/jMessage/registerUser
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 userId	true	String	用户Id

 9.31.2	正常返回结果
 返回键	类型	返回值	说明
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 username	String	用户Id

 {
 "state" : 200,
 "data" : "[{\"username\":\"17070500000003\"}]"
 }

 */

public class AddJMessageUser {

    public interface IAddJMessageUser {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/push/jMessage/registerUser")
        Call<AddJMessageUser.AddJMessageUserResponse> addJMessageUser(@Body AddJMessageUser user);
    }

    private String userId;

    public AddJMessageUser(String userId) {
        this.userId = userId;
    }

    public static class AddJMessageUserResponse {
        public static class User {
            public String username;
        }
        public int state;
        public List<User> data;
    }
}
