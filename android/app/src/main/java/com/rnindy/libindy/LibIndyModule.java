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

// Maven import stuff
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.LibIndy;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;
// end Maven import stuff

// experimental
import android.system.Os;
import android.util.Log;

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
    public void init () {
        File externalFilesDir = this.getReactApplicationContext().getExternalFilesDir(null);
        String path = externalFilesDir.getAbsolutePath();
        Log.v("LIBINDY", "this is the path" + path);
        try {
            Os.setenv("EXTERNAL_STORAGE", path, true);
            // Os.setenv("EXTERNAL_STORAGE", getExternalFilesDir(null).getAbsolutePath(), true);
        } catch (ErrnoException e) {
            e.printStackTrace();
        }

        LibIndy.init();
        Log.v("LIBINDY", "Outside init()");
    }

    @ReactMethod
    public void createWallet (Promise promise) {
        // An example native method that you will expose to React
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        Wallet wallet = null;
        int handle = -1;

        Log.v("LIBINDY", "Inside createWallet()");
        try {
            final String WALLET = "Wallet1";
            final String TYPE = "default";
            final String WALLET_CREDENTIALS =
                    new JSONObject()
                            .put("key", "key")
                            .toString();
            final String WALLET_CONFIG =
                    new JSONObject()
                            .put("id", WALLET)
                            .put("storage_type", TYPE)
                            .toString();
            Log.v("LIBINDY", "About to try to create wallet...");
            try {
                Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
            } catch (ExecutionException e) {
                Log.v("LIBINDY", "ExecutionException:", e);
                System.out.println( e.getMessage() );
                if (e.getMessage().indexOf("WalletExistsException") >= 0) {
                    // ignore
                } else {
                    throw new RuntimeException(e);
                }
            }
            Log.v("LIBINDY", "About to try to OPEN wallet...");
            wallet = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
            System.out.println("===================> wallet:" + wallet);
            handle = wallet.getWalletHandle();
        } catch (IndyException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (wallet != null) {
                try {
                    Log.v("LIBINDY", "Closing wallet...");
                    wallet.closeWallet().get();
                    Log.v("LIBINDY", "Wallet closed!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Log.v("LIBINDY", "Leaving createWallet()");
        promise.resolve( Integer.toString(handle) );
    }

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        // A method for emitting from the native side to JS
        // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }
}
