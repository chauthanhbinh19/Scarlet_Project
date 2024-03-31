package com.example.scarlet.Data;

import java.util.Date;
import java.util.List;

public class Membership {
    private String name;
    private int level;
    private List<String> privileges;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

    public Membership() {
    }

    public Membership(String name, int level, List<String> privileges) {
        this.name = name;
        this.level = level;
        this.privileges = privileges;
    }
}
