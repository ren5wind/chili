package com.topunion.chili.net.request_interface;


import com.topunion.chili.net.response_model.BaseStateResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Shawn on 7/18/17.
 9.27	删除易投好友
 9.27.1	请求格式
 URL	/app/contacts/friend/deleteFriend
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 9.27.2	参数说明
 参数名	必选	类型及范围	说明
 userId	true	String	用户Id
 friendId	true	String	好友Id

 9.27.3	正常返回结果
 返回键	类型	返回值	说明
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体

 {
 "state" : 200,
 "data" : null
 }

 */

public class RemoveETFriend {

    public interface IRemoveETFriend {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/contacts/friend/deleteFriend")
        Call<BaseStateResponse> removeETFriend(@Field("userId") String userId, @Field("friendId") String friendId);
    }
}
