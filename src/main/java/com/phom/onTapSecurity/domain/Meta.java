package com.phom.onTapSecurity.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
