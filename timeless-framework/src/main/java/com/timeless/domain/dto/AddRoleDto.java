package com.timeless.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-17 18:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddRoleDto {

    private String roleName;

    private String roleKey;

    private Integer roleSort;

    private String status;

    private List<Long> menuIds;

    private String remark;

}
