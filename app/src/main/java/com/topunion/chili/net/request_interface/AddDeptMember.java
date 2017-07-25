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
 * 9.16	新增部门员工
 9.16.1	请求格式
 URL	/app/corp/corpUser/insertCorpUser
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 corpId	true	Integer	企业Id
 userIds	true	List	用户Id列表
 corpDeptId	true	Integer	部门Id
 role	true	String	角色


 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体

 {
 "state" : 200,
 "data" : null
 }

 */

public class AddDeptMember {

    public interface IAddDeptMember {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/corp/corpUser/insertCorpUser")
        Call<BaseStateResponse> addDeptMember(@Body AddDeptMember member);
    }

    public int corpId;
    public ArrayList<String> userIds;
    public int corpDeptId;
    public String role;

    public AddDeptMember() {}

    public AddDeptMember(int corpId, ArrayList<String> userIds, int corpDeptId, String role) {
        this.corpId = corpId;
        this.userIds = userIds;
        this.corpDeptId = corpDeptId;
        this.role = role;
    }

}
