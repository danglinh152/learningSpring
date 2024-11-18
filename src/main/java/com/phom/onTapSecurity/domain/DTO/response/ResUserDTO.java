package com.phom.onTapSecurity.domain.DTO.response;


import com.phom.onTapSecurity.domain.Company;
import com.phom.onTapSecurity.domain.User;
import com.phom.onTapSecurity.util.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private ResCompanyDTO company;

//    public ResUserDTO(User user) {
//        this.firstName = user.getFirstName();
//        this.lastName = user.getLastName();
//        this.email = user.getEmail();
//        this.age = user.getAge();
//        this.gender = user.getGender();
//        this.address = user.getAddress();
//        if (user.getCompany() != null) {
//            this.company = new ResCompanyDTO(user.getCompany().getId(), user.getCompany().getName());
//        } else {
//            this.company = null;
//        }
//    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResCompanyDTO {
        private long companyId;
        private String companyName;
    }


//    public User() {
//    }
//
//    public User(Long id, String firstName, String lastName, String email, String password) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.password = password;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
