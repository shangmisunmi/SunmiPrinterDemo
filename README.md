SunmiPrinterDemo
==========
### 
Please be sure to read the documentation when using Demo and You can find 
developer documentation from https://docs.sunmi.com
###
We strongly recommend developers to use Android Studio to develop.The demo for Android Studio has full functionality, such as printing barcodes, printing qr code, printing text, printing pictures and printing tables.  

## Library
    
    dependencies{
      implementation 'com.sunmi:printerlibrary:1.0.13'
    }
    
Now we replaced AIDL integration with Sunmi Printer Interface Library.  
It is suitable for any Sunmi device.  

## AIDL  
If you have to use AIDL to integrate projects， you can get AIDL resource from http://sunmi-ota.oss-cn-hangzhou.aliyuncs.com/DOC/resource/re_cn/AIDL%E6%96%87%E4%BB%B6/aidl.zip
You can see simple use from Demo for Eclipse:https://github.com/shangmisunmi/SunmiPrinterDemo-Eclipse-  

## Instruction

* FunctionActivity——
Home page of each function module  
* AllActivity——
Print all ESC command supported by Sunmi Printer
* QrActivity——Demo call api to print Qr Code
* BarCodeActivity——Demo call api to print bar code
* TextActivity——Demo call api to print text
* TableActivity——Demo call api to print table， 
Bluetooth only plays table graphics
* BitmapActivity——Demo call api to print image,picture
* BufferActivity——Demo call api to print 
a simple small ticket content, and You can get the print result via transaction mode
* BlackLabelActivity——Demo call api to print on black label paper
* LabelActivity——Demo call api to print on "label paper"

## InnerPrinter Ble
Each function module can be used via Bluetooth, 
You can send the esc command through innerprinter ble.  
See it in each module:
    
    if (!BluetoothUtil.isBlueToothPrinter) {
        //Call API  
    } else {
        //ESC cmd via Ble
    }
    

