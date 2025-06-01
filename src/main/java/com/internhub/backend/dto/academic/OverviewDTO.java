package com.internhub.backend.dto.academic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverviewDTO {

    private long totalInternStudents;
    private long maxExpectedAcceptances;
    private long acceptedStudents;
}
