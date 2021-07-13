package com.unlam.vacunartech.models;

public class Event {
    private String env;
    private String type_events;
    private String description;
    private Integer dni;
    private Integer id;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getTypeEvent() {
        return type_events;
    }

    public void setTypeEvent(String type_event) {
        this.type_events = type_event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

