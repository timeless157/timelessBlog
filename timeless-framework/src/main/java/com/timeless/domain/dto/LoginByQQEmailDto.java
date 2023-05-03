package com.timeless.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author timeless
 * @create 2023-04-22 1:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginByQQEmailDto {

    private String email;

    private String code;

}
