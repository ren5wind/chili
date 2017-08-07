package com.topunion.chili.business;

import android.content.Context;

import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.AddCorpDept;
import com.topunion.chili.net.request_interface.AddGroupMember;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/7.
 */

public class CompanyManager {
    private Context mContext;

    public CompanyManager(Context context) {
        mContext = context;
    }

    public boolean addDepartMent(int companyId, String name) {
        AddCorpDept addCorpDept = new AddCorpDept(companyId, name, "");
        AddCorpDept.AddCorpDeptResponse response = HttpHelper_.getInstance_(mContext).addCorpDept(addCorpDept);
        if (response.state == 200) {
            return true;
        }
        return false;
    }

    public boolean updateDepartMent(int id, String name) {
        return HttpHelper_.getInstance_(mContext).updateCorpDept(id, name, "");
    }

    public boolean deleteDepartMent(int id) {
        return HttpHelper_.getInstance_(mContext).removeCorpDept(id);
    }

    public boolean addEmployee(int groupId, ArrayList<String> EmployeeIdList) {
        AddGroupMember addGroupMember = new AddGroupMember(groupId, EmployeeIdList);
        return HttpHelper_.getInstance_(mContext).addGroupMember(addGroupMember);
    }

    public boolean deleteEmployee(int id) {
        return HttpHelper_.getInstance_(mContext).removeDeptMember(id);
    }
}
