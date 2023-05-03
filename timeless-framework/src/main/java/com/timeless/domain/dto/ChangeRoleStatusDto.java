package com.timeless.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author timeless
 * @create 2022-12-17 13:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleStatusDto {

    private Long roleId;

    private String status;

}
