package org.unyde.mapintegrationlib.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.*;
import java.util.List;
import java.util.Random;

public class Helper {

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private static final int sizeOfIntInHalfBytes = 8;
    private static final int numberOfBitsInAHalfByte = 4;
    private static final int halfByte = 0x0F;
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String decToHex(int dec) {
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i) {
            int j = dec & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            dec >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
    }


    public static String toHexString(byte[] bytes) {
        if (bytes.length == 0) {
            return "";
        }
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int c = bytes[i] & 0xFF;
            chars[i * 2] = HEX[c >>> 4];
            chars[i * 2 + 1] = HEX[c & 0x0F];
        }
        return new String(chars).toLowerCase();
    }

    public static String randomHexString(int length) {
        byte[] buf = new byte[length];
        new Random().nextBytes(buf);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02X", buf[i]));
        }
        return stringBuilder.toString();
    }

    public static String ByteArrayToString(byte[] ba) {
        StringBuilder hex = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            hex.append(b + " ");

        return hex.toString();
    }
   /* public static String getImeiNumber(Context c) {
        final TelephonyManager telephonyManager= (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //getDeviceId() is Deprecated so for android O we can use getImei() method
            return telephonyManager.getImei();
        }
        else {
            return telephonyManager.getDeviceId();
        }

    }*/

    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static byte[] hex2Byte(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer
                    .parseInt(str.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static byte[] toByteArray(String hexString) {
        // hexString guaranteed valid.
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static String userIdInHex(int num) {
        String str = "";
        try {
            String hex = Integer.toHexString(num);
            str = String.format("%0" + (10 - hex.length()) + "d%s", 0, hex);
        } catch (Exception e) {
            Log.e("UserIDHEX", e.getMessage());
        }

        return str;
    }


    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }



    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null)
            return false;
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        return isConnected;

    }


    public static void bluetoothEnablingWhenScanFailed(final BluetoothAdapter mBluetoothAdapter) {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                            mBluetoothAdapter.enable();
                            Log.d("Helper Blue",""+mBluetoothAdapter.getState());
                           // MyApplication.getInstance().getServiceInstance()
                            break;
                        } else {
                            break;
                        }
                    }
                }

            }).start();
        }
    }


    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {


       // try{
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.i("isMyServiceRunning?", true + "");
                    return true;
                }
            }
            Log.i("isMyServiceRunning?", false + "");
     /*   }
        catch(Exception e)
        {

        }*/

        return false;
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public static Boolean isActivityRunning(Class activityClass, Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }
    public static double[] getLatLngByRange(double latitude, double longitude, double distance, String unit) {
        // radius of earth; @note: the earth is not perfectly spherical, but this is considered the 'mean radius'
        double radius = 0.0;
        double[] res = new double[4];
        if (unit == "km") {
            radius = 6371.009;
        } else if (unit == "mi") {
            radius = 3958.761;
        }
        // latitude boundaries

        /*minLatitude*/
        res[0] = latitude - Math.toDegrees(distance / radius);
        /*maxLatitude*/
        res[1] = latitude + Math.toDegrees(distance / radius);
        /*minLongitude*/
        res[2] = (longitude - Math.toDegrees(distance / radius) / (Math.cos(Math.toRadians(latitude))));
        /*maxLongitude*/
        res[3] = (longitude + Math.toDegrees(distance / radius) / (Math.cos(Math.toRadians(latitude))));

        return res;
    }


    public static Boolean isStoreInRange(double[] res, double store_latitude, double store_longitude) {
       /* if(is_null($storeCoordinates['gps_lat']) || is_null($storeCoordinates['gps_long'])) {
            return false;
        }*/

        Boolean latInRange = false;
        Boolean longInRange = false;


        if (store_latitude>=res[0] && store_latitude<=res[1])
        {
            latInRange = true;
        }

        if (store_longitude>=res[2] && store_longitude<=res[3])
        {
            longInRange = true;
        }


        if (latInRange && longInRange) {
            return true;
        }

        return false;
    }

  /*  public static void load_fragment(FragmentActivity activity, Fragment fragment){
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.new_home_frags,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }*/


    public static boolean copyAssetFolder(AssetManager assetManager,
                                          String fromAssetPath, String toPath) {
        try {
          //  Uri uri=Uri.parse(fromAssetPath);
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyAsset(AssetManager assetManager,
                                    String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            Uri uri= Uri.parse(fromAssetPath);
            in = assetManager.open(uri.toString());
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static Boolean dateComarison( String oldDate,  String newDate) {
        Boolean isUpdateAvailabe = false;
        if (newDate.compareTo(oldDate) < 0) {
            isUpdateAvailabe = false;
        } else if (newDate.compareTo(oldDate) > 0) {
            isUpdateAvailabe = true;
        } else {
            isUpdateAvailabe = false;
        }

        return isUpdateAvailabe;
    }


}


