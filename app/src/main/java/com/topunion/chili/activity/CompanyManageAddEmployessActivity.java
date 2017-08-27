package com.topunion.chili.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Author      : renxiaoming
 * Date        : 2017/8/27
 * Description :
 */
@EActivity(R.layout.activity_manage_add_employees)
public class CompanyManageAddEmployessActivity extends AppCompatActivity {
    public final static String RXBUS_ADD_EMPLOYESS = "rxbus_add_Employess";
    private List<Employee> chooseEmployee;
    //    @Extra
    Company company;
    //    @Extra
    String deparmentId;
    @ViewById
    ListView mListView;
    @ViewById
    TextView txt_title;
    @ViewById
    Button btn_confirm;

    @ViewById
    TextView txt_department;
    @ViewById
    TextView txt_position_name;
    @ViewById
    ListView listView;

    private String[] departmentArray;
    private String[] role;
    GetCorpUserRole.GetCorpUserRoleResponse roles;

    @AfterViews
    void init() {
        btn_confirm.setText("确定");
        btn_confirm.setVisibility(View.VISIBLE);
        txt_title.setText("员工添加");
        Intent intent = getIntent();
        if (intent != null) {
            company = (Company) intent.getSerializableExtra("company");
            chooseEmployee = (List<Employee>) intent.getSerializableExtra("employees");
        }
        initDeparment();
        initRole();
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

    @Click
    void btn_confirm() {
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

}
