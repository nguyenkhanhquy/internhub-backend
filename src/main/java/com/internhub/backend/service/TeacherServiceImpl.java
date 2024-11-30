package com.internhub.backend.service;

import com.internhub.backend.dto.request.teachers.TeacherCreateRequest;
import com.internhub.backend.dto.request.teachers.TeacherUpdateRequest;
import com.internhub.backend.entity.teacher.Teacher;
import com.internhub.backend.exception.CustomException;
import com.internhub.backend.exception.EnumException;
import com.internhub.backend.repository.TeacherRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public void addTeacher(TeacherCreateRequest request) {
        Teacher teacher = Teacher.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        teacherRepository.save(teacher);
    }

    @Override
    public void updateTeacher(String id, TeacherUpdateRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.TEACHER_NOT_FOUND));

        teacher.setName(request.getName());
        teacher.setEmail(request.getEmail());

        teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(String id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.TEACHER_NOT_FOUND));

        teacherRepository.delete(teacher);
    }

    @Override
    public Teacher getTeacherById(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(EnumException.TEACHER_NOT_FOUND));
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public void importTeachersFromFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("text/csv") && !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
            throw new CustomException(EnumException.FILE_TYPE_INVALID);
        }

        try {
            List<Teacher> teachers = new ArrayList<>();

            if (contentType.equals("text/csv")) {
                // Xử lý file CSV
                try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                    List<String[]> rows = csvReader.readAll();
                    for (String[] row : rows) {
                        if (row[0].equals("Name") && row[1].equals("Email")) continue; // Bỏ qua tiêu đề
                        teachers.add(new Teacher(null, row[0], row[1]));
                    }
                }
            } else {
                // Xử lý file Excel
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Bỏ qua tiêu đề
                    String name = row.getCell(0).getStringCellValue();
                    String email = row.getCell(1).getStringCellValue();
                    teachers.add(new Teacher(null, name, email));
                }
                workbook.close();
            }

            teacherRepository.saveAll(teachers);
        } catch (Exception e) {
            throw new CustomException(EnumException.IMPORT_FILE_ERROR);
        }
    }
}
