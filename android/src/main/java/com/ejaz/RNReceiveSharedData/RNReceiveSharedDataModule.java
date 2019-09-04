
package com.ejaz.RNReceiveSharedData;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNReceiveSharedDataModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNReceiveSharedDataModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNReceiveSharedData";
  }
}