package org.example.springbootjdbcdemo.bundle;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class InheritingResourceBundleControl extends ResourceBundle.Control {
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
                                    boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        // 调用父类的逻辑来获取基础资源
        ResourceBundle parentBundle = super.newBundle(baseName, locale, format, loader, reload);

        // 构造子资源的名称，通常是在基础资源名称后面添加"Child"或其他标识
        String childBaseName = baseName + "Child";

        // 使用相同的加载逻辑来获取子资源
        ResourceBundle childBundle = super.newBundle(childBaseName, locale, format, loader, reload);

        // 创建一个继承自父资源和子资源的新资源包
        ResourceBundle inheritedBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                try {
                    // 首先尝试在子资源中查找键
                    return childBundle.getObject(key);
                } catch (java.util.MissingResourceException e) {
                    // 如果子资源中没有找到，回退到父资源
                    return parentBundle.getObject(key);
                }
            }

            @Override
            public Enumeration<String> getKeys() {
                return null;
            }
        };

        return inheritedBundle;
    }

}
