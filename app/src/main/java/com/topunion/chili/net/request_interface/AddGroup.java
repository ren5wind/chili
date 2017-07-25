package com.topunion.chili.net.request_interface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Shawn on 7/18/17.
 *
 9.18	新增群组


 9.18.1	请求格式
 URL	/app/contacts/group/insertGroup
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 9.18.2	参数说明
 参数名	必选	类型及范围	说明
 sender	true	String	发起人
 name	true	String	名称，默认群组成员名称，以"、"连接
 acceptorIds	true	List	群组成员Id列表

 9.18.3	正常返回结果
 返回键	类型	返回值	说明
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 id	Integer	群组Id
 pushGroupId	String	推送群组Id

 {
 "state" : 200,
 "data" : {
 "id" : "3",
 " pushGroupId " : "0"
 }
 }

 */

public class AddGroup {

    public interface IAddGroup {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/contacts/group/insertGroup")
        Call<AddGroup.AddGroupResponse> addGroup(@Body AddGroup group);
    }

    public String sender;
    public String name;
    public ArrayList<String> acceptorIds;

    public AddGroup() {}

    public AddGroup(String sender, String name, ArrayList<String> acceptorIds) {
        this.sender = sender;
        this.name = name;
        this.acceptorIds = acceptorIds;
    }

    public static class AddGroupResponse {
        public int state;
        public static class Data {
            public String id;
            public String pushGroupId;
        }
    }
}
