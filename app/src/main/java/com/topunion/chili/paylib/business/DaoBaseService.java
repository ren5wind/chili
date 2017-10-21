package com.topunion.chili.paylib.business;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 与Dao相关的BaseService，
 * 这里会自动的判断 继承 DaoBaseService 的直接子类的泛型类获取需要生成对应的T类型，可以方便的使用 load ,find,save 等操作
 * 具体使用方式：</br>
 * 如 UserService extents DaoBaseService<User,Integer></br>
 * 这里就会自动把 User 作为类型，</br>
 * <i>注意，不支持多层继承 来获取 T 的类型，如果 StudentService<Student,Integer> -> UserService ->DaoBaseService </br>
 * 这里只会根据 DaoBaseService 的直接子类的泛型类型来 判断T，也就是说 是UserService的继承泛型值 Object类型
 * </i>
 *</br>
 * <i>新增支持多层 继承，但是 再每一层都要去重写 getTypeClass 方法</i>
 * @param <T>  目标类型 如User
 * @param <ID> 目标类型 id 如 Integer
 */
public abstract class DaoBaseService<T, ID> {
    protected Class mEntityClass;//当前对应的泛型T的类

    public DaoBaseService() {
        mEntityClass = getTypeClass();
    }

    /**
     * 获取对应的 Dao 的类,对应T
     * 如果 在使用多层继承的时候 必须 每次一层都需要重写此方法</br>
     * 比如  BaseService<T,ID> extends DaoBaseService<T,ID> </br>
     * 则需要在 BaseService 重写该方法 </br>
     * return getSuperClassGenricType(getClass(),BaseService.class, 0);
     *
     * @return
     */
    protected Class getTypeClass(){
        return getSuperClassGenricType(getClass(),DaoBaseService.class, 0);
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz clazz The class to introspect
     * @param topSuperClass 最后一层父类
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("unchecked")
    protected Class<Object> getSuperClassGenricType(final Class clazz,final Class topSuperClass, final int index) {


        Class superClass = clazz;
        //往上遍历到 baseService 的子类
        Class tmpClass = superClass;

        while (superClass != topSuperClass) {
            tmpClass = superClass;
            superClass = superClass.getSuperclass();
        }

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = tmpClass.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取 数据库管理器
     *
     * @return
     */
    protected abstract DatabaseManager getDatabseManager();

    /**
     * 获取对应T的dao
     *
     * @return
     */
    public Dao<T, ID> getDao() {
        return getDatabseManager().getDao(mEntityClass);
    }

    /**
     * 保存
     *
     * @param entity
     * @return 是否成功
     */
    public boolean save(T entity) {
        boolean isSuccess = false;
        try {
            Dao.CreateOrUpdateStatus status = getDao().createOrUpdate(entity);
            isSuccess = status.getNumLinesChanged() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 删除
     *
     * @param entity
     */
    public boolean delete(T entity) {
        boolean isSuccess = false;
        try {
            isSuccess = getDao().delete(entity) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 读取
     *
     * @param id
     * @return
     */
    public T load(ID id) {
        try {
            return getDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询，根据 params
     *
     * @param params
     * @return
     */
    public List<T> find(Map<String, Object> params) {
        QueryBuilder builder = getQueryBuilder();

        try {
            if (params != null && params.size() > 0) {
                Where where = builder.where();
                Iterator<String> it = params.keySet().iterator();
                while (it.hasNext()) {
                    String columName = it.next();
                    where.eq(columName, params.get(columName));
                    if (it.hasNext()) {
                        where.and();
                    }
                }
            }

            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询
     *
     * @param builder
     * @return
     */
    public List<T> find(QueryBuilder<T, ID> builder) {
        try {
            return builder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    /**
     * 刷新数据
     *
     * @param entity
     */
    public void refresh(T entity) {
        try {
            getDao().refresh(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取查询的builder
     *
     * @return
     */
    public QueryBuilder<T, ID> getQueryBuilder() {
        return getDao().queryBuilder();
    }

}
