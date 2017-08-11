package com.topunion.chili.net.request_interface;


import com.topunion.chili.net.response_model.BaseStateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 * 9.30	退出群组
 * 9.30.1	请求格式
 * URL	/app/contacts/groupFriends/exitGroupFriends
 * 支持格式	JOSN
 * HTTP请求方式	POST
 * 是否登录验证
 * 请求数限制
 * <p>
 * 9.30.2	参数说明
 * 参数名	必选	类型及范围	说明
 * groupId	true	int	群组Id
 * acceptorId	true	String	群组成员Id
 * <p>
 * 9.30.3	正常返回结果
 * 返回键	类型	返回值	说明
 * state	int	结果代码信号	200结果成功,300 结果失败
 * data	Object	响应体
 * <p>
 * {
 * "state" : 200,
 * "data" : null
 * }
 */

public class ExitGroup {
    public interface IExitGroup {
        @Headers({"Content-Type: application/json", "Accept:  application/json"})
        @POST("app/contacts/groupFriends/exitGroupFriends")
        Call<BaseStateResponse> exitGroup(@Query("groupId") int groupId, @Query("acceptorId") String acceptorId);
    }
}
