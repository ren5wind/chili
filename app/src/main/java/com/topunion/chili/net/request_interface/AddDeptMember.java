package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseStateResponse;


import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 * <p>
 * 9.16	新增部门员工
 * 9.16.1	请求格式
 * URL	/app/corp/corpUser/insertCorpUser
 * 支持格式	JOSN
 * HTTP请求方式	POST
 * 是否登录验证
 * 请求数限制
 * <p>
 * 参数说明
 * 参数名	必选	类型及范围	说明
 * corpId	true	Integer	企业Id
 * userIds	true	jsonArr	用户Id列表
 * corpDeptId	true	Integer	部门Id
 * role	true	String	角色
 * <p>
 * <p>
 * state	int	结果代码信号	200结果成功,300 结果失败
 * data	Object	响应体
 * <p>
 * {
 * "state" : 200,
 * "data" : null
 * }
 */

public class AddDeptMember {

    public interface IAddDeptMember {
        @Headers({"Content-Type: application/json", "Accept:  application/json"})
        @POST("app/corp/corpUser/insertCorpUser")
        Call<BaseStateResponse> addDeptMember(@Query("corpId") int corpId, @Query("userIds") JSONArray userIds,
                                              @Query("corpDeptId") int corpDeptId, @Query("role") String role);


    }
}
