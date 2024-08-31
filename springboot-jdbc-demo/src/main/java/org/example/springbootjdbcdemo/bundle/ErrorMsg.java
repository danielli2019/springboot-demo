package org.example.springbootjdbcdemo.bundle;

import org.example.ErrorMsgUtil;

import java.util.ResourceBundle;

public class ErrorMsg {
    public static void main(String[] args) {
        // 创建自定义的Control实例
//        ResourceBundle.Control control = new InheritingResourceBundleControl();
        ResourceBundle parentResourceBundle = ResourceBundle.getBundle("messageCommon");
        ResourceBundle childResourceBundle = ResourceBundle.getBundle("messageChild");
        ResourceBundle resourceBundle = new InheritingResourceBundle(parentResourceBundle, childResourceBundle);

        System.out.println(resourceBundle.getString("1001"));
        System.out.println(resourceBundle.getString("1002"));
        System.out.println(resourceBundle.getString("3001"));

        System.out.println(ErrorMsgUtil.getMessage());
    }
}
