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
 * 9.9	获取群组成员列表
 9.9.1	请求格式
 URL	/app/contacts/groupFriends/getUserAcceptorsByGroup
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 pageNo	false	int	当前页，不传递默认1
 pageSize	false	int	每页记录数，不传递默认10
 groupId	true	String	群组Id
 groupName	false	String	群组名称，不传递默认所有
 operatorUserId	false	String	操作成员编号，关联好友信息
 orderBy	false	String	排序，默认好友名称正序排列


 *
 *
 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 totalCount	long	总条数
 totalPages	long	总页数
 userId	String	用户Id
 username	String	用户名
 name	String	用户真实姓名
 nickname	String	用户昵称
 logicNickname	String	平台昵称	动态判断平台昵称，判断顺序：好友昵称→用户昵称→用户名
 headImg	String	头像地址
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
 "totalCount" : 3,
 "result" : [ {
 "headImg" : "/chili-2.0/images/headImg/8a80804d5d1bfc2a015d1c89a77a0008.jpg",
 "logicNickname" : "amy11",
 "friendNickName" : "",
 "nickname" : "amy11",
 "name" : null,
 "isFriend" : "0",
 "userId" : "17070700000003",
 "username" : "amy11"
 }, {
 "headImg" : "/chili-2.0/images/headImg/8a80804d5d35e428015d35e428040000.jpg",
 "logicNickname" : "huohuo",
 "friendNickName" : "",
 "nickname" : "huohuo",
 "name" : null,
 "isFriend" : "0",
 "userId" : "17070700000002",
 "username" : "huohuo"
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

public class GetGroupMembers {

    public interface IGetGroupMembers {
        @GET("app/contacts/groupFriends/getUserAcceptorsByGroup")
        Call<GetGroupMembers.GetGroupMembersResponse> getGroupMembers(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize,
                                                                      @Query("groupId") String groupId);
    }

    public static class GetGroupMembersResponse {
        public int state;
        public Data data;
        public static class Data extends BaseListResponse{
            public List<Member> result;
            public static class Member {
                public String headImg;
                public String logicNickname;
                public String friendNickName;
                public String nickname;
                public String isFriend;
                public String userId;
                public String username;
            }
        }
    }
}
