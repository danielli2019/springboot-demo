package org.example.springbootjdbcdemo.bundle;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

public class InheritingResourceBundle extends ResourceBundle {
    private final ResourceBundle parent;
    private final ResourceBundle child;
    private final Set<String> keys;

    public InheritingResourceBundle(ResourceBundle parent, ResourceBundle child) {
        this.parent = parent;
        this.child = child;
        this.keys = new HashSet<>();
        initializeKeys();
    }

    private void initializeKeys() {
        // 添加父资源的所有键
        Enumeration<String> parentKeys = parent.getKeys();
        while (parentKeys.hasMoreElements()) {
            keys.add(parentKeys.nextElement());
        }

        // 添加子资源的所有键，跳过那些在父资源中已经存在的键
        Enumeration<String> childKeys = child.getKeys();
        while (childKeys.hasMoreElements()) {
            String key = childKeys.nextElement();
            if (!keys.contains(key)) {
                keys.add(key);
            }
        }
    }

    @Override
    protected Object handleGetObject(String key) {
        // 优先从子资源获取对象，如果不存在则从父资源获取
        try {
            return child.getObject(key);
        } catch (MissingResourceException e) {
            return parent.getObject(key);
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(keys);
    }
}
