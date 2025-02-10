# SunmiPrinterDemo

> [!IMPORTANT]
> Please be sure to read the documentation when using Demo  
> and You can find developer documentation from <https://developer.sunmi.com>

We strongly recommend developers to use **Android Studio** to develop.  
The demo for **Android Studio** has full functionality, such as printing **bar codes**, printing **QR code**, printing **text**, printing **pictures** and printing **tables**.

---

## Library

```Groovy
dependencies {
    implementation 'com.sunmi:printerlibrary:1.0.23'
}
```

Now we replaced AIDL integration with Sunmi Printer Interface Library.  
It is suitable for any Sunmi device.

## AIDL  

If you have to use AIDL to integrate projects, you can get AIDL resource from [**here**](http://sunmi-ota.oss-cn-hangzhou.aliyuncs.com/DOC/resource/re_cn/AIDL%E6%96%87%E4%BB%B6/aidl.zip).  
You can see simple use from Demo for Eclipse: [**shangmisunmi/SunmiPrinterDemo-Eclipse-**](https://github.com/shangmisunmi/SunmiPrinterDemo-Eclipse-)

## Instruction

- **Function**Activity
  - **Home page** of each function module  
- **All**Activity
  - Print all **ESC command** supported by Sunmi Printer
- **Qr**Activity
  - Demo call api to print **QR code**
- **BarCode**Activity
  - Demo call api to print **bar code**
- **Text**Activity
  - Demo call api to print **text**
- **Table**Activity
  - Demo call api to print **table**,  
    bluetooth only plays table graphics
- **Bitmap**Activity
  - Demo call api to print **picture**
- **Buffer**Activity
  - Demo call api to print a **simple small ticket content**,  
    and You can get the print result via transaction mode
- **BlackLabel**Activity
  - Demo call api to print on **black label paper**
- **Label**Activity
  - Demo call api to print on "**label paper**"

## InnerPrinter BLE

Each function module can be used via Bluetooth,  
You can send the esc command through innerprinter BLE.

See it in each module:
```Java
if (!BluetoothUtil.isBlueToothPrinter) {
    // Call API  
} else {
    // ESC cmd via Ble
}
```
