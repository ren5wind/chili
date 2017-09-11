package com.topunion.chili.data;

import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.request_interface.GetCorps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */

public class Organization extends BaseData {
    private List<Company> companyList;

    public List<Company> analysisCompany(GetCorps.GetCorpsResponse corps) {
        if (corps == null) {
            return null;
        }
        companyList = new ArrayList<>();
        List<GetCorps.GetCorpsResponse.Data.Corp> corpList = corps.data.result;
        int size = corpList.size();
        for (int i = 0; i < size; i++) {
            GetCorps.GetCorpsResponse.Data.Corp corp = corpList.get(i);
            Company company = new Company();
            company.setId(corp.id);
            company.setName(corp.name);
            company.setAdministrators((AccountManager.getInstance().getUserId().equals(corp.userId)) ? true : false);
            companyList.add(company);
        }
        return companyList;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }
}
