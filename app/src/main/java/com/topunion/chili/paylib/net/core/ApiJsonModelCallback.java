package com.topunion.chili.paylib.net.core;


import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.topunion.chili.paylib.Constants;
import com.topunion.chili.paylib.net.interf.ServiceTask;

import java.lang.reflect.Type;

/**
 * ApiCallback
 * json模型callback
 */
public class ApiJsonModelCallback extends ApiCallback {
    boolean DEBUG = false;
    private Type mJsonModelType;

    /**
     * @param serviceTask
     * @param jsonModelType 对应的json模型 class type
     */
    public ApiJsonModelCallback(ServiceTask serviceTask, Type jsonModelType) {
        super(serviceTask);
        mJsonModelType = jsonModelType;
    }


    /**
     * @param serviceTask
     * @param token       对应的json模型 TypeToken
     */
    public ApiJsonModelCallback(ServiceTask serviceTask, TypeToken token) {
        this(serviceTask, token.getType());
    }

    @Override
    public void onCall(int resultCode, Object data) {
        //增加debug打印
        if (DEBUG) {
            Log.d("url====",mTag);
        }

        super.onCall(resultCode, data);
    }

    @Override
    public void onSuccess(Object data, ServiceTask task) throws Exception {
        Object modelForJson = null;
        if (data != null) {
            modelForJson = ApiHelper.getGson().fromJson(data.toString(), mJsonModelType);
        }
        task.complete(Constants.STATE_CODE_SUCCESS, modelForJson);
    }

}
