package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 *
 * 9.2	获取企业中部门列表
 9.2.1	请求格式
 URL	/app/corp/corpDept/getCorpDepts
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 pageNo	false	int	当前页，不传递默认1
 pageSize	false	int	每页记录数，不传递默认10
 corpId	false	Integer	企业Id，不传递默认所有
 name	false	String	部门名称，不传递默认所有
 orderBy	false	String	排序，默认部门名称正序排列



 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 totalCount	long	总条数
 totalPages	long	总页数
 name	String	部门名称
 id	Integer	部门Id

 {
 "state" : 200,
 "data" : {
 "pageNo" : 1,
 "pageSize" : 10,
 "orderBy" : null,
 "order" : "",
 "autoCount" : true,
 "totalCount" : 5,
 "result" : [  {
 "name" : "综合部",
 "id" : "10"
 }, {
 "name" : "财务部",
 "id" : "9"
 } ],
 "orderBySetted" : false,
 "hasNext" : false,
 "nextPage" : 1,
 "hasPrev" : false,
 "prevPage" : 1,
 "totalPages" : 1,
 "first" : 1
 }
 }



 */

public class GetCorpDepts {

    public interface IGetCorpDepts {
        @GET("app/corp/corpDept/getCorpDepts")
        Call<GetCorpDepts.GetCorpDeptsResponse> getCorpDepts(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize, @Query("corpId") int corpId);
    }

    public static class GetCorpDeptsResponse{
        public int state;
        public Data data;
        public static class Data extends BaseListResponse{
            public static class Dept {
                public String id;
                public String name;
            }
            public List<Dept> result;
        }
    }
}
