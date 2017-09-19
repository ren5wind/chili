package com.topunion.chili.net.request_interface;

import com.topunion.chili.net.response_model.BaseStateResponse;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author      : renxiaoming
 * Date        : 2017/9/19
 * Description :
 */
public class SendInvite {
    public interface ISendInvite {
        @Headers({"Content-Type: application/json", "Accept:  application/json"})
        @POST("user/api/sendInvite.do")
        Call<BaseStateResponse> sendInvite(@Query("phone") String phone, @Query("inviter") String inviter);
    }

    public static class SendInviteResponse {
        public int state;
    }
}
