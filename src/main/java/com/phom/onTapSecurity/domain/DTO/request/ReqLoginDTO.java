package com.phom.onTapSecurity.domain.DTO.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqLoginDTO {
    private String username;
    private String password;
}
