package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 * <p>
 * <p>
 * 9.5	获取易投好友申请列表
 * 9.5.1	请求格式
 * URL	/app/contacts/friendApply/getFriendApplies
 * 支持格式	JOSN
 * HTTP请求方式	GET
 * 是否登录验证
 * 请求数限制
 * <p>
 * 参数说明
 * 参数名	必选	类型及范围	说明
 * pageNo	false	int	当前页，不传递默认1
 * pageSize	false	int	每页记录数，不传递默认10
 * sender		false	String	发起人Id
 * acceptor	false	String	接受人Id
 * agree	false	Boolean	是否同意，不传递默认所有
 * orderBy	false	String	排序，默认时间倒序排列
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * state	int	结果代码信号	200结果成功,300 结果失败
 * data	Object	响应体
 * data明细
 * totalCount	long	总条数
 * totalPages	long	总页数
 * id	Integer	好友申请表Id
 * sender	String	发起人用户Id
 * senderUsername	String	发起人用户名
 * senderNickname	String	发起人用户昵称
 * headImgSender	String	发起人头像地址
 * senderFriendNickName	String	发起人好友昵称
 * acceptor	String	接受人用户Id
 * acceptorUsername	String	接受人用户名
 * acceptorNickname	String	接受人用户昵称
 * headImgAcceptor		String	接受人头像地址
 * acceptorFriendNickName	String	接受人好友昵称
 * created	Date	创建时间
 * agree	Boolean	是否已经同意	false：否 true：是
 * <p>
 * {
 * "state" : 200,
 * "data" : {
 * "pageNo" : 1,
 * "pageSize" : 10,
 * "orderBy" : null,
 * "order" : "",
 * "autoCount" : true,
 * "totalCount" : 21,
 * "result" : [ {
 * "senderNickname" : "honghong",
 * "acceptor" : "17070700000003",
 * "acceptorUsername" : "amy11",
 * "created" : 1499847291000,
 * "acceptorNickname" : "amy11",
 * "agree" : false,
 * "headImgSender" : "",
 * "acceptorFriendNickName" : "amy11",
 * "sender" : "17070700000001",
 * "headImgAcceptor" : "8a80804d5d1bfc2a015d1c89a77a0008.jpg",
 * "senderFriendNickName" : "",
 * "id" : "31",
 * "senderUsername" : "honghong"
 * }, {
 * "senderNickname" : "amy11",
 * "acceptor" : "17070700000002",
 * "acceptorUsername" : "huohuo",
 * "created" : 1499422475000,
 * "acceptorNickname" : "huohuo",
 * "agree" : false,
 * "headImgSender" : "8a80804d5d1bfc2a015d1c89a77a0008.jpg",
 * "acceptorFriendNickName" : "huohuo",
 * "sender" : "17070700000003",
 * "headImgAcceptor" : "8a80804d5d35e428015d35e428040000.jpg",
 * "senderFriendNickName" : "",
 * "id" : "30",
 * "senderUsername" : "amy11"
 * } ],
 * "orderBySetted" : false,
 * "hasNext" : true,
 * "nextPage" : 2,
 * "hasPrev" : false,
 * "prevPage" : 1,
 * "totalPages" : 3,
 * "first" : 1
 * }
 * }
 */

public class GetFriednApplies {

    public interface IGetFriednApplies {
        @GET("app/contacts/friendApply/getFriendApplies")
        Call<GetFriednApplies.GetFriednAppliesResponse> getFriednApplies(@Query("acceptor") String acceptor, @Query("pageNo") int pageNo, @Query("pageSize") int pageSize);
    }

    public static class GetFriednAppliesResponse extends BaseListResponse {
        public Data data;
        public int state;

        public static class Data extends BaseListResponse {
            public List<Apply> result;

            public static class Apply {
                public String senderNickname;
                public String acceptor;
                public String acceptorUsername;
                public long created;
                public String acceptorNickname;
                public boolean agree;
                public String headImgSender;
                public String acceptorFriendNickName;
                public String sender;
                public String headImgAcceptor;
                public String senderFriendNickName;
                public String id;
                public String senderUsername;
            }
        }
    }
}
