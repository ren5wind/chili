package com.topunion.chili.net.request_interface;


import com.topunion.chili.net.response_model.BaseStateResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.


 9.23	修改易投好友备注

 9.23.1	请求格式
 URL	/app/contacts/friend/updateFriendNickname
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 9.23.2	参数说明
 参数名	必选	类型及范围	说明
 userId	true	String	用户Id
 friendId	true	String	好友Id
 nickname	true	String	备注内容

 9.23.3	正常返回结果
 返回键	类型	返回值	说明
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体

 {
 "state" : 200,
 "data" : null
 }

 */

public class UpdateETFriendNickname {

    public interface IUpdateETFriendNickname {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/contacts/friend/updateFriendNickname")
        Call<BaseStateResponse> updateETFriendNickname(@Query("userId") String userId, @Query("friendId") String friendId,
                                                       @Query("nickname") String nickname);
    }

}
