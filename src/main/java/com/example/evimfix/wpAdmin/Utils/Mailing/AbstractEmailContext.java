package com.example.evimfix.wpAdmin.Utils.Mailing;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractEmailContext {
    private String to;
    private String from;
    private String subject;
    private String email;
    private String templateLocation;
    private Map<String, Object> context;

    public AbstractEmailContext() {
        this.context = new HashMap<>();
    }

    public <T> void init(T context) {

    }

    public Object put(String key, Object value) {
        return key == null ? null : this.context.put(key.intern(), value);
    }
}
