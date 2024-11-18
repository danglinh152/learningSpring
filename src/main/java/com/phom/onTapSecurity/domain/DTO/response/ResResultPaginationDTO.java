package com.phom.onTapSecurity.domain.DTO.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResResultPaginationDTO {
    private Meta meta;
    private Object data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private int page;
        private int pageSize;
        private int totalPages;
        private long totalElements;
    }

}
