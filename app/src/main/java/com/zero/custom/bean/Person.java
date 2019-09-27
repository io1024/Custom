package com.zero.custom.bean;

import com.zero.custom.PinYinUtils;

/**
 * 实体
 */
public class Person {

    private String name;
    private String namePinYin;

    public Person(String name) {
        this.name = name;
        this.namePinYin = PinYinUtils.pinYinRemoveSpace(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePinYin() {
        return namePinYin;
    }

    public void setNamePinYin(String namePinYin) {
        this.namePinYin = namePinYin;
    }
}
