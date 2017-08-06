package com.topunion.chili.data;

import com.topunion.chili.net.request_interface.GetCorpDepts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */

public class Company extends BaseData{
    private String id;
    private String name;
    private String headUrl;
    private List<Department> departmentList;

    public List<Department> analysisDepartment(GetCorpDepts.GetCorpDeptsResponse depts){
        departmentList = new ArrayList<>();

        List<GetCorpDepts.GetCorpDeptsResponse.Data.Dept> corpList = depts.data.result;
        int size = corpList.size();
        for(int i =0;i< size;i++){
            GetCorpDepts.GetCorpDeptsResponse.Data.Dept dept = corpList.get(i);
            Department department = new Department();
            department.setId(dept.id);
            department.setName(dept.name);
            department.setCompanyId(id);
            departmentList.add(department);
        }
        return departmentList;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
}
