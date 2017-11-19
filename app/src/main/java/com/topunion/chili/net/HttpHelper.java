package com.topunion.chili.net;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.topunion.chili.net.request_interface.AddCorpDept;
import com.topunion.chili.net.request_interface.AddDeptMember;
import com.topunion.chili.net.request_interface.AddETFriend;
import com.topunion.chili.net.request_interface.AddGroup;
import com.topunion.chili.net.request_interface.AddGroupMember;
import com.topunion.chili.net.request_interface.AddJMessageUser;
import com.topunion.chili.net.request_interface.ExitGroup;
import com.topunion.chili.net.request_interface.GetCorpDepts;
import com.topunion.chili.net.request_interface.GetCorpMemberDetails;
import com.topunion.chili.net.request_interface.GetCorpOrDeptUsers;
import com.topunion.chili.net.request_interface.GetCorpUserRole;
import com.topunion.chili.net.request_interface.GetCorps;
import com.topunion.chili.net.request_interface.GetDeptDetails;
import com.topunion.chili.net.request_interface.GetETMemberDetails;
import com.topunion.chili.net.request_interface.GetFriednApplies;
import com.topunion.chili.net.request_interface.GetFriends;
import com.topunion.chili.net.request_interface.GetGroupDetails;
import com.topunion.chili.net.request_interface.GetGroupMembers;
import com.topunion.chili.net.request_interface.GetGroupMembersCount;
import com.topunion.chili.net.request_interface.GetGroups;
import com.topunion.chili.net.request_interface.GetUsers;
import com.topunion.chili.net.request_interface.RemoveCorpDept;
import com.topunion.chili.net.request_interface.RemoveDeptMember;
import com.topunion.chili.net.request_interface.RemoveETFriend;
import com.topunion.chili.net.request_interface.RemoveGroup;
import com.topunion.chili.net.request_interface.RemoveGroupMember;
import com.topunion.chili.net.request_interface.SendInvite;
import com.topunion.chili.net.request_interface.UpdateCorpDept;
import com.topunion.chili.net.request_interface.UpdateDeptMember;
import com.topunion.chili.net.request_interface.UpdateETFriendApplyAgree;
import com.topunion.chili.net.request_interface.UpdateETFriendNickname;
import com.topunion.chili.net.request_interface.UpdateGroupName;
import com.topunion.chili.net.response_model.BaseStateResponse;
import com.topunion.chili.net.response_model.ResponseData;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.String.valueOf;

/**
 * Created by Shawn on 7/17/17.
 */

@EBean(scope = EBean.Scope.Singleton)
public class HttpHelper {
    public static final String DEBUG_SERVER = "http://www.yibidding.com/yibidding-api/";
//    public static final String DEBUG_SERVER = "http://tmit.f3322.net:2051/chili-2.0/";


    public static final int PAGE_COUNT = 20;

