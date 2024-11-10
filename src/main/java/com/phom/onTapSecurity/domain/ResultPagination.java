package com.phom.onTapSecurity.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultPagination {
    private Meta meta;
    private Object data;
}
