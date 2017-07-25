package com.topunion.chili.net.request_interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 * 9.11	获取部门详情
 9.11.1	请求格式
 URL	/app/corp/corpDept/getCorpDept
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 id	true	Integer	编号


 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 id	Integer	部门Id
 corpId	String	企业Id
 name	String	部门名称
 desc	String	部门描述

 {
 "state" : 200,
 "data" : {
 "id" : 3,
 "corpId" : 1,
 "name" : "Zu3",
 "status" : 0,
 "desc" : ""
 }
 }


 */

public class GetDeptDetails {

    public interface IGetDeptDetails {
        @GET("app/corp/corpDept/getCorpDept")
        Call<GetDeptDetails.GetDeptDetailsResponse> getDeptDetails(@Query("id") int id);
    }

    public static class GetDeptDetailsResponse {
        public int state;
        public DeptDetails data;
        public static class DeptDetails {
            public int id;
            public int corpId;
            public String name;
            public int status;
            public String desc;
        }
    }

}
