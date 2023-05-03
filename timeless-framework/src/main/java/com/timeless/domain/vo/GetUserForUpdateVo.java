package com.timeless.domain.vo;

import com.timeless.domain.entity.Role;
import com.timeless.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author timeless
 * @create 2022-12-18 1:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserForUpdateVo {

    private List<Long> roleIds;

    private List<Role> roles;

    private User user;

}
