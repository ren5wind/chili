package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseStateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 * <p>
 * <p>
 * 9.17	新增易投好友
 * 9.17.1	请求格式
 * URL	/app/contacts/friendApply/insertFriendApply
 * 支持格式	JOSN
 * HTTP请求方式	POST
 * 是否登录验证
 * 请求数限制
 * <p>
 * 参数说明
 * 参数名	必选	类型及范围	说明
 * sender	true	String	发起人
 * acceptor	true	String	接受人
 * acceptorNickname	true	String	接受人昵称
 * <p>
 * 9.17.2	正常返回结果
 * 返回键	类型	返回值	说明
 * state	int	结果代码信号	200结果成功,300 结果失败
 * data	Object	响应体
 * <p>
 * {
 * "state" : 200,
 * "data" : null
 * }
 */

public class AddETFriend {

    public interface IAddETFriend {
        @Headers({"Content-Type: application/json", "Accept:  application/json"})
        @POST("app/contacts/friendApply/insertFriendApply")
        Call<BaseStateResponse> addETFriend(@Query("sender") String sender, @Query("acceptor") String acceptor,
                                            @Query("acceptorNickname") String acceptorNickname);
    }

}
