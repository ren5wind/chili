package com.topunion.chili.net.request_interface;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Shawn on 7/18/17.

 9.15	新增公司部门


 9.15.1	请求格式
 URL	/app/corp/corpDept/insertCorpDept
 支持格式	JOSN
 HTTP请求方式	POST
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 corpId	true	Integer	企业Id
 name	true	String	部门名称
 description	true	String	部门描述


 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 id	Integer	部门Id

 {
 "state" : 200,
 "data" : {
 "id" : 3
 }
 }

 */

public class AddCorpDept {

    public interface IAddCorpDept {
        @Headers({"Content-Type: application/json","Accept:  application/json"})
        @POST("app/corp/corpDept/insertCorpDept")
        Call<AddCorpDeptResponse> addCorpDept(@Body AddCorpDept dept);
    }

    public int corpId;
    public String name;
    public String description;

    public AddCorpDept(int corpId, String name, String description) {
        this.corpId = corpId;
        this.name = name;
        this.description = description;
    }

    public static class AddCorpDeptResponse {
        public int state;
    }
}
