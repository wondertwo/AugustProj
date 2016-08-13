package me.wondertwo.august0802.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * NetConnectUtils网络连接工具类
 *
 * Created by wondertwo on 2016/4/6.
 */
public class NetStateUtils {

    /**
     * 判断网络是否可用 isNetworkAvailable()
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * 检查 WIFI 连接情况 checkWifiConnection()
     */
    public static boolean checkWifiConnection(Context context) {

        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiConn = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (wifiConn == NetworkInfo.State.CONNECTED || wifiConn== NetworkInfo.State.CONNECTING) {
            Log.e("wifi connection", "true");
            return true;
        } else {
            Log.e("wifi connection", "false");
            return false;
        }
    }

    /**
     * 检查网络连接情况
     */
    public static boolean checkNetworkConnection(Context context) {

        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mobileInfo.isAvailable() || wifiInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取本地 IP 地址
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements(); ) {

                NetworkInterface networkInterface = enumeration.nextElement();

                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference Ip", ex.getMessage());
        }
        return null;
    }
}
