package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 * 9.8	获取群组列表
 9.8.1	请求格式
 URL	/app/contacts/groupFriends/getGroupsByAcceptor
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 pageNo	false	int	当前页，不传递默认1
 pageSize	false	int	每页记录数，不传递默认10
 acceptor	true	String	用户Id
 acceptorName	false	String	用户名称，不传递默认所有
 orderBy	false	String	排序，默认好友名称正序排列

 *
 *
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 totalCount	long	总条数
 totalPages	long	总页数
 id	Integer	群组Id
 pushGroupId	Long	推送平台群组Id
 name	String	群组名称
 senderId	String	创建用户Id
 count	String	成员总数
 imgUrls	String	成员头像，前4名。用“,”分隔

 {
 "state" : 200,
 "data" : {
 "pageNo" : 1,
 "pageSize" : 10,
 "orderBy" : null,
 "order" : "",
 "autoCount" : true,
 "totalCount" : 1,
 "result" : [ {
 "senderId" : "17070700000002",
 "name" : "amy11、honghong",
 "count" : "3",
 "headImgs" : [ "8a80804d5d1bfc2a015d1c89a77a0008.jpg", "8a80804d5d35e428015d35e428040000.jpg" ],
 "id" : "4",
 "pushGroupId" : "1"
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

 *
 */

public class GetGroups {

    public interface IGetGroups {
        @GET("app/contacts/groupFriends/getGroupsByAcceptor")
        Call<GetGroups.GetGroupsResponse> getGroups(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                               @Query("acceptor") String acceptor);
    }

    public static class GetGroupsResponse extends BaseListResponse{
        public List<Group> result;
        public static class Group {
            public String senderId;
            public String name;
            public String count;
            public List<String> haedImgs;
            public String id;
            public String pushGroupId;
        }
    }

}
