package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseStateResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Shawn on 7/18/17.
 *
 9.19	新增群组人员
 9.19.1	请求格式
 URL	/app/contacts/groupFriends/insertGroupFriends
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 9.19.2	参数说明
 参数名	必选	类型及范围	说明
 groupId	true	Integer	群组Id
 acceptorIds	true	List	群组成员Id列表

 9.19.3	正常返回结果
 返回键	类型	返回值	说明
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体

 {
 "state" : 200,
 "data" : null
 }

 */

public class AddGroupMember {

    public interface IAddGroupMember {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/contacts/groupFriends/insertGroupFriends")
        Call<BaseStateResponse> addGroupMember(@Body AddGroupMember member);
    }

    public int groupId;
    public ArrayList<String> acceptorIds;

    public AddGroupMember() {
    }

    public AddGroupMember(int groupId, ArrayList<String> acceptorIds) {
        this.groupId = groupId;
        this.acceptorIds = acceptorIds;
    }

}
