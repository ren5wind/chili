package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.R;
import com.topunion.chili.data.Company;
import com.topunion.chili.data.Department;
import com.topunion.chili.data.Employee;
import com.topunion.chili.data.Organization;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_manage_company)
public class CompanyManageActivity extends AppCompatActivity {

    @Extra
    String companyId;

    @ViewById
    TextView txt_title;

    @ViewById
    RelativeLayout search_layout;

    @ViewById
    ListView mListView;

    @Extra
    Organization organization;
    private List<ItemData> itemDataList;

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void mSearchInput() {
        SearchFromManualActivity_.intent(this).startForResult(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index = resultCode;
        //TODO
    }

    @AfterViews
    void init() {
        //数据转换
        itemDataList = analysisOrganization(organization);

        txt_title.setVisibility(View.GONE);
        search_layout.setVisibility(View.VISIBLE);
        mListView.setAdapter(new MyAdapter(itemDataList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 3) {
                    //TODO
                    startActivity(new Intent(CompanyManageActivity.this, MemberManageActivity_.class));
                }
            }
        });
    }

    private void showMemberAddAlert() {
        final AlertDialog alertDialog = new AlertDialog.Builder(CompanyManageActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_transparent_bg);
        window.setGravity(Gravity.BOTTOM);
        window.setContentView(R.layout.dialog_member_add);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_from_manual:
                        startActivity(new Intent(CompanyManageActivity.this, SearchFromManualActivity_.class));
                        break;
                    case R.id.btn_from_contact:
                        startActivity(new Intent(CompanyManageActivity.this, SearchFromContactActivity_.class));
                        break;
                    case R.id.btn_from_friedns:
                        startActivity(new Intent(CompanyManageActivity.this, SearchFromFriendsActivity_.class));
                        break;
                }
                alertDialog.dismiss();
            }
        };
        window.findViewById(R.id.btn_from_manual).setOnClickListener(listener);
        window.findViewById(R.id.btn_from_contact).setOnClickListener(listener);
        window.findViewById(R.id.btn_from_friedns).setOnClickListener(listener);
        window.findViewById(R.id.btn_cancel).setOnClickListener(listener);
    }

    private List<ItemData> analysisOrganization(Organization organization) {
        List<ItemData> itemDatas = new ArrayList<>();
        List<Company> companies = organization.getCompanyList();

        if (companies == null || companies.size() == 0) {
            return null;
        }
        for (int i = 0; i < companies.size(); i++) {
            List<Employee> employees = new ArrayList<>();
            //添加企业
            Company company = companies.get(i);
            ItemData itemDataCompany = new ItemData();
            itemDataCompany.type = ItemData.TYPE_COMPANY;
            itemDataCompany.name = company.getName();
            itemDataCompany.id = company.getId();
            itemDatas.add(itemDataCompany);
            //添加部门
            List<Department> departments = company.getDepartmentList();
            for (int j = 0; departments != null && j < departments.size(); j++) {
                Department department = departments.get(j);
                ItemData itemDataDepartment = new ItemData();
                itemDataDepartment.type = ItemData.TYPE_DEPARTMENT;
                itemDataDepartment.name = department.getName();
                itemDataDepartment.id = department.getId();
                itemDataDepartment.companyId = department.getCompanyId();
                itemDatas.add(itemDataDepartment);
                //读取员工
                if (department.getEmployeeList() != null) {
                    employees.addAll(department.getEmployeeList());
                }

            }
            //添加员工管理
            ItemData itemEmployeeManagement = new ItemData();
            itemEmployeeManagement.type = ItemData.TYPE_EMPLOYEE_MANAGEMENT;
            itemEmployeeManagement.name = "员工管理";
            //添加员工
            for (int k = 0; employees != null && k < employees.size(); k++) {
                Employee employee = employees.get(k);
                ItemData itemDataEmployee = new ItemData();
                itemDataEmployee.type = ItemData.TYPE_EMPLOYEE;
                itemDataEmployee.name = employee.getName();
                itemDataEmployee.id = employee.getId();
                itemDataEmployee.parentName = employee.getDeptName();
                itemDataEmployee.iconUrl = employee.getHeadUrl();
                itemDataEmployee.companyId = employee.getCompanyId();
                itemDataEmployee.departmentId = employee.getDeptId();
                itemDatas.add(itemDataEmployee);
            }
        }
        return itemDatas;
    }


    private void showGroupAddAlert(String title) {
        final AlertDialog alertDialog = new AlertDialog.Builder(CompanyManageActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.dialog_group_add);
        final EditText edit_alert = (EditText) window.findViewById(R.id.edit_alert);
        TextView txt_alert_title = (TextView) window.findViewById(R.id.txt_alert_title);
        if (title != null) {
            txt_alert_title.setText(title);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        };
        window.findViewById(R.id.btn_close).setOnClickListener(listener);
        window.findViewById(R.id.btn_cancel).setOnClickListener(listener);
        window.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edit_alert.getText().toString();

                if (name != null && name.length() > 0) {
                    //TODO
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(CompanyManageActivity.this, "组织名称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private List<ItemData> dataList;

        MyAdapter(List<ItemData> dataList) {
            this.dataList = dataList;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView txt_name, txt_department;
            SimpleDraweeView img_header;
            LinearLayout layout_add, layout_manage;
            Button btn_add;
            ImageButton btn_dustbin, btn_edit;
            ItemData data = dataList.get(i);
            switch (data.type) {
                case ItemData.TYPE_COMPANY: //公司管理
                    view = LayoutInflater.from(CompanyManageActivity.this).inflate(R.layout.company_list_item, null);
                    txt_name = (TextView) view.findViewById(R.id.txt_name);
                    txt_name.setText(data.name);
                    img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                    img_header.setImageResource(R.mipmap.manage_group);
                    layout_add = (LinearLayout) view.findViewById(R.id.layout_add);
                    layout_manage = (LinearLayout) view.findViewById(R.id.layout_manage);
                    layout_manage.setVisibility(View.GONE);
                    layout_add.setVisibility(View.VISIBLE);
                    btn_add = (Button) view.findViewById(R.id.btn_add);
                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showGroupAddAlert(null);
                        }
                    });
                    break;
                case ItemData.TYPE_DEPARTMENT: //部门操作
                    view = LayoutInflater.from(CompanyManageActivity.this).inflate(R.layout.department_list_item, null);
                    txt_name = (TextView) view.findViewById(R.id.txt_name);
                    LinearLayout layout_operation = (LinearLayout) view.findViewById(R.id.layout_operation);
                    layout_operation.setVisibility(View.VISIBLE);
                    btn_edit = (ImageButton) view.findViewById(R.id.btn_edit);
                    btn_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showGroupAddAlert("修改部门名称");
                        }
                    });
                    btn_dustbin = (ImageButton) view.findViewById(R.id.btn_dustbin);
                    txt_name.setText(data.name);
                    break;
                case ItemData.TYPE_EMPLOYEE_MANAGEMENT: //员工管理
                    view = LayoutInflater.from(CompanyManageActivity.this).inflate(R.layout.company_list_item, null);
                    txt_name = (TextView) view.findViewById(R.id.txt_name);
                    txt_name.setText(data.name);
                    img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
                    img_header.setImageResource(R.mipmap.manage_staff);
                    layout_add = (LinearLayout) view.findViewById(R.id.layout_add);
                    layout_manage = (LinearLayout) view.findViewById(R.id.layout_manage);
                    layout_manage.setVisibility(View.GONE);
                    layout_add.setVisibility(View.VISIBLE);
                    btn_add = (Button) view.findViewById(R.id.btn_add);
                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showMemberAddAlert();
                        }
                    });
                    break;
                case ItemData.TYPE_EMPLOYEE: //员工操作
                    view = LayoutInflater.from(CompanyManageActivity.this).inflate(R.layout.member_list_item, null);
                    txt_name = (TextView) view.findViewById(R.id.txt_name);
                    txt_name.setText(data.name);
                    txt_department = (TextView) view.findViewById(R.id.txt_department);
                    txt_department.setText(data.parentName);
                    btn_dustbin = (ImageButton) view.findViewById(R.id.btn_dustbin);
                    btn_dustbin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO
                            Toast.makeText(CompanyManageActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
            return view;
        }


        @Override
        public int getCount() {
            return (dataList == null) ? 0 : dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return (dataList == null || dataList.size() < i) ? null : dataList.get(i);
        }
    }

    class ItemData {
        static final int TYPE_COMPANY = 0;
        static final int TYPE_DEPARTMENT = 1;
        static final int TYPE_EMPLOYEE_MANAGEMENT = 2;
        static final int TYPE_EMPLOYEE = 3;
        int type;
        String id;
        String name;
        String iconUrl;
        String parentName;
        String companyId;
        String departmentId;

    }

}
