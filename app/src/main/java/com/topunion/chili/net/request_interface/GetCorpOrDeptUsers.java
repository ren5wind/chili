package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseListResponse;
import com.topunion.chili.net.response_model.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 *
 * 9.4	获取企业或部门中员工列表
 9.4.1	请求格式
 URL	/app/corp/corpUser/getCorpUsers
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 pageNo	false	int	当前页，不传递默认1
 pageSize	false	int	每页记录数，不传递默认10
 corpId	false	Integer	企业Id，不传递默认所有
 corpName	false	String	企业名称，不传递默认所有
 corpDeptId	false	Integer	部门Id，不传递默认所有
 corpDeptName	false	String	部门名称，不传递默认所有
 userName	false	String	用户名称，不传递默认所有
 orderBy	false	String	排序，默认员工名称正序排列




 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 totalCount	long	总条数
 totalPages	long	总页数
 id	Integer	企业人员表Id
 userId	String	用户Id
 username	String	用户名
 name	String	用户真实姓名
 nickname	String	用户昵称
 logicNickname	String	平台昵称	动态判断平台昵称，判断顺序：好友昵称→用户昵称→用户名
 headImg	String	头像地址
 corpDeptId	Integer	部门Id
 corpDeptName	String	部门名称
 isFriend	byte	是否好友	0：否 1：是
 friendNickName	String	好友昵称

 {
 "state" : 200,
 "data" : {
 "pageNo" : 1,
 "pageSize" : 10,
 "orderBy" : null,
 "order" : "",
 "autoCount" : true,
 "totalCount" : 6,
 "result" : [ {
 "headImg" : "",
 "logicNickname" : "testCorp",
 "friendNickName" : "",
 "corpDeptName" : "",
 "nickname" : "testCorp",
 "name" : null,
 "isFriend" : "0",
 "corpDeptId" : "",
 "id" : "12",
 "userId" : "17061300000001",
 "username" : "testCorp"
 }, {
 "headImg" : "/chili-2.0/images/headImg/8a8080865cc8bbf6015cc8bbf7060000.jpeg",
 "logicNickname" : "tm",
 "friendNickName" : "",
 "corpDeptName" : "",
 "nickname" : "tm",
 "name" : null,
 "isFriend" : "0",
 "corpDeptId" : "",
 "id" : "13",
 "userId" : "17061400000003",
 "username" : "tm"
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

public class GetCorpOrDeptUsers {

    public interface IGetCorpOrDeptUsers {
        @GET("app/corp/corpUser/getCorpUsers")
        Call<GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse> getCorpUsers(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize ,
                                                                             @Query("corpId") int corpId, @Query("corpName") String corpName);
        @GET("app/corp/corpUser/getCorpUsers")
        Call<ResponseData<GetCorpOrDeptUsersResponse>> getDeptUsers(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize ,
                                                                    @Query("corpDeptId") int corpDeptId, @Query("corpDeptName") String corpDeptName);
    }

    public static class GetCorpOrDeptUsersResponse extends BaseListResponse {
        public List<User> result;
        public static class User {
            public String headImg;
            public String logicNickname;
            public String friendNickName;
            public String corpDeptName;
            public String nickname;
            public int isFriend;
            public String corpDeptId;
            public String id;
            public String userId;
            public String username;
        }
    }

}
