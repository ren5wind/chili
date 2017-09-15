package com.topunion.chili.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.topunion.chili.base.RxBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author      : renxiaoming
 * Date        : 2017/8/25
 * Description :
 */

public class HeadSettingActivity extends Activity {
    private final int START_ALBUM_REQUESTCODE = 1;
    private final int CAMERA_WITH_DATA = 2;
    private final int CROP_RESULT_CODE = 3;
    public static final String TMP_PATH = "01yitou/account/";
    public static final String INTENT_BUNDLE_KEY_HEADSETTING_TYPE = "intent_bundle_key_headsetting_type";
    public static final int HEADSETTING_TYPE_CAMERA = 0;
    public static final int HEADSETTING_TYPE_LOCAL = 1;
    private final int CODE_FOR_PERMISSION = 200;
    public static final String RXBUS_USER_HEAD_PATH = "rxbus_user_head_path";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    public void initData() {
        Intent intent = getIntent();
        int type = 0;
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            type = bundle.getInt(INTENT_BUNDLE_KEY_HEADSETTING_TYPE);
        }
        if (type == HEADSETTING_TYPE_CAMERA) {
            startCapture();
        } else if (type == HEADSETTING_TYPE_LOCAL) {
            startAlbum();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // String result = null;
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        switch (requestCode) {
            case CROP_RESULT_CODE:
                String path = data.getStringExtra(ClipImageActivity.RESULT_PATH);
//                Bitmap photo = BitmapFactory.decodeFile(path);
                RxBus.getInstance().post(RXBUS_USER_HEAD_PATH, path);
                //TODO 发送路径上传图片
                finish();
                break;
            case START_ALBUM_REQUESTCODE:
                startCropImageActivity(getPath(data.getData()));
                break;
            case CAMERA_WITH_DATA:
                // 照相机程序返回的,再次调用图片剪辑程序去修剪图片
                startCropImageActivity(Environment.getExternalStorageDirectory() + "/" + TMP_PATH
                        + "temp.jpg");
                break;
        }
    }

    // 裁剪图片的Activity
    private void startCropImageActivity(String path) {
//        ClipImageActivity.startActivity(this, path, CROP_RESULT_CODE);
        Intent intent = new Intent(this, ClipImageActivity.class);
        intent.putExtra(ClipImageActivity.KEY, path);
        startActivityForResult(intent, CROP_RESULT_CODE);
    }

    private void startAlbum() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
            intent.setType("image/*");
            startActivityForResult(intent, START_ALBUM_REQUESTCODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, START_ALBUM_REQUESTCODE);
            } catch (Exception e2) {
                e.printStackTrace();
            }
        }
    }

    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private boolean requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<String> permissionsNeeded = new ArrayList<>();

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissionsNeeded.size() == 0) {
                return true;
            }
            String[] permissions = new String[permissionsNeeded.size()];
            for (int i = 0; i < permissionsNeeded.size(); i++) {
                permissions[i] = permissionsNeeded.get(i);
            }
            requestPermissions(permissions, CODE_FOR_PERMISSION);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CODE_FOR_PERMISSION);
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasPermission = 0;
            for (int i = 0; i < permission.length; i++) {
                hasPermission = checkSelfPermission(permission[i]);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    private void startCapture() {
        if (!requestPermission()) {
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(
                Environment.getExternalStorageDirectory() + "/" + TMP_PATH, "temp.jpg");
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_WITH_DATA);
    }

    /**
     * 通过uri获取文件路径
     *
     * @param mUri
     * @return
     */
    public String getFilePath(Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return changeUriToPath(mUri);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    // 获取文件路径通过url
    private String getFilePathByUri(Uri mUri) throws FileNotFoundException {
        Cursor cursor = getContentResolver()
                .query(mUri, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    // 将URI转换为真实路径
    private String changeUriToPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualImageCursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualImageCursor.moveToFirst();
        String currentImagePath = actualImageCursor
                .getString(actual_image_column_index);
        return currentImagePath;
    }

    public String getPath(Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(this, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length == 0 || grantResults.length == 0) {
            return;
        }
        if (requestCode == CODE_FOR_PERMISSION) {
            int permissionCount = 0;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissionCount++;
                }
            }
            if (permissionCount == permissions.length) {
                startCapture();
            } else {
                finish();
            }
        }

    }
}