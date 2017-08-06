package com.topunion.chili.data;

import com.topunion.chili.net.request_interface.GetCorpOrDeptUsers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */

public class Department extends BaseData {
    private String name;
    private String id;
    private String headUrl;
    private String companyId;
    private List<Employee> employeeList;

    public void analysisEmployee(GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse employees) {
        employeeList = new ArrayList<>();
        List<GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse.User> corpList = employees.result;
        int size = corpList.size();
        for (int i = 0; i < size; i++) {
            GetCorpOrDeptUsers.GetCorpOrDeptUsersResponse.User user = corpList.get(i);
            Employee employee = new Employee();
            employee.setId(user.id);
            employee.setName(user.nickname);
            employee.setHeadUrl(user.headImg);
            employee.setDeptId(user.corpDeptId);
            employee.setDeptName(user.corpDeptName);
            employee.setCompanyId(companyId);
            employee.setDeptId(id);
            employee.setDeptName(name);
            employeeList.add(employee);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
