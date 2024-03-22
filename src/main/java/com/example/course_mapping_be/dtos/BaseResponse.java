package com.example.course_mapping_be.dtos;

import com.example.course_mapping_be.constraints.Constants;
import com.example.course_mapping_be.models.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private String message;
    private Integer status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer page;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer size;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public BaseResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }


    public void success() {
        this.message = Constants.ResponseMessage.SUCCESS;
        this.status = HttpStatus.OK.value();
    }

    public void updatePagination(QueryParams params, Long total) {
        this.setPage(params.getPage());
        this.setSize(params.getSize());
        this.setTotal(total);
    }


}
