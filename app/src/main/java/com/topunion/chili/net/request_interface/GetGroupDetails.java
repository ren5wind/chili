package com.topunion.chili.net.request_interface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 * 9.14	获取群组详情
 9.14.1	请求格式
 URL	/app/contacts/group/getGroup
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 id	true	Integer	编号
 operatorUserId	false	String	操作成员编号，关联好友信息


 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 id	Integer	群组Id
 pushGroupId	Long	推送平台群组Id
 name	String	群组名称
 senderId	String	群组创建用户Id
 count	String	群组成员数
 members	List	用户成员数组
 members->id	String	用户Id
 members->username	String	用户名
 members->name	String	用户真实姓名
 members->nickname	String	用户昵称
 members->logicNickname	String	平台昵称	动态判断平台昵称，判断顺序：好友昵称→用户昵称→用户名
 members->headImg	String	头像地址
 members->isFriend	String	是否好友	0：否 1：是（与operatorUserId匹配）
 members->friendNickName	String	好友昵称	（与operatorUserId匹配）

 {
 "state" : 200,
 "data" : {
 "senderId" : "17070700000002",
 "members" : [ {
 "headImg" : "/chili-2.0/images/headImg/8a80804d5d1bfc2a015d1c89a77a0008.jpg",
 "logicNickname" : "amy11",
 "friendNickName" : "",
 "nickname" : "amy11",
 "name" : null,
 "isFriend" : "0",
 "id" : "17070700000003",
 "username" : "amy11"
 }, {
 "headImg" : "",
 "logicNickname" : "honghong",
 "friendNickName" : "",
 "nickname" : "honghong",
 "name" : null,
 "isFriend" : "0",
 "id" : "17070700000001",
 "username" : "honghong"
 }, {
 "headImg" : "/chili-2.0/images/headImg/8a80804d5d35e428015d35e428040000.jpg",
 "logicNickname" : "huohuo",
 "friendNickName" : "",
 "nickname" : "huohuo",
 "name" : null,
 "isFriend" : "0",
 "id" : "17070700000002",
 "username" : "huohuo"
 } ],
 "name" : "amy11、honghong",
 "count" : "3",
 "id" : "4",
 "pushGropuId" : "1"
 }
 }

 *
 */

public class GetGroupDetails {

    public interface IGetGroupDetails {
        @GET("app/contacts/group/getGroup")
        Call<GetGroupDetails.GetGroupDetailsResponse> getGroupDetails(@Query("id") int id);
    }

    public static class GetGroupDetailsResponse {
        public int state;
        public Group data;
        public static class Group {
            public String name;
            public String count;
            public String id;
            public String pushGroupId;
            public List<Member> members;
            public static class Member {
                public String headImg;
                public String logicNickname;
                public String friendNickName;
                public String nickname;
                public String isFriend;
                public String id;
                public String username;
            }
        }
    }
}
