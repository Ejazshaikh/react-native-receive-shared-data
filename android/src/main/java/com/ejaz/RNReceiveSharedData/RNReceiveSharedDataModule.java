
package com.ejaz.RNReceiveSharedData;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import java.util.ArrayList;

public class RNReceiveSharedDataModule extends ReactContextBaseJavaModule {

  enum Type {
    TEXT,
    IMAGE,
    IMAGES
  }

  private final ReactApplicationContext reactContext;
  private Promise promise;

  public RNReceiveSharedDataModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  private void checkPermission() {
    Activity activity = reactContext.getCurrentActivity();
    final int readPermission = ActivityCompat
            .checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
    final boolean permissionsGranted = readPermission == PackageManager.PERMISSION_GRANTED;
    if (!permissionsGranted) {
      ((PermissionAwareActivity) activity).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1, new PermissionListener() {
        @Override
        public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
          if (requestCode == 1) {

            for (int grantResult : grantResults) {
              if (grantResult == PackageManager.PERMISSION_DENIED) {
                promise.reject("E_PERMISSIONS_MISSING", "Required permission missing");
                return false;
              }
            }
            readSharedData();
            return true;
          }
          return false;
        }
      });
    } else {
      readSharedData();
    }
  }

  private void readSharedData() {
    Activity activity = reactContext.getCurrentActivity();
    Intent intent = activity.getIntent();
    String action = intent.getAction();
    String type = intent.getType();

    if (Intent.ACTION_SEND.equals(action) && type != null) {
      if ("text/plain".equals(type)) {
        handleSendText(intent);
      } else if (type.startsWith("image/")) {
        handleSendImage(intent);
      }
    } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
      if (type.startsWith("image/")) {
        handleSendMultipleImages(intent);
      }
    }
  }


  @ReactMethod
  public void getSharedData(final Promise promise) {
    this.promise = promise;
    checkPermission();
  }

  private void handleSendText(Intent intent) {
    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
    if (sharedText != null) {
      WritableMap map = new WritableNativeMap();
      WritableMap textMap = new WritableNativeMap();
      textMap.putString("text", sharedText);
      textMap.putString("type", intent.getType());

      map.putMap("data", textMap);
      map.putString("type", Type.TEXT.name());
      promise.resolve(map);
    }
  }

  private void handleSendImage(Intent intent) {
    Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    if (imageUri != null) {
      WritableMap map = new WritableNativeMap();
      map.putMap("data", toImageMap(imageUri, intent.getType()));
      map.putString("type", Type.IMAGE.name());
      promise.resolve(map);
    }
  }

  private WritableMap toImageMap(Uri imageUri, String type) {
    Context context = getCurrentActivity().getApplicationContext();
    String path = getRealPathFromURI(context, imageUri);
    WritableMap imageMap = new WritableNativeMap();
    imageMap.putString("uri", imageUri.toString());
    imageMap.putString("path", path);
    imageMap.putString("type", type);
    return imageMap;
  }

  private void handleSendMultipleImages(Intent intent) {
    ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
    if (imageUris != null) {
      WritableArray images = new WritableNativeArray();
      String type = intent.getType();
      for (Uri imageUri : imageUris) {
        images.pushMap(toImageMap(imageUri, type));
      }
      WritableMap map = new WritableNativeMap();

      map.putArray("data", images);
      map.putString("type", Type.IMAGES.name());
      promise.resolve(map);
    }
  }

  private String getRealPathFromURI(Context context, Uri contentUri) {
    Cursor cursor = null;
    try {
      String[] proj = {MediaStore.Images.Media.DATA};
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  @Override
  public String getName() {
    return "RNReceiveSharedData";
  }
}