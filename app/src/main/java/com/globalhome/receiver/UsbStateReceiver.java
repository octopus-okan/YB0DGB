package com.globalhome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.globalhome.listener.BaseListener;
import com.globalhome.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;


/**
 * author: ztz
 * date: 2019/9/5 18:24
 * description:usb设备，包含u盘，键盘
 */
public class UsbStateReceiver extends BroadcastReceiver {
    private Context mContext;

    public UsbStateReceiver(Context context) {
        mContext = context;
    }

    public void onRegister() {
        if (mContext != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.hardware.usb.action.USB_STATE");
//            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);//mobile in host mode  ,插入u盘在此模式。在一直插着upan时，重启后会收不到该广播，需要usb_state
//            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);//in host mode
//            intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);//手机为从设备
//            intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
            mContext.registerReceiver(this, intentFilter);
        }
    }

    public void onUnregister() {
        if (mContext != null) {
            mContext.unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.hardware.usb.action.USB_STATE")) {
            Utils.log("USB_STATE：" + intent.getExtras().getBoolean("connected"));//不知为啥结果一直返回false

            //在一直插着upan时，重启后会收不到该广播，需要这里做处理
            boolean isUDisk = false;
            UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            Iterator<UsbDevice> iterator = deviceList.values().iterator();
            while (iterator.hasNext()) {
                UsbDevice usbDevice = iterator.next();
                int deviceClass = usbDevice.getDeviceClass();
                if (deviceClass == 0) {
                    UsbInterface usbInterface = usbDevice.getInterface(0);
                    int interfaceClass = usbInterface.getInterfaceClass();
                    if (interfaceClass == 8) {
                        Utils.log("u盘");
                        isUDisk = true;
                    } else {
                        Utils.log("其他：" + interfaceClass);
                    }
                }
            }
            if (listener != null) {
                listener.onUpdate(isUDisk);
            }
        } else if (action.equals(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)) {
            Utils.log("ACTION_USB_ACCESSORY_ATTACHED");
        } else if (action.equals(UsbManager.ACTION_USB_ACCESSORY_DETACHED)) {
            Utils.log("ACTION_USB_ACCESSORY_DETACHED");
        } else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            Utils.log("ACTION_USB_DEVICE_ATTACHED");
            if (listener != null) {
                listener.onUpdate(true);
            }
        } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            Utils.log("ACTION_USB_DEVICE_DETACHED");
            if (listener != null) {
                listener.onUpdate(false);
            }
        }
    }

    BaseListener.OnBoolUpdateListener listener;

    public void setOnUpdateListener(BaseListener.OnBoolUpdateListener listener) {
        this.listener = listener;
    }
}
