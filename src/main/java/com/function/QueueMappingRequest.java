package com.function;

public class QueueMappingRequest {
    private String event;

    private String queue;

    // Constructors, getters, and setters
    public QueueMappingRequest() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

}
