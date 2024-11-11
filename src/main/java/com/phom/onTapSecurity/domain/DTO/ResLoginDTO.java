package com.phom.onTapSecurity.domain.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResLoginDTO {
    private String token;
    private UserLoginDTO user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLoginDTO {
        private long id;
        private String email;
        private String firstName;
        private String lastName;
    }

}
