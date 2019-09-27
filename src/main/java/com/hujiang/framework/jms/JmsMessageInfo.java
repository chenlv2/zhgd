package com.hujiang.framework.jms;

public class JmsMessageInfo<T> {
    public JmsMessageType getType() {
        return type;
    }

    public void setType(JmsMessageType type) {
        this.type = type;
    }

    private JmsMessageType type;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    private T body;

    /** 项目id */
    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