    public OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor());

    public Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(HttpHelper.DEBUG_SERVER)
            .client(builder.build())
            .build();

    public AddCorpDept.AddCorpDeptResponse addCorpDept(int corpId, String name, String description) {
        try {
            AddCorpDept.IAddCorpDept request = retrofit.create(AddCorpDept.IAddCorpDept.class);
            Call<AddCorpDept.AddCorpDeptResponse> call = request.addCorpDept(corpId, name, description);
              Response<AddCorpDept.AddCorpDeptResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addDeptMember(int corpId, List<String> userIds,
                                 int corpDeptId, String role) {
        try {
            AddDeptMember.IAddDeptMember request = retrofit.create(AddDeptMember.IAddDeptMember.class);

            String[] userArr = new String[userIds.size()];
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < userIds.size(); i++) {
                jsonArray.put(userIds.get(i));
            }

            Call<BaseStateResponse> call = request.addDeptMember(corpId, jsonArray, corpDeptId, role);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addETFriend(String sender, String acceptor, String acceptorNickname) {
        try {
            AddETFriend.IAddETFriend request = retrofit.create(AddETFriend.IAddETFriend.class);
            Call<BaseStateResponse> call = request.addETFriend(sender, acceptor, acceptorNickname);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AddGroup.AddGroupResponse addGroup(String sender, String name, JSONArray acceptorIds) {
        try {
            AddGroup.IAddGroup request = retrofit.create(AddGroup.IAddGroup.class);
            Call<AddGroup.AddGroupResponse> call = request.addGroup(sender, name, acceptorIds);
            Response<AddGroup.AddGroupResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addGroupMember(String groupId, List<String> acceptorIds) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < acceptorIds.size(); i++) {
            jsonArray.put(acceptorIds.get(i));
        }
        try {
            AddGroupMember.IAddGroupMember request = retrofit.create(AddGroupMember.IAddGroupMember.class);
            Call<BaseStateResponse> call = request.addGroupMember(groupId, jsonArray);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AddJMessageUser.AddJMessageUserResponse addGroupMember(String userId) {
        try {
            AddJMessageUser.IAddJMessageUser request = retrofit.create(AddJMessageUser.IAddJMessageUser.class);
            Call<AddJMessageUser.AddJMessageUserResponse> call = request.addJMessageUser(userId);
            Response<AddJMessageUser.AddJMessageUserResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean exitGroup(String groupId, String acceptorId) {
        try {
            ExitGroup.IExitGroup request = retrofit.create(ExitGroup.IExitGroup.class);
            Call<BaseStateResponse> call = request.exitGroup(Integer.valueOf(groupId), acceptorId);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public GetCorpDepts.GetCorpDeptsResponse getCorpDepts(int pageNo, int pageSize, int corpId) {
        try {
            GetCorpDepts.IGetCorpDepts request = retrofit.create(GetCorpDepts.IGetCorpDepts.class);
            Call<GetCorpDepts.GetCorpDeptsResponse> call = request.getCorpDepts(pageNo, pageSize, corpId);
            Response<GetCorpDepts.GetCorpDeptsResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetCorpMemberDetails.GetCorpMemberDetailsResponse getCorpMemberDetails(String id) {
        try {
            GetCorpMemberDetails.IGetCorpMemberDetails request = retrofit.create(GetCorpMemberDetails.IGetCorpMemberDetails.class);
            Call<GetCorpMemberDetails.GetCorpMemberDetailsResponse> call = request.getCorpMemberDetails(id);
            Response<GetCorpMemberDetails.GetCorpMemberDetailsResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse getCorpUsers(int pageNo, int pageSize, int corpId, String corpName) {
        try {
            GetCorpOrDeptUsers.IGetCorpOrDeptUsers request = retrofit.create(GetCorpOrDeptUsers.IGetCorpOrDeptUsers.class);
            Call<GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse> call = request.getCorpUsers(pageNo, pageSize, corpId, corpName);
            Response<GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse getDeptUsers(int pageNo, int pageSize, int deptId, String corpName) {
        try {
            GetCorpOrDeptUsers.IGetCorpOrDeptUsers request = retrofit.create(GetCorpOrDeptUsers.IGetCorpOrDeptUsers.class);
            Call<ResponseData<GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse>> call = request.getDeptUsers(pageNo, pageSize, deptId, corpName);
            Response<ResponseData<GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse>> result = call.execute();
            return (result.body() == null) ? null : result.body().data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetCorps.GetCorpsResponse getCorps(String userId) {
        try {
            GetCorps.IGetCorps request = retrofit.create(GetCorps.IGetCorps.class);
            Call<GetCorps.GetCorpsResponse> call = request.getCorps(userId);
            Response<GetCorps.GetCorpsResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetCorpUserRole.GetCorpUserRoleResponse getRoles() {
        try {
            GetCorpUserRole.IGetCorpUserRole request = retrofit.create(GetCorpUserRole.IGetCorpUserRole.class);
            Call<GetCorpUserRole.GetCorpUserRoleResponse> call = request.getRoles();
            Response<GetCorpUserRole.GetCorpUserRoleResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetDeptDetails.GetDeptDetailsResponse getDeptDetails(int id) {
        try {
            GetDeptDetails.IGetDeptDetails request = retrofit.create(GetDeptDetails.IGetDeptDetails.class);
            Call<GetDeptDetails.GetDeptDetailsResponse> call = request.getDeptDetails(id);
            Response<GetDeptDetails.GetDeptDetailsResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetETMemberDetails.GetETMemberDetailsResponse getETMemberDetails(String userId, String operatorUserId) {
        try {
            GetETMemberDetails.IGetETMemberDetails request = retrofit.create(GetETMemberDetails.IGetETMemberDetails.class);
            Call<GetETMemberDetails.GetETMemberDetailsResponse> call = request.getETMemberDetails(userId, operatorUserId);
            Response<GetETMemberDetails.GetETMemberDetailsResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetFriednApplies.GetFriednAppliesResponse getFriednApplies(String acceptor, int pageNo, int pageSize) {
        try {
            GetFriednApplies.IGetFriednApplies request = retrofit.create(GetFriednApplies.IGetFriednApplies.class);
            Call<GetFriednApplies.GetFriednAppliesResponse> call = request.getFriednApplies(acceptor, pageNo, pageSize);
            Response<GetFriednApplies.GetFriednAppliesResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetFriends.GetFriendsResponse getFriends(String userId, int pageNo, int pageSize) {
        try {
            GetFriends.IGetFriends request = retrofit.create(GetFriends.IGetFriends.class);
            Call<ResponseData<GetFriends.GetFriendsResponse>> call = request.getFriends(userId, pageNo, pageSize);
            Response<ResponseData<GetFriends.GetFriendsResponse>> result = call.execute();
            return (result.body() == null) ? null : result.body().data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetGroupDetails.GetGroupDetailsResponse getGroupDetails(long groupImId) {
        try {
            GetGroupDetails.IGetGroupDetails request = retrofit.create(GetGroupDetails.IGetGroupDetails.class);
            Call<GetGroupDetails.GetGroupDetailsResponse> call = request.getGroupDetails(groupImId);
            Response<GetGroupDetails.GetGroupDetailsResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetGroupMembers.GetGroupMembersResponse getGroupMembers(int pageNo, int pageSize, String groupId) {
        try {
            GetGroupMembers.IGetGroupMembers request = retrofit.create(GetGroupMembers.IGetGroupMembers.class);
            Call<GetGroupMembers.GetGroupMembersResponse> call = request.getGroupMembers(pageNo, pageSize, groupId);
            Response<GetGroupMembers.GetGroupMembersResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getGroupMembersCount(int groupId) {
        try {
            GetGroupMembersCount.IGetGroupMembersCount request = retrofit.create(GetGroupMembersCount.IGetGroupMembersCount.class);
            Call<GetGroupMembersCount.GetGroupMembersCountResponse> call = request.getGroupMembersCount(groupId);
            Response<GetGroupMembersCount.GetGroupMembersCountResponse> result = call.execute();
            return (result.body() == null) ? null : result.body().data;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public GetGroups.GetGroupsResponse getGroups(int pageNo, int pageSize, String acceptorId) {
        try {
            GetGroups.IGetGroups request = retrofit.create(GetGroups.IGetGroups.class);
            Call<ResponseData<GetGroups.GetGroupsResponse>> call = request.getGroups(pageNo, pageSize, acceptorId);
            Response<ResponseData<GetGroups.GetGroupsResponse>> result = call.execute();
            return (result.body() == null) ? null : result.body().data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetUsers.GetUsersResponse getUsers(int pageNo, int pageSize, String name) {
        try {
            GetUsers.IGetUsers request = retrofit.create(GetUsers.IGetUsers.class);
            Call<GetUsers.GetUsersResponse> call = request.getUsers(pageNo, pageSize, name);
            Response<GetUsers.GetUsersResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeCorpDept(int id) {
        try {
            RemoveCorpDept.IRemoveCorpDept request = retrofit.create(RemoveCorpDept.IRemoveCorpDept.class);
            Call<BaseStateResponse> call = request.removeCorpDept(id);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeDeptMember(int id) {
        try {
            RemoveDeptMember.IRemoveDeptMember request = retrofit.create(RemoveDeptMember.IRemoveDeptMember.class);
            Call<BaseStateResponse> call = request.removeDeptMember(id);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeETFriend(String userId, String friendId) {
        try {
            RemoveETFriend.IRemoveETFriend request = retrofit.create(RemoveETFriend.IRemoveETFriend.class);
            Call<BaseStateResponse> call = request.removeETFriend(userId, friendId);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeGroup(int id) {
        try {
            RemoveGroup.IRemoveGroup request = retrofit.create(RemoveGroup.IRemoveGroup.class);
            Call<BaseStateResponse> call = request.removeGroup(id);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeGroupMember(String id, List<String> acceptorIds) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < acceptorIds.size(); i++) {
            jsonArray.put(acceptorIds.get(i));
        }
        try {
            RemoveGroupMember.IRemoveGroupMember request = retrofit.create(RemoveGroupMember.IRemoveGroupMember.class);
            Call<BaseStateResponse> call = request.removeGroupMember(id, jsonArray);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCorpDept(int id, String name, String description) {
        try {
            UpdateCorpDept.IUpdateCorpDept request = retrofit.create(UpdateCorpDept.IUpdateCorpDept.class);
            Call<BaseStateResponse> call = request.updateCorpDept(id, name, description);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDeptMember(int id, String corpDeptId, String role) {
        try {
            UpdateDeptMember.IUpdateDeptMember request = retrofit.create(UpdateDeptMember.IUpdateDeptMember.class);
            Call<BaseStateResponse> call = request.updateDeptMember(id, corpDeptId, role);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateETFriendApplyAgree(String id) {
        try {
            UpdateETFriendApplyAgree.IUpdateETFriendApplyAgree request = retrofit.create(
                    UpdateETFriendApplyAgree.IUpdateETFriendApplyAgree.class);
            Call<BaseStateResponse> call = request.updateETFriendApplyAgree(id);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateETFriendNickname(String id, String friendId, String nickname) {
        try {
            UpdateETFriendNickname.IUpdateETFriendNickname request = retrofit.create(UpdateETFriendNickname.IUpdateETFriendNickname.class);
            Call<BaseStateResponse> call = request.updateETFriendNickname(id, friendId, nickname);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGroupName(String id, String name) {
        try {
            UpdateGroupName.IUpdateGroupName request = retrofit.create(UpdateGroupName.IUpdateGroupName.class);
            Call<BaseStateResponse> call = request.updateGroupName(id, name);
            Response<BaseStateResponse> result = call.execute();
            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendInvite(String phone, String inviter) {
        try {
            SendInvite.ISendInvite request = retrofit.create(SendInvite.ISendInvite.class);
            Call<BaseStateResponse> call = request.sendInvite(phone, inviter);
            Response<BaseStateResponse> result = call.execute();

            return (result.body() == null) ? false : result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void uploadImage(Context context, String userId, File file, uploadListener listener) {
        mListener = listener;
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        postFile(context, DEBUG_SERVER + "user/api/updateImgUrl.do", map, file);
    }

    uploadListener mListener;

    public interface uploadListener {
        void onSuccess();

        void onFail();
    }

    private void postFile(Context context, final String url, final Map<String, Object> map, File file) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag(context).build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.i("lfq", "onFailure");
                mListener.onFail();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().toString();
                    Log.i("lfq", response.message() + " , body " + str);
                    mListener.onSuccess();
                } else {
                    Log.i("lfq", response.message() + " error : body " + response.body().toString());
                    mListener.onFail();
                }
            }
        });
    }

    /**
     * @param url     下载连接
     * @param saveDir 储存下载文件的SDCard目录
     */
    public void download(final String url, final String saveDir, final OnDownloadListener mDownloadListener) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                mDownloadListener.onDownloadFailed();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        mDownloadListener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    mDownloadListener.onDownloadSuccess(file.getAbsolutePath());
                } catch (Exception e) {
                    mDownloadListener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

}
