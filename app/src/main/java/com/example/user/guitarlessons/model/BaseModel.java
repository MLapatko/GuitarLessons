package com.example.user.guitarlessons.model;

import com.example.user.guitarlessons.ModelType;

import weborb.service.MapToProperty;

/**
 * Created by user on 25.02.2018.
 */

public abstract class BaseModel implements ModelType{
    @MapToProperty(property = "objectId")
    String objectId;

    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
