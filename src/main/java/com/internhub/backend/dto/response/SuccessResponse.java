package com.internhub.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccessResponse <T> {

    @Builder.Default
    private boolean success = true;

    @Builder.Default
    private int statusCode = 200;

    private String message;

    private T result;
}
