package com.topunion.chili.net.request_interface;


import com.topunion.chili.net.response_model.BaseStateResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 9.21	修改部门员工
 9.21.1	请求格式
 URL	/app/corp/corpUser/updateCorpUser
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 9.21.2	参数说明
 参数名	必选	类型及范围	说明
 id	true	Integer	编号
 corpDeptId	true	String	部门Id
 role	true	String	角色

 9.21.3	正常返回结果
 返回键	类型	返回值	说明
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体

 {
 "state" : 200,
 "data" : null
 }

 */

public class UpdateDeptMember {

    public interface IUpdateDeptMember {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/corp/corpUser/updateCorpUser")
        Call<BaseStateResponse> updateDeptMember(@Query("id") int id, @Query("corpDeptId") String corpDeptId,
                                                 @Query("role") String role);
    }

}
