package com.topunion.chili.net.request_interface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 * 9.3	获取角色列表
 9.3.1	请求格式
 URL	/app/corp/corpUser/getCorpUserRoleMap
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 无

 *
 * state	int	结果代码信号	200结果成功,300 结果失败
 data明细
 name	String	角色名称
 id	Integer	角色Id

 {
 "state" : 200,
 "data" : [ {
 "name" : "标的管理员",
 "id" : "110"
 }, {
 "name" : "外包人员",
 "id" : "130"
 }, {
 "name" : "普通员工",
 "id" : "120"
 } ]
 }


 */

public class GetCorpUserRole {

    public interface IGetCorpUserRole {
        @GET("app/corp/corpUser/getCorpUserRoleMap")
        Call<GetCorpUserRole.GetCorpUserRoleResponse> getRoles();
    }

    public static class GetCorpUserRoleResponse {
        public int state;
        public List<Role> data;
        public static class Role {
            public String name;
            public String id;
        }
    }
}
