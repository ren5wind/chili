package com.topunion.chili.net.request_interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 * <p>
 * 9.13	获取易投成员详情
 * 9.13.1	请求格式
 * URL	/user/api/getUserInfo.do
 * 支持格式	JOSN
 * HTTP请求方式	GET
 * 是否登录验证
 * 请求数限制
 * <p>
 * 参数说明
 * 参数名	必选	类型及范围	说明
 * userId	true	String	详情成员编号
 * operatorUserId	false	String	操作成员编号，关联好友信息
 * <p>
 * <p>
 * state	int	结果代码信号	200结果成功,300 结果失败
 * data	Object	响应体
 * data明细
 * cellphone	String	用户电话号码
 * username	String	用户名
 * name	String	用户真实姓名
 * nickname	String	用户昵称
 * logicNickname	String	平台昵称	动态判断平台昵称，判断顺序：好友昵称→用户昵称→用户名
 * age	Integer	年龄
 * gender	String	性别	male:男 female:女
 * headImg	String	头像地址
 * idcard	String	身份证号
 * integral	Integer	积分
 * cPushStatus	byte	推送平台状态	0：未同步 1：已同步
 * hasIdentify	Boolean	是否认证	false：否 true：是
 * hasCorp	Boolean	是否企业用户	0：否 1：是
 * corpName	String	企业名称
 * corpTitleName	String	职称名称
 * isFriend	byte	是否好友	0：否 1：是
 * friendNickName	String	好友昵称
 * <p>
 * {
 * "state" : 200,
 * "data" : {
 * "cPushStatus" : 0,
 * "headImg" : "",
 * "gender" : null,
 * "hasCorp" : false,
 * "corpName" : " 湖南002",
 * "corpTitleName" : "",
 * "logicNickname" : "15019413263",
 * "integral" : 0,
 * "idcard" : null,
 * "friendNickName" : "",
 * "nickname" : "15019413263",
 * "name" : null,
 * "cellphone" : "15019876543",
 * "isFriend" : "0",
 * "hasIdentify" : false,
 * "age" : 0,
 * "username" : "U%15019413263"
 * }
 * }
 */

public class GetETMemberDetails {

    public interface IGetETMemberDetails {
        @GET("user/api/getUserInfo.do")
        Call<GetETMemberDetails.GetETMemberDetailsResponse> getETMemberDetails(@Query("userId") String userId, @Query("operatorUserId") String operatorUserId);
    }

    public static class GetETMemberDetailsResponse {
        public int state;
        public Member data;

        public static class Member {
            public int cPushStatus;
            public String headImg;
            public String gender;
            public boolean hasCorp;
            public String corpName;
            public String corpTitleName;
            public String logicNickname;
            public int integral;
            public String friendNickName;
            public String nickname;
            public String cellphone;
            public String isFriend;
            public boolean hasIdentify;
            public int age;
            public String username;
        }
    }
}
