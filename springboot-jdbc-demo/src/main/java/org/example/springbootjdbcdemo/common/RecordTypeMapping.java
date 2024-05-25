package org.example.springbootjdbcdemo.common;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeMapping {
    Map<String, Map<String, String>> recordTypeEventMapping;

    Map<String, Map<String, String>> recordTypeRepositoryMapping;

    public void init() {
        initEventMapping();
        initRepositoryMapping();
    }

    private void initEventMapping() {
        recordTypeEventMapping = new HashMap<>();
        Map<String, String> actionMapping = new HashMap<>();
        actionMapping.put("ADD", "CreateBookRequest");
        recordTypeEventMapping.put("Book", actionMapping);
    }

    private void initRepositoryMapping() {

    }
}
