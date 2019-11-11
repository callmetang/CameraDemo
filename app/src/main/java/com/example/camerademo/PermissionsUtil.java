package com.example.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.ValueCallback;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * 权限申请
 * Created by tdh on 2019/6/24.
 */

public class PermissionsUtil {
    public static final String[] PERMISSIONS_ALL = {
//            Manifest.permission.GET_TASKS,
//            Manifest.permission.INTERNET,
//            Manifest.permission.ACCESS_NETWORK_STATE,
//            Manifest.permission.ACCESS_WIFI_STATE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.VIBRATE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
//            Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    public static final String[] PERMISSIONS_START = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static final String[] PERMISSIONS_GPS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET};

    public static final String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final String[] PERMISSIONS_SYSTEM_ALERT_WINDOW = {Manifest.permission.SYSTEM_ALERT_WINDOW,
            "android.permission.SYSTEM_OVERLAY_WINDOW", };

    /**
     * @param activity
     * @param callback
     * @param args
     */
    public static void checkPermissions(final Context activity, final ValueCallback<Boolean> callback, String... args) {
        if (activity == null) {
            return;
        }
        if (!(activity instanceof Activity)) {
            Log.d("AlbumFragment","不是activity");
            return;
        }
        Dexter.withActivity((Activity) activity)
                .withPermissions(args)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Log.d("AlbumFragment", "multiplePermissionsReport.areAllPermissionsGranted():" + multiplePermissionsReport.areAllPermissionsGranted());
                        Log.d("AlbumFragment", "multiplePermissionsReport.isAnyPermissionPermanentlyDenied():" + multiplePermissionsReport.isAnyPermissionPermanentlyDenied());
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            //所有权限都允许
                            Log.d("AlbumFragment","所有权限都允许");
                            if (callback != null) {
                                callback.onReceiveValue(true);
                            }
                        } else if (multiplePermissionsReport.areAllPermissionsGranted() == false ||
                                !multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            if (((Activity) activity).isFinishing()) {
                                return;
                            }
                            //拒绝权限,并且下次不允许弹出授权对话框时
                            new AlertDialog.Builder(activity).setTitle("提示")
                                    .setMessage("无法获取权限，请去设置")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (callback != null) {
                                                callback.onReceiveValue(false);
                                            }
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                                                intent.putExtra("cmp", "com.android.settings/.applications.InstalledAppDetails");
                                                intent.addCategory("android.intent.category.DEFAULT");
                                                activity.startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        if (permissionToken != null) {
                            permissionToken.continuePermissionRequest();
                        }
                    }
                }).check();
    }
}
