package com.topunion.chili.business;

import android.util.SparseArray;

import com.topunion.chili.net.request_interface.GetFriends;
import com.topunion.chili.net.request_interface.GetGroups;
import com.topunion.chili.util.CloneUtil;

import java.util.List;


/**
 * Author      : renxiaoming
 * Date        : 2017/7/17
 * Description :
 */
public class ImInfoManager {
    private static ImInfoManager instance;
    private final static Object syncLock = new Object();

    private SparseArray<GetGroups.GetGroupsResponse.Group> groupList;
    private SparseArray<GetFriends.GetFriendsResponse.Friend> friendList;

    public static ImInfoManager getInstance() {
        if (instance == null) {
            synchronized (syncLock) {
                if (instance == null) {
                    instance = new ImInfoManager();
                }
            }
        }
        return instance;
    }

    private ImInfoManager() {
        groupList = new SparseArray<>();
        friendList = new SparseArray<>();
    }

    public void addFriendList(final List<GetFriends.GetFriendsResponse.Friend> list) {
        if(list == null){
            return;
        }
        List<GetFriends.GetFriendsResponse.Friend> newList = CloneUtil.deepCopyList(list);
        for (int i = 0; i < newList.size(); i++) {
            GetFriends.GetFriendsResponse.Friend friend = newList.get(i);
            int id = Integer.valueOf(friend.id);
            synchronized (syncLock) {
                friendList.put(id, friend);
            }
        }
    }

    public GetFriends.GetFriendsResponse.Friend getFriendById(int id) {
        GetFriends.GetFriendsResponse.Friend group = null;
        synchronized (syncLock) {
            group = (friendList == null || friendList.size() == 0) ? null : friendList.get(id);
        }
        return group;
    }

    public void clearFriendCache() {
        synchronized (syncLock) {
            friendList.clear();
        }
    }

    public void addGroupList(final List<GetGroups.GetGroupsResponse.Group> list) {
        if(list == null){
            return;
        }
        List<GetGroups.GetGroupsResponse.Group> newList = CloneUtil.deepCopyList(list);
        for (int i = 0; i < newList.size(); i++) {
            GetGroups.GetGroupsResponse.Group group = newList.get(i);
            int id = Integer.valueOf(group.id);
            synchronized (syncLock) {
                groupList.put(id, group);
            }
        }
    }

    public GetGroups.GetGroupsResponse.Group getGroupById(int id) {
        GetGroups.GetGroupsResponse.Group group = null;
        synchronized (syncLock) {
            group = (groupList == null || groupList.size() == 0) ? null : groupList.get(id);
        }
        return group;
    }

    public void clearGroupCache() {
        synchronized (syncLock) {
            groupList.clear();
        }
    }

}
