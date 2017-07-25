package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 *
 *
 * 9.1	获取企业列表
 9.1.1	请求格式
 URL	/app/corp/corpUser/getCorps
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 pageNo	false	int	当前页，不传递默认1
 pageSize	false	int	每页记录数，不传递默认10
 userId	true	String	用户Id
 corpId	false	Integer	企业Id，不传递默认所有
 corpName	false	String	企业名称，不传递默认所有




 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 totalCount	long	总条数
 totalPages	long	总页数
 name	String	企业名称
 id	Integer	企业Id
 userId	String	用户Id

 {
 "state" : 200,
 "data" : {
 "pageNo" : 1,
 "pageSize" : 10,
 "orderBy" : null,
 "order" : "",
 "autoCount" : true,
 "totalCount" : 5,
 "result" : [ {
 "name" : " 湖南002",
 "id" : "1",
 "userId" : "17060900000002"
 }, {
 "name" : "tiantian001",
 "id" : "3",
 "userId" : "17061400000003"
 }],
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

public class GetCorps {

    public interface IGetCorps {
        @GET("app/corp/corpUser/getCorps")
        Call<GetCorps.GetCorpsResponse> getCorps(@Query("userId") String userId);
    }

    public static class GetCorpsResponse {
        public Data data;
        public static class Data extends BaseListResponse{
            public List<Corp> result;
            public static class Corp {
                public String name;
                public String id;
                public String userId;
            }

        }
    }

}
