package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 * 9.7	获取易投用户列表
 *9.7.1	请求格式
 URL	/user/api/getUsers.do
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 * 参数名	必选	类型及范围	说明
 pageNo	false	int	当前页，不传递默认1
 pageSize	false	int	每页记录数，不传递默认10
 name	true	String	用户名称，完全匹配用户名或手机号码
 operatorUserId	false	String	操作成员编号，关联好友信息
 corpId	false	Integer	企业Id，判断企业中是否存在
 orderBy	false	String	排序，默认好友名称正序排列



 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 totalCount	long	总条数
 totalPages	long	总页数
 cId	String	用户Id
 cUserName	String	用户名
 logicNickname	String	平台昵称	动态判断平台昵称，判断顺序：好友昵称→用户昵称→用户名
 imgUrl	String	头像地址
 pushStatus	byte	推送平台状态	0：未同步 1：已同步
 inCorp	byte	是否属于企业	0：否 1：是
 isFriend	byte	是否好友	0：否 1：是
 friendNickName	String	好友昵称	好友昵称

 {
 "state" : 200,
 "data" : {
 "pageNo" : 1,
 "pageSize" : 10,
 "orderBy" : null,
 "order" : "",
 "autoCount" : true,
 "totalCount" : 64,
 "result" : [ {
 "imgUrl" : "",
 "cPushStatus" : "0",
 "cUsername" : "aaaaaa",
 "inCorp" : "0",
 "logicNickname" : "aaaaaa",
 "friendNickName" : "",
 "isFriend" : "0",
 "cId" : "16122700000002"
 }, {
 "imgUrl" : "/chili-2.0/images/headImg/8a80804d5d1bfc2a015d1c89a77a0008.jpg",
 "cPushStatus" : "1",
 "cUsername" : "amy11",
 "inCorp" : "0",
 "logicNickname" : "amy11",
 "friendNickName" : "",
 "isFriend" : "0",
 "cId" : "17070700000003"
 } ],
 "orderBySetted" : false,
 "hasNext" : true,
 "nextPage" : 2,
 "hasPrev" : false,
 "prevPage" : 1,
 "totalPages" : 7,
 "first" : 1
 }
 }


 */

public class GetUsers {

    public interface IGetUsers {
        @GET("user/api/getUsers.do")
        Call<GetUsers.GetUsersResponse> getUsers(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                    @Query("name") String name);
    }

    public static class GetUsersResponse {
        public int state;
        public Data data;
        public static class Data extends BaseListResponse{
            public List<User> result;
            public static class User {
                public String imgUrl;
                public String cPushStatus;
                public String cUsername;
                public String inCorp;
                public String logicNickname;
                public String friendNickName;
                public String isFriend;
                public String cId;
            }
        }
    }

}
