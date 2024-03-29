package com.example.FQW.models.enums;

import lombok.Getter;

public enum RoomType {

    RESIDENTIAL(1.5f, 2f, 2.5f),
    COMMERCIAL(1f, 1.5f, 2f);

    @Getter
    private final float regular, general, afterRepair;

    RoomType(float regular, float general, float afterRepair) {
        this.regular = regular;
        this.general = general;
        this.afterRepair = afterRepair;
    }
}
