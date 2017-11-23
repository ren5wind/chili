package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topunion.chili.MyApplication;
import com.topunion.chili.R;
import com.topunion.chili.base.RxBus;
import com.topunion.chili.base.dialog.DialogTemplate;
import com.topunion.chili.base.dialog.UiLibDialog;
import com.topunion.chili.base.dialog.UiLibDialogInterface;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.data.AddEmployess;
import com.topunion.chili.data.Company;
import com.topunion.chili.data.Department;
import com.topunion.chili.data.Employee;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.net.request_interface.GetCorpUserRole;
import com.topunion.chili.net.request_interface.GetFriends;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Author      : renxiaoming
 * Date        : 2017/8/27
 * Description :
 */
@EActivity(R.layout.activity_manage_add_employees)
public class CompanyManageAddEmployessActivity extends BaseAppCompatActivity {
    public final static String RXBUS_ADD_EMPLOYESS = "rxbus_add_Employess";
    private List<Employee> chooseEmployee;
    //    @Extra
    Company company;
    //    @Extra
    String deparmentId;
    @ViewById
    ListView listView;
    @ViewById
    TextView txt_title;
    @ViewById
    Button btn_confirm;

    @ViewById
    TextView txt_department;
    @ViewById
    TextView txt_position_name;

    private MyAdapter myAdapter;

    private String[] departmentArray;
    private String[] role;
    GetCorpUserRole.GetCorpUserRoleResponse roles;

    @AfterViews
    void init() {
        btn_confirm.setText("提交");
        btn_confirm.setVisibility(View.VISIBLE);
        txt_title.setText("员工添加");
        Intent intent = getIntent();
        if (intent != null) {
            company = (Company) intent.getSerializableExtra("company");
            chooseEmployee = (List<Employee>) intent.getSerializableExtra("employees");
        }
        initDeparment();
        initRole();

        myAdapter = new MyAdapter(chooseEmployee);
        listView.setAdapter(myAdapter);
    }

    private void initDeparment() {
        List<Department> departmentList = company.getDepartmentList();
        if (departmentList != null && departmentList.size() > 0) {
            txt_department.setText(departmentList.get(0).getName());
            departmentArray = new String[departmentList.size()];
            for (int i = 0; i < departmentList.size(); i++) {
                departmentArray[i] = departmentList.get(i).getName();
            }
        }
    }

    @Background
    void initRole() {
        roles = HttpHelper_.getInstance_(this).getRoles();
        if (roles.data != null) {
            role = new String[roles.data.size()];
            for (int i = 0; i < roles.data.size(); i++) {
                role[i] = roles.data.get(i).name;
            }
        }
    }

    @Click
    void btn_back() {
        this.finish();
    }

    @UiThread
    void showToast(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Click
    void btn_confirm() {
        if (chooseEmployee == null || chooseEmployee.size() == 0) {
            showToast("您还没有选择人员");
            return;
        }
        AddEmployess data = new AddEmployess();
        for (int i = 0; i < company.getDepartmentList().size(); i++) {
            if ((txt_department.getText().toString().trim()).equals(company.getDepartmentList().get(i).getName())) {
                deparmentId = company.getDepartmentList().get(i).getId();
            }
        }


        data.companyId = company.getId();
        data.deparmentId = deparmentId;

        for (int i = 0; i < roles.data.size(); i++) {
            if ((txt_position_name.getText().toString().trim()).equals(roles.data.get(i).name)) {
                data.roleId = roles.data.get(i).id;
                break;
            }
        }

        data.employees = chooseEmployee;
        RxBus.getInstance().post(RXBUS_ADD_EMPLOYESS, data);
        this.finish();
    }

    @Click
    void layout_group() {
        UiLibDialog.Builder builder = new UiLibDialog.Builder(this, DialogTemplate.TEMPLATE_LIST);

        UiLibDialog uiLibDialog = builder.setTitle("所在组织")
                .setItems(departmentArray, new UiLibDialogInterface.ListOnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, String itemText) {
                        txt_department.setText(itemText);
                    }
                })
                .create();
        uiLibDialog.show();
    }

    @Click
    void layout_position() {
        UiLibDialog.Builder builder = new UiLibDialog.Builder(this, DialogTemplate.TEMPLATE_LIST);
        UiLibDialog uiLibDialog = builder.setTitle("角色分配")
                .setItems(role, new UiLibDialogInterface.ListOnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, String itemText) {
                        txt_position_name.setText(itemText);
                    }
                })
                .create();
        uiLibDialog.show();
    }

    class MyAdapter extends BaseAdapter {
        private List<Employee> dataList;

        MyAdapter(List<Employee> dataList) {
            this.dataList = dataList;
        }

        public void setData(List<Employee> dataList) {
            this.dataList = dataList;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView txt_name;
            SimpleDraweeView img_header;
            ImageButton btn_dustbin;
            final Employee data = dataList.get(i);
            view = LayoutInflater.from(CompanyManageAddEmployessActivity.this).inflate(R.layout.member_list_item, null);
            img_header = (SimpleDraweeView) view.findViewById(R.id.img_header);
            img_header.setImageURI(data.getHeadUrl());
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_name.setText(data.getName());
            btn_dustbin = (ImageButton) view.findViewById(R.id.btn_dustbin);
            btn_dustbin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //移除员工
                    dataList.remove(data);
                    notifyDataSetChanged();
                }
            });
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
}
