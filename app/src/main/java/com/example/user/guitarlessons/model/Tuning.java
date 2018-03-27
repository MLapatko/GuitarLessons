package com.example.user.guitarlessons.model;

/**
 * Created by user on 27.03.2018.
 */

public class Tuning {
    private String name;
    private String st1;
    private String st2;
    private String st3;
    private String st4;
    private String st5;
    private String st6;

    public String getName() {
        return name;
    }

    public String getSt1() {
        return st1;
    }

    public String getSt2() {
        return st2;
    }

    public String getSt3() {
        return st3;
    }

    public String getSt4() {
        return st4;
    }

    public String getSt5() {
        return st5;
    }

    public String getSt6() {
        return st6;
    }

    @Override
    public String toString() {
        return "Tuning{" +
                "name='" + name + '\'' +
                ", st1='" + st1 + '\'' +
                ", st2='" + st2 + '\'' +
                ", st3='" + st3 + '\'' +
                ", st4='" + st4 + '\'' +
                ", st5='" + st5 + '\'' +
                ", st6='" + st6 + '\'' +
                '}';
    }
}
