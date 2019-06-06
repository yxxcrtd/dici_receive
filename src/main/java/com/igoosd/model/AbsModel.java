package com.igoosd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 2017/8/24.
 * 通用model
 */
@Data
@AllArgsConstructor
public abstract class AbsModel {

    /**
     * bean的唯一标志 uuid
     */
    private String key;

}
