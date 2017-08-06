package com.topunion.chili.net;

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
import com.topunion.chili.net.request_interface.UpdateCorpDept;
import com.topunion.chili.net.request_interface.UpdateDeptMember;
import com.topunion.chili.net.request_interface.UpdateETFriendApplyAgree;
import com.topunion.chili.net.request_interface.UpdateETFriendNickname;
import com.topunion.chili.net.request_interface.UpdateGroupName;
import com.topunion.chili.net.response_model.BaseStateResponse;
import com.topunion.chili.net.response_model.ResponseData;

import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shawn on 7/17/17.
 */

@EBean(scope = EBean.Scope.Singleton)
public class HttpHelper {
    public static final String DEBUG_SERVER = "http://tmit.f3322.net:2051/chili-2.0/";
    public static final int PAGE_COUNT = 20;
    public Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(HttpHelper.DEBUG_SERVER)
            .build();

    public AddCorpDept.AddCorpDeptResponse addCorpDept(AddCorpDept data) {
        try {
            AddCorpDept.IAddCorpDept request = retrofit.create(AddCorpDept.IAddCorpDept.class);
            Call<AddCorpDept.AddCorpDeptResponse> call = request.addCorpDept(data);
            Response<AddCorpDept.AddCorpDeptResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addDeptMember(AddDeptMember data) {
        try {
            AddDeptMember.IAddDeptMember request = retrofit.create(AddDeptMember.IAddDeptMember.class);
            Call<BaseStateResponse> call = request.addDeptMember(data);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addETFriend(AddETFriend data) {
        try {
            AddETFriend.IAddETFriend request = retrofit.create(AddETFriend.IAddETFriend.class);
            Call<BaseStateResponse> call = request.addETFriend(data);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AddGroup.AddGroupResponse addGroup(AddGroup data) {
        try {
            AddGroup.IAddGroup request = retrofit.create(AddGroup.IAddGroup.class);
            Call<AddGroup.AddGroupResponse> call = request.addGroup(data);
            Response<AddGroup.AddGroupResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addGroupMember(AddGroupMember data) {
        try {
            AddGroupMember.IAddGroupMember request = retrofit.create(AddGroupMember.IAddGroupMember.class);
            Call<BaseStateResponse> call = request.addGroupMember(data);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AddJMessageUser.AddJMessageUserResponse addGroupMember(AddJMessageUser data) {
        try {
            AddJMessageUser.IAddJMessageUser request = retrofit.create(AddJMessageUser.IAddJMessageUser.class);
            Call<AddJMessageUser.AddJMessageUserResponse> call = request.addJMessageUser(data);
            Response<AddJMessageUser.AddJMessageUserResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean exitGroup(ExitGroup data) {
        try {
            ExitGroup.IExitGroup request = retrofit.create(ExitGroup.IExitGroup.class);
            Call<BaseStateResponse> call = request.exitGroup(data);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
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

    public GetCorpMemberDetails.GetCorpMemberDetailsResponse getCorpMemberDetails(int id) {
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
            return result.body().data;
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

    public GetETMemberDetails.GetETMemberDetailsResponse getETMemberDetails(int userId) {
        try {
            GetETMemberDetails.IGetETMemberDetails request = retrofit.create(GetETMemberDetails.IGetETMemberDetails.class);
            Call<GetETMemberDetails.GetETMemberDetailsResponse> call = request.getETMemberDetails(userId);
            Response<GetETMemberDetails.GetETMemberDetailsResponse> result = call.execute();
            return result.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetFriednApplies.GetFriednAppliesResponse getFriednApplies(int pageNo, int pageSize) {
        try {
            GetFriednApplies.IGetFriednApplies request = retrofit.create(GetFriednApplies.IGetFriednApplies.class);
            Call<GetFriednApplies.GetFriednAppliesResponse> call = request.getFriednApplies(pageNo, pageSize);
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
            return result.body().data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GetGroupDetails.GetGroupDetailsResponse getGroupDetails(int id) {
        try {
            GetGroupDetails.IGetGroupDetails request = retrofit.create(GetGroupDetails.IGetGroupDetails.class);
            Call<GetGroupDetails.GetGroupDetailsResponse> call = request.getGroupDetails(id);
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
            return result.body().data;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public GetGroups.GetGroupsResponse getGroups(int pageNo, int pageSize, String acceptorId) {
        try {
            GetGroups.IGetGroups request = retrofit.create(GetGroups.IGetGroups.class);
            Call<GetGroups.GetGroupsResponse> call = request.getGroups(pageNo, pageSize, acceptorId);
            Response<GetGroups.GetGroupsResponse> result = call.execute();
            return result.body();
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
            return result.body().state == 200;
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
            return result.body().state == 200;
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
            return result.body().state == 200;
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
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeGroupMember(int id, List<String> acceptorIds) {
        try {
            RemoveGroupMember.IRemoveGroupMember request = retrofit.create(RemoveGroupMember.IRemoveGroupMember.class);
            Call<BaseStateResponse> call = request.removeGroupMember(id, acceptorIds);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
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
            return result.body().state == 200;
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
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateETFriendApplyAgree(int id) {
        try {
            UpdateETFriendApplyAgree.IUpdateETFriendApplyAgree request = retrofit.create(
                    UpdateETFriendApplyAgree.IUpdateETFriendApplyAgree.class);
            Call<BaseStateResponse> call = request.updateETFriendApplyAgree(id);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateETFriendNickname(int id, String friendId, String nickname) {
        try {
            UpdateETFriendNickname.IUpdateETFriendNickname request = retrofit.create(UpdateETFriendNickname.IUpdateETFriendNickname.class);
            Call<BaseStateResponse> call = request.updateETFriendNickname(id, friendId, nickname);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGroupName(int id, String name) {
        try {
            UpdateGroupName.IUpdateGroupName request = retrofit.create(UpdateGroupName.IUpdateGroupName.class);
            Call<BaseStateResponse> call = request.updateGroupName(id, name);
            Response<BaseStateResponse> result = call.execute();
            return result.body().state == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
