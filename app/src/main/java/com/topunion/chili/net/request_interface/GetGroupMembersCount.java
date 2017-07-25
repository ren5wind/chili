package com.topunion.chili.net.request_interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 * 9.10	获取群组成员总数
 9.10.1	请求格式
 URL	/app/contacts/groupFriends/getGroupFriendsCount
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 groupId	false	Integer	群组Id，不传递默认所有



 state	int	结果代码信号	200结果成功,300 结果失败
 data	long	群组成员总数

 {
 "state" : 200,
 "data" : 3
 }

 */

public class GetGroupMembersCount {

    public interface IGetGroupMembersCount {
        @GET("app/contacts/groupFriends/getGroupFriendsCount")
        Call<GetGroupMembersCount.GetGroupMembersCountResponse> getGroupMembersCount(@Query("groupId") int groupId);
    }

    public static class GetGroupMembersCountResponse {
        public int state;
        public int data;
    }

}
