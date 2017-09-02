package com.topunion.chili.activity;

import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.topunion.chili.base.RxBus;
import com.topunion.chili.business.CompanyManager;
import com.topunion.chili.data.AddEmployess;
import com.topunion.chili.data.Company;
import com.topunion.chili.data.Department;
import com.topunion.chili.data.Employee;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    Company company;
    private List<ItemData> itemDataList;

    private CompanyManager mCompanyManager;

    private MyAdapter myAdapter;

    public static final int REQUEST_CODE_GET_EMPLOYEE = 1;
    public static final String RXBUS_UPDATE_COMPANY = "rxbus_update_company";

    @Click
    void btn_back() {
        this.finish();
    }

    @Click
    void mSearchInput() {
        SearchFromManualActivity_.intent(this).company(company).startForResult(0);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null)
//            return;
//        String companyId = data.getStringExtra("companyId");
//        String deparmentId = data.getStringExtra("deparmentId");
//        String role = data.getStringExtra("role");
//        List<Employee> employees = (List<Employee>) data.getSerializableExtra("employees");
//        addEmployeeRequest(Integer.valueOf(companyId), employees, Integer.valueOf(deparmentId), role);
//    }

    @AfterViews
    void init() {
        mCompanyManager = new CompanyManager(this);
        //数据转换
        itemDataList = analysisOrganization(company);

        txt_title.setVisibility(View.GONE);
        search_layout.setVisibility(View.VISIBLE);
        myAdapter = new MyAdapter(itemDataList);
        mListView.setAdapter(myAdapter);
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

    private List<ItemData> analysisOrganization(Company company) {
        List<ItemData> itemDatas = new ArrayList<>();
        if (company == null) {
            return null;
        }
        List<Employee> employees = new ArrayList<>();
        //添加企业
        ItemData itemDataCompany = new ItemData();
        itemDataCompany.type = ItemData.TYPE_COMPANY;
        itemDataCompany.name = company.getName();
        itemDataCompany.companyId = company.getId();
        itemDatas.add(itemDataCompany);
        //添加部门
        List<Department> departments = company.getDepartmentList();
        for (int j = 0; departments != null && j < departments.size(); j++) {
            Department department = departments.get(j);
            ItemData itemDataDepartment = new ItemData();
            itemDataDepartment.type = ItemData.TYPE_DEPARTMENT;
            itemDataDepartment.name = department.getName();
            itemDataDepartment.departmentId = department.getId();
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
        itemDatas.add(itemEmployeeManagement);
        //添加员工
        for (int k = 0; employees != null && k < employees.size(); k++) {
            Employee employee = employees.get(k);
            ItemData itemDataEmployee = new ItemData();
            itemDataEmployee.type = ItemData.TYPE_EMPLOYEE;
            itemDataEmployee.name = employee.getName();
            itemDataEmployee.employeeid = employee.getId();
            itemDataEmployee.parentName = employee.getDeptName();
            itemDataEmployee.iconUrl = employee.getHeadUrl();
            itemDataEmployee.companyId = employee.getCompanyId();
            itemDataEmployee.departmentId = employee.getDeptId();
            itemDatas.add(itemDataEmployee);
        }
        return itemDatas;
    }

    private void showMemberAddAlert(final String companyId) {
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
                        addEmployeeListener();
                        Intent intent1 = new Intent(CompanyManageActivity.this, SearchFromManualActivity_.class);
                        intent1.putExtra("companyId", companyId);
                        intent1.putExtra("company", company);
                        intent1.putExtra("viewType", SearchFromManualActivity_.TYPE_NORMAL);
                        startActivity(intent1);
                        break;
//                    case R.id.btn_from_contact:
//                        startActivity(new Intent(CompanyManageActivity.this, SearchFromContactActivity_.class));
//                        break;
                    case R.id.btn_from_friedns:
//                        startActivity(new Intent(CompanyManageActivity.this, SearchFromFriendsActivity_.class));
                        addEmployeeListener();
                        Intent intent = new Intent(CompanyManageActivity.this, SearchFromFriendsActivity_.class);
                        intent.putExtra("companyId", companyId);
                        intent.putExtra("company", company);

                        startActivityForResult(intent, REQUEST_CODE_GET_EMPLOYEE);
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

    private void addEmployeeListener() {
        //监听添加员工
        Observable<AddEmployess> addEmployessCallBackobservable = RxBus.getInstance().register(CompanyManageAddEmployessActivity.RXBUS_ADD_EMPLOYESS);
        addEmployessCallBackobservable.observeOn(Schedulers.io())
                .subscribe(new Subscriber<AddEmployess>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AddEmployess data) {
                        RxBus.getInstance().unregister(CompanyManageAddEmployessActivity.RXBUS_ADD_EMPLOYESS);
                        addEmployeeRequest(Integer.valueOf(data.companyId), data.employees, Integer.valueOf(data.deparmentId), data.roleId);
                    }
                });
    }

    private void showGroupAddAlert(@TYPE final int type, String title, final String companyId, final String deparmentId) {
        final AlertDialog alertDialog = new AlertDialog.Builder(CompanyManageActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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
                    switch (type) {
                        case TYPE_ADD_DEPARMENT:
                            addDeparmentRequest(Integer.parseInt(companyId), name);
                            break;
                        case TYPE_UPDATE_DEPARMENT:
                            updateDeparmentRequest(Integer.parseInt(deparmentId), name);
                            break;
                    }
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(CompanyManageActivity.this, "组织名称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void addEmployeeRequest(int corpId, List<Employee> employees,
                            int corpDeptId, String roleId) {
        int size = employees.size();
        List<String> userIds = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            userIds.add(employees.get(i).getId());
        }

        boolean isSuccess = mCompanyManager.addEmployee(corpId, userIds, corpDeptId, roleId);
        addEmployee(employees, String.valueOf(corpDeptId));
        updateAdapter(isSuccess, "当前网络不佳，添加员工失败");
    }

    @Background
    void deleteEmployeeRequest(int employeeId, int departmentId) {
        boolean isSuccess = mCompanyManager.deleteEmployee(employeeId);
        deleteEmployee(String.valueOf(departmentId), String.valueOf(employeeId));
        updateAdapter(isSuccess, "当前网络不佳，删除员工失败");
    }

    @Background
    void addDeparmentRequest(int companyId, String name) {
        int id = mCompanyManager.addDepartMent(companyId, name);
        addDeparment(name, id);
        updateAdapter(id != -1, "当前网络不佳，添加部门失败");
    }

    @Background
    void updateDeparmentRequest(int id, String name) {
        boolean isSuccess = mCompanyManager.updateDepartment(id, name);
        updateDeparment(name, String.valueOf(id));
        updateAdapter(isSuccess, "当前网络不佳，修改部门失败");

    }

    @Background
    void deleteDepartmentRequest(int id) {
        boolean isSuccess = mCompanyManager.deleteDepartment(id);
        deleteDepartment(String.valueOf(id));
        updateAdapter(isSuccess, "当前网络不佳，删除部门失败");

    }

    @UiThread
    void updateAdapter(final boolean isUpdate, final String toast) {
        if (isUpdate) {
            myAdapter.setData(itemDataList);
            myAdapter.notifyDataSetChanged();
            RxBus.getInstance().post(RXBUS_UPDATE_COMPANY, true);
        } else {
            Toast.makeText(CompanyManageActivity.this, toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void addEmployee(List<Employee> addEmployeeList, String departmentId) {
        List<Department> departmentList = company.getDepartmentList();
        int size = departmentList.size();
        for (int i = 0; i < size; i++) {
            Department department = departmentList.get(i);
            if (departmentId.equals(department.getId())) {
                for (int j = 0; j < addEmployeeList.size(); j++) {
                    addEmployeeList.get(i).setDeptId(departmentId);
                    addEmployeeList.get(i).setDeptName(department.getName());
                }
                department.getEmployeeList().addAll(addEmployeeList);
                break;
            }
        }
        itemDataList = analysisOrganization(company);
    }

    private void deleteEmployee(String departmentId, String employeeId) {
        List<Department> departmentList = company.getDepartmentList();
        int size = departmentList.size();
        DEPARTMENT:
        for (int i = 0; i < size; i++) {
            Department department = departmentList.get(i);
            if (departmentId.equals(department.getId())) {
                List<Employee> employeeList = department.getEmployeeList();
                for (int j = 0; j < employeeList.size(); j++) {
                    Employee employee = employeeList.get(j);
                    if (employeeId.equals(employee.getId())) {
                        employeeList.remove(employee);
                        break DEPARTMENT;
                    }
                }
            }
        }
        itemDataList = analysisOrganization(company);
    }

    private void addDeparment(String name, int id) {
        Department department = new Department();
        department.setName(name);
        department.setId(String.valueOf(id));
        company.getDepartmentList().add(department);
        itemDataList = analysisOrganization(company);
    }

    private void updateDeparment(String name, String id) {
        List<Department> departmentList = company.getDepartmentList();
        int size = departmentList.size();
        for (int i = 0; i < size; i++) {
            Department department = departmentList.get(i);
            if (id.equals(department.getId())) {
                department.setId(id);
                department.setName(name);
                break;
            }
        }
        itemDataList = analysisOrganization(company);
    }

    private void deleteDepartment(String id) {
        List<Department> departmentList = company.getDepartmentList();
        int size = departmentList.size();
        for (int i = 0; i < size; i++) {
            Department department = departmentList.get(i);
            if (id.equals(department.getId())) {
                departmentList.remove(department);
                break;
            }
        }
        itemDataList = analysisOrganization(company);
    }

    class MyAdapter extends BaseAdapter {
        private List<ItemData> dataList;

        MyAdapter(List<ItemData> dataList) {
            this.dataList = dataList;
        }

        public void setData(List<ItemData> dataList) {
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
            final ItemData data = dataList.get(i);
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
                            showGroupAddAlert(TYPE_ADD_DEPARMENT, "添加部门", data.companyId, data.departmentId);
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
                            showGroupAddAlert(TYPE_UPDATE_DEPARMENT, "修改部门名称", data.companyId, data.departmentId);
                        }
                    });
                    btn_dustbin = (ImageButton) view.findViewById(R.id.btn_dustbin);
                    btn_dustbin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteDepartmentRequest(Integer.parseInt(data.departmentId));
                        }
                    });
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
                            showMemberAddAlert(data.companyId);
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
                            deleteEmployeeRequest(Integer.parseInt(data.employeeid), Integer.parseInt(data.departmentId));
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
        String employeeid;
        String name;
        String iconUrl;
        String parentName;
        String companyId;
        String departmentId;
    }

    private static final int TYPE_ADD_DEPARMENT = 0;
    private static final int TYPE_UPDATE_DEPARMENT = 1;
    private static final int TYPE_ADD_EMPLOYEE = 2;
    private static final int TYPE_UPDATE_EMPLOYEE = 3;

    @IntDef({TYPE_ADD_DEPARMENT, TYPE_UPDATE_DEPARMENT, TYPE_ADD_EMPLOYEE, TYPE_UPDATE_EMPLOYEE})
    @Retention(RetentionPolicy.SOURCE)
    @interface TYPE {
    }
}
