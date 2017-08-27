package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseListResponse;
import com.topunion.chili.net.response_model.ResponseData;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 *  9.6	获取易投好友列表
 9.6.1	请求格式
 URL	/app/contacts/friend/getFriends
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 pageNo	false	int	当前页，不传递默认1
 pageSize	false	int	每页记录数，不传递默认10
 userId	true	String	用户Id
 friendId	false	String	好友Id，不传递默认所有
 friendName	false	String	好友名称，不传递默认所有
 groupId	false	Integer	群组Id，判断群组中是否存在
 corpId	false	Integer	企业Id，判断企业中是否存在
 orderBy	false	String	排序，默认好友名称正序排列





 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 totalCount	long	总条数
 totalPages	long	总页数
 id	Integer	好有表Id
 userId	String	用户Id
 nickname	String	好友昵称
 headImg	String	好友头像地址
 isInCorp	byte	是否属于企业	0：否 1：是
 isInGroup	byte	是否在群组中	0：否 1：是
 friendId	String	好友Id

 {
 "state" : 200,
 "data" : {
 "pageNo" : 1,
 "pageSize" : 10,
 "orderBy" : null,
 "order" : "",
 "autoCount" : true,
 "totalCount" : 2,
 "result" : [ {
 "friendId" : "17070700000003",
 "headImg" : "/chili-2.0/images/headImg/8a80804d5d1bfc2a015d1c89a77a0008.jpg",
 "inCorp" : "0",
 "nickname" : "amy11",
 "id" : "51",
 "inGroup" : "0",
 "userId" : "17070700000002"
 }, {
 "friendId" : "17070700000001",
 "headImg" : "",
 "inCorp" : "0",
 "nickname" : "honghong",
 "id" : "46",
 "inGroup" : "0",
 "userId" : "17070700000002"
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

public class GetFriends {
    /**
    pageNo	false	int	当前页，不传递默认1
    pageSize	false	int	每页记录数，不传递默认10
    userId	true	String	用户Id
    friendId	false	String	好友Id，不传递默认所有
    friendName	false	String	好友名称，不传递默认所有
    groupId	false	Integer	群组Id，判断群组中是否存在
    corpId	false	Integer	企业Id，判断企业中是否存在
    orderBy	false	String	排序，默认好友名称正序排列
     */
    public interface IGetFriends {
        @GET("app/contacts/friend/getFriends")
        Call<ResponseData<GetFriendsResponse>> getFriends(@Query("userId") String userId, @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);
    }

    public static class GetFriendsResponse extends BaseListResponse{
        public List<Friend> result;
        public static class Friend implements Serializable {
            public String friendId;
            public String headImg;
            public String inCorp;
            public String nickname;
//            public String id;
            public String inGroup;
//            public String userId;
        }
    }

}
