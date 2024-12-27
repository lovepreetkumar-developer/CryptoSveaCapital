package com.svea.app.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

import com.svea.app.R;
import com.svea.app.activities.HomeActivity;
import com.svea.app.custom_views.MyToast;
import com.svea.app.interfaces.ResponseInterface;
import com.svea.app.utils.Constants;
import com.svea.app.utils.RuntimePermissionsActivity;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * Created by macminiandroid on 31/07/18.
 */

public abstract class BaseActivity<V extends ViewDataBinding> extends RuntimePermissionsActivity
        implements ResponseInterface {

    Context context;
    protected ProgressDialog progressDialog;
    public boolean check_session = true;
    private static final String TAG = BaseActivity.class.getName();
    protected V binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getContentView());
        this.context = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        onViewReady(savedInstanceState, getIntent());

    }

    public void showToast(String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }

    public void showBaseProgress() {
        progressDialog.show();
    }

    protected void goBack() {
        onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        super.onDestroy();
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
    }

    public void hideBaseProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void startTimer() {

    }

    protected <T> void onCallComplete(Call<T> call, Throwable t) {
        hideBaseProgress();
        if (!call.isCanceled() && t != null) {
            showErrorToast(resolveNetworkError(t));
        }
    }

    protected String resolveNetworkError(Throwable cause) {
        if (cause instanceof UnknownHostException)
            return context.getString(R.string.no_internet);
        else if (cause instanceof SocketTimeoutException)
            return context.getString(R.string.server_error);
        else if (cause instanceof ConnectException)
            return context.getString(R.string.no_internet);
        else if (cause instanceof JsonSyntaxException)
            return context.getString(R.string.parser_error);
        return context.getString(R.string.wrong);
    }

    protected void handleCodes(int s) {
        if (s == 401) {
            logoutUser();
        }
    }

    protected void logoutUser() {
        /*if (PrefUtils.getInstance().getUser() != null) {
            if (PrefUtils.getInstance().clear()) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                ActivityCompat.finishAffinity(BaseActivity.this);
            }
        }*/
    }


    protected void goToHomeScreen() {
        // if (PrefUtils.getInstance().getUser() != null) {

        //   LoginBean.DataBean loginBean = PrefUtils.getInstance().getUser();

        Intent intent;

        // if (loginBean != null) {
        intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        //}
        //}
    }

    public void DebugLog(String message) {
        if (Constants.SHOW_LOG)
            Log.d(context.getClass().getSimpleName(), "" + message);
    }

    @Override
    protected void onPause() {
        super.onPause();
        check_session = false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    protected abstract int getContentView();

    protected void setBaseCallback(BaseCallback baseCallback) {
        binding.setVariable(BR.callback, baseCallback);
    }


    protected void goNextTransition() {
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    protected void goBackTransition() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        //Log.e("TAG", "Activity Minimized");
    }

    protected void showSuccessToast(String message) {
        MyToast.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showInfoToast(String message) {
        MyToast.info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showWarnToast(String message) {
        MyToast.warning(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void goBackScreen() {
        onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    protected void showErrorToast(Throwable t) {
        try {
            showErrorToast(t.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected Map<String, String> getHeader() {
        Map<String, String> map = new HashMap<>();
        /*LoginBean.DataBean bean = PrefUtils.getInstance().getUser();
        if (bean != null) {
            map.put(Constants.KEY_USERID, String.valueOf(bean.getId()));
            map.put(Constants.KEY_SESSION, bean.getSessionkey());

        }*/
        return map;

    }

    protected void showErrorToast(Exception t) {
        try {
            showErrorToast(t.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected void showErrorToast(String t) {
        MyToast.error(context, "" + t, Toast.LENGTH_LONG, true).show();
    }
}
