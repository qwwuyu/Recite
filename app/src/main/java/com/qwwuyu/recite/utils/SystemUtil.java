package com.qwwuyu.recite.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * 系统工具类
 */
public class SystemUtil {

    public static PackageManager getPackgeManager(Context context) {
        return context.getPackageManager();
    }

    /**
     * 获取当前系统版本号
     *
     * @return versionCode
     */
    public static int getCurrentVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取当前系统版本名称
     *
     * @return versionName
     */
    public static String getCurrentVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 获取应用信息
     *
     * @return
     */
    public static ApplicationInfo getAppInfo(Context context) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackgeManager(context).getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationInfo;
    }

    /** 获取android系统版本号 */
    public static String getOSVersion() {
        String release = Build.VERSION.RELEASE; // android系统版本号
        release = "android" + release;
        return release;
    }

    /** 获得android系统sdk版本号 */
    public static String getOSVersionSDK() {
        return Build.VERSION.SDK;
    }

    /** 获得android系统sdk版本号 */
    public static int getOSVersionSDKINT() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 读取清单文件中的meta data
     */
    public static String getMetaData(Context context, String key) {
        ApplicationInfo info;
        String value = null;
        try {
            info = getPackgeManager(context).getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = info.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /** 获取手机型号 */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /** 获取设备的IMEI */
    public static String getIMEI(Context context) {
        if (null == context) {
            return null;
        }
        String imei = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception ignored) {
        }
        return imei;
    }

    /** 检测手机是否已插入SIM卡 */
    public static boolean isCheckSimCardAvailable(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /** sim卡是否可读 */
    public static boolean isCanUseSim(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception ignored) {
        }
        return false;
    }

    /** 取得当前sim手机卡的imsi */
    public static String getIMSI(Context context) {
        String imsi = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
        } catch (Exception ignored) {
        }
        return imsi;
    }

    /** 返回本地手机号码，这个号码不一定能获取到 */
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    /** 返回手机服务商名字 */
    public static String getProvidersName(Context context) {
        String ProvidersName;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = getIMSI(context);
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        } else {
            ProvidersName = "其他服务商:" + IMSI;
        }
        return ProvidersName;
    }

    /** 获取当前设备的SN */
    public static String getSimSN(Context context) {
        String simSN = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            simSN = tm.getSimSerialNumber();
        } catch (Exception ignored) {
        }
        return simSN;
    }

    /** 获取当前设备的MAC地址 */
    public static String getMacAddress(Context context) {
        String mac = null;
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wm.getConnectionInfo();
            mac = info.getMacAddress();
        } catch (Exception ignored) {
        }
        return mac;
    }

    /** 获得设备ip地址 */
    public static String getLocalAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ignored) {
        }
        return null;
    }

    /**
     * 获取设备的Pseudo-Unique ID
     * 这个在任何Android手机中都有效
     *
     * @return
     */
    public static String getDeviceId() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    /**
     * 获取设备android id
     * 不可靠的
     *
     * @param context 上下文
     * @return android id
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取蓝牙mac地址
     * 要加入android.permission.BLUETOOTH 权限.
     */
    public static String getBTMac() {
        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return m_BluetoothAdapter.getAddress();
    }

    /**
     * 生成设备唯一标识
     *
     * @return 唯一码
     */
    public static String getPhoneUid(Context context) {
        String m_szImei = getIMEI(context);
        String m_szDevIDShort = getDeviceId();
        String m_szAndroidID = getAndroidId(context);
        String m_szWLANMAC = getMacAddress(context);
        String m_szBTMAC = getBTMac();
        String m_szLongID = m_szImei + m_szDevIDShort
                + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
// compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
// create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
// if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
// add number to string
            m_szUniqueID += Integer.toHexString(b);
        }   // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }

    /** 获取屏幕的分辨率 */
    @SuppressWarnings("deprecation")
    public static int[] getResolution(Context context) {
        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int[] res = new int[2];
        res[0] = windowMgr.getDefaultDisplay().getWidth();
        res[1] = windowMgr.getDefaultDisplay().getHeight();
        return res;
    }

    /**
     * 获取通知栏高度
     */
    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    /**
     * 获取导航栏高度
     *
     * @param activity activity
     * @return 高度
     */
    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }

    /** 获得设备的横向dpi */
    public static float getWidthDpi(Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().densityDpi;
    }

    /** 获得设备的纵向dpi */
    public static float getHeightDpi(Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().ydpi;
    }

    /** 获取设备信息 */
    public static String[] getDivceInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException ignored) {
        }
        return cpuInfo;
    }

    /** 判断手机CPU是否支持NEON指令集 */
    public static boolean isNEON() {
        boolean isNEON = false;
        String cupinfo = getCPUInfos();
        if (cupinfo != null) {
            cupinfo = cupinfo.toLowerCase();
            isNEON = cupinfo != null && cupinfo.contains("neon");
        }
        return isNEON;
    }

    /** 读取CPU信息文件，获取CPU信息 */
    @SuppressWarnings("resource")
    private static String getCPUInfos() {
        String str1 = "/proc/cpuinfo";
        String str2;
        StringBuilder resusl = new StringBuilder();
        String resultStr = null;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                resusl.append(str2);
            }
            resultStr = resusl.toString();
            return resultStr;
        } catch (IOException ignored) {
        }
        return resultStr;
    }

    /** 获取当前设备cpu的型号 */
    public static int getCPUModel() {
        return matchABI(getSystemProperty("ro.product.cpu.abi")) | matchABI(getSystemProperty("ro.product.cpu.abi2"));
    }

    /** 匹配当前设备的cpu型号 */
    private static int matchABI(String abiString) {
        if (TextUtils.isEmpty(abiString)) {
            return 0;
        }
        if ("armeabi".equals(abiString)) {
            return 1;
        } else if ("armeabi-v7a".equals(abiString)) {
            return 2;
        } else if ("x86".equals(abiString)) {
            return 4;
        } else if ("mips".equals(abiString)) {
            return 8;
        }
        return 0;
    }

    /** 获取CPU核心数 */
    public static int getCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    /** 获取Rom版本 */
    public static String getRomversion() {
        String rom = "";
        try {
            String modversion = getSystemProperty("ro.modversion");
            String displayId = getSystemProperty("ro.build.display.id");
            if (modversion != null && !modversion.equals("")) {
                rom = modversion;
            }
            if (displayId != null && !displayId.equals("")) {
                rom = displayId;
            }
        } catch (Exception ignored) {
        }
        return rom;
    }

    /** 获取系统配置参数 */
    public static String getSystemProperty(String key) {
        String pValue = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method m = c.getMethod("get", String.class);
            pValue = m.invoke(null, key).toString();
        } catch (Exception ignored) {
        }
        return pValue;
    }

    /** 获取系统中的Library包 */
    public static List<String> getSystemLibs(Context context) {
        PackageManager pm = context.getPackageManager();
        String[] libNames = pm.getSystemSharedLibraryNames();
        List<String> listLibNames = Arrays.asList(libNames);
        return listLibNames;
    }

    /** 获取手机内部空间大小，单位为byte */
    @SuppressWarnings("deprecation")
    public static long getTotalInternalSpace() {
        long totalSpace = -1L;
        try {
            String path = Environment.getDataDirectory().getPath();
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();// 获取该区域可用的文件系统数
            totalSpace = totalBlocks * blockSize;
        } catch (Exception ignored) {
        }
        return totalSpace;
    }

    /** 获取手机内部可用空间大小，单位为byte */
    @SuppressWarnings("deprecation")
    public static long getAvailableInternalMemorySize() {
        long availableSpace = -1l;
        try {
            String path = Environment.getDataDirectory().getPath();// 获取 Android
            // 数据目录
            StatFs stat = new StatFs(path);// 一个模拟linux的df命令的一个类,获得SD卡和手机内存的使用情况
            long blockSize = stat.getBlockSize();// 返回 Int ，大小，以字节为单位，一个文件系统
            long availableBlocks = stat.getAvailableBlocks();// 返回 Int
            // ，获取当前可用的存储空间
            availableSpace = availableBlocks * blockSize;
        } catch (Exception ignored) {
        }
        return availableSpace;
    }

    /** 获取单个应用最大分配内存，单位为byte */
    public static long getOneAppMaxMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getMemoryClass() * 1024 * 1024;
    }

    /** 获取指定包名应用占用的内存，单位为byte */
    public static long getUsedMemory(Context context) {
        String packageName = context.getPackageName();
        long size = 0;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runapps = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runapp : runapps) { //
            //遍历运行中的程序
            if (packageName.equals(runapp.processName)) {//
                //得到程序进程名，进程名一般就是包名，但有些程序的进程名并不对应一个包名
                // 返回指定PID程序的内存信息，可以传递多个PID，返回的也是数组型的信息
                Debug.MemoryInfo[] processMemoryInfo =
                        activityManager.getProcessMemoryInfo(new int[]{runapp.pid});
                // 得到内存信息中已使用的内存，单位是K
                size = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
            }
        }
        return size;
    }

    /** 获取手机剩余内存，单位为byte */
    public static long getAvailableMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info.availMem;
    }

    /** 手机是否处于低内存运行 */
    public static boolean isLowMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info.lowMemory;
    }

    /**
     * 检查应用是否拥有某个权限
     *
     * @param context        上下文
     * @param permissionName 权限完整名字
     * @return true，此权限被允许。false,未能获得该权限，即使你在清单文件里声明也有可能被用户拒绝此权限
     */
    public static boolean checkPermission(Context context, String permissionName) {
        return context.checkCallingOrSelfPermission(permissionName) == PackageManager.PERMISSION_GRANTED;
    }
}
