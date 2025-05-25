package com.internhub.backend.dto.cv;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumVitaeDTO {

    private String id;

    private String title;

    private String filePath;

    private String createdDate;

    private String studentId;
}
