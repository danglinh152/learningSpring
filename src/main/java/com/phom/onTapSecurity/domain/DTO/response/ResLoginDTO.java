package com.phom.onTapSecurity.domain.DTO.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResLoginDTO {

    @JsonProperty("access_token")
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetAccountDTO {
        private UserLoginDTO user;
    }

}
