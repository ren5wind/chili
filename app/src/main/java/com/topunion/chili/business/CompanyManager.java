package com.topunion.chili.business;

import android.content.Context;

import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.AddCorpDept;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/7.
 */

public class CompanyManager {
    private Context mContext;

    public CompanyManager(Context context) {
        mContext = context;
    }


    public int addDepartMent(int companyId, String name) {
        AddCorpDept.AddCorpDeptResponse response = HttpHelper_.getInstance_(mContext).addCorpDept(companyId, name, "");
        if (response.state == 200) {
            return response.data.id;
        } else {
            return -1;
        }
    }

    public boolean updateDepartment(int id, String name) {
        return HttpHelper_.getInstance_(mContext).updateCorpDept(id, name, "");
    }

    public boolean deleteDepartment(int id) {
        return HttpHelper_.getInstance_(mContext).removeCorpDept(id);
    }

    public boolean addEmployee(int groupId, ArrayList<String> EmployeeIdList) {
        return HttpHelper_.getInstance_(mContext).addGroupMember(groupId, EmployeeIdList);
    }

    public boolean deleteEmployee(int id) {
        return HttpHelper_.getInstance_(mContext).removeDeptMember(id);
    }
}
