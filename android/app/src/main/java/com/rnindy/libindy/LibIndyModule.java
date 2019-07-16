//  Created by react-native-create-bridge

package com.rnindy.libindy;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Promise;

import java.util.HashMap;
import java.util.Map;

// experimental
import android.system.Os;

import java.io.File;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
// end experimental

// make getExternalFileDir work
import com.facebook.react.bridge.ReactApplicationContext;
import android.content.Context;
import android.os.Environment;
import android.system.ErrnoException;
// end make getExternalFileDir work

public class LibIndyModule extends ReactContextBaseJavaModule {
    public interface API extends Library {

        // wallet.rs
        public int indy_create_wallet(int command_handle, String config, String credentials, Callback cb);
        public int indy_open_wallet(int command_handle, String config, String credentials, Callback cb);
    }
    // public static {
    //     System.loadLibrary("indy");
    // }

    // private ReactApplicationContext reactContext;

    // public RNFSManager(ReactApplicationContext reactContext) {
    //     super(reactContext);
    //     this.reactContext = reactContext;
    // }

    public static final String REACT_CLASS = "LibIndy";
    private static ReactApplicationContext reactContext = null;

    public LibIndyModule(ReactApplicationContext context) {
        // Pass in the context to the constructor and save it so you can emit events
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        super(context);

        reactContext = context;
    }

    
    @Override
    public String getName() {
        // Tell React the name of the module
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        return REACT_CLASS;
    }

    @Override
    public Map<String, Object> getConstants() {
        // Export any constants to be used in your native module
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        final Map<String, Object> constants = new HashMap<>();
        constants.put("EXAMPLE_CONSTANT", "example");

        return constants;
    }

    @ReactMethod
    public void exampleMethod (Promise promise) {
        // An example native method that you will expose to React
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        String text = "Hi from Java!";
        promise.resolve(text);
    }

    @ReactMethod
    public void init (Promise promise) {
        File externalFilesDir = this.getReactApplicationContext().getExternalFilesDir(null);
        String path = externalFilesDir.getAbsolutePath();
        try {
            Os.setenv("EXTERNAL_STORAGE", path, true);
            // Os.setenv("EXTERNAL_STORAGE", getExternalFilesDir(null).getAbsolutePath(), true);
        } catch (ErrnoException e) {
            e.printStackTrace();
}

        API api = Native.loadLibrary("indy", API.class);
        promise.resolve(api);
}

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        // A method for emitting from the native side to JS
        // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }
}
