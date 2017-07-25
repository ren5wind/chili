package com.topunion.chili.net.request_interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shawn on 7/18/17.
 *
 * 9.12	获取部门员工详情
 9.12.1	请求格式
 URL	/app/corp/corpUser/getCorpUser
 支持格式	JOSN
 HTTP请求方式	GET
 是否登录验证
 请求数限制

 参数说明
 参数名	必选	类型及范围	说明
 id	true	Integer	编号


 state	int	结果代码信号	200结果成功,300 结果失败
 data	Object	响应体
 data明细
 cellphone	String	用户电话号码
 username	String	用户名
 name	String	用户真实姓名
 nickname	String	用户昵称
 logicNickname	String	平台昵称	动态判断平台昵称，判断顺序：好友昵称→用户昵称→用户名
 age	Integer	年龄
 gender	String	性别	male:男 female:女
 headImg	String	头像地址
 idcard	String	身份证号
 hasIdentify	Boolean	是否认证	false：否 true：是
 hasCorp	Boolean	是否企业用户	false：否 true：是
 corpName	String	企业名称
 corpTitleName	String	职称名称
 corpDeptId	Integer	部门Id
 corpDeptName	String	部门名称
 isFriend	byte	是否好友	0：否 1：是
 friendNickName	String	好友昵称
 role	String	角色
 roleName	String	角色名称

 {
 "state" : 200,
 "data" : {
 "role" : "120",
 "headImg" : "/chili-2.0/images/headImg/8a8080875cc9709f015cc97237b90002.png",
 "gender" : "male",
 "hasCorp" : false,
 "corpName" : " 湖南002",
 "corpDeptId" : 4,
 "corpTitleName" : "",
 "logicNickname" : "18911288538",
 "idcard" : null,
 "friendNickName" : "18911288538",
 "corpDeptName" : "技术部\n",
 "nickname" : "18911288538",
 "name" : null,
 "roleName" : "普通员工",
 "cellphone" : "18911288538",
 "isFriend" : "1",
 "hasIdentify" : false,
 "age" : 9,
 "username" : "jianan002"
 }
 }



 */

public class GetCorpMemberDetails {

    public interface IGetCorpMemberDetails {
        @GET("app/corp/corpUser/getCorpUser")
        Call<GetCorpMemberDetails.GetCorpMemberDetailsResponse> getCorpMemberDetails(@Query("id") int id);
    }

    public static class GetCorpMemberDetailsResponse {
        public int state;
        public Member data;
        public static class Member {
            public String role;
            public String headImg;
            public String gender;
            public boolean hasCorp;
            public String corpName;
            public int corpDeptId;
            public String corpTitleName;
            public String logicNickname;
            public String friendNickName;
            public String corpDeptName;
            public String nickname;
            public String roleName;
            public String cellphone;
            public int isFriend;
            public boolean hasIdentify;
            public int age;
            public String username;
        }
    }
}
