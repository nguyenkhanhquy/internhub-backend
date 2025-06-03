package com.internhub.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumException {

    // 400 : Bad request — dữ liệu gửi lên không hợp lệ
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Dữ liệu yêu cầu không hợp lệ"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "Người dùng đã tồn tại"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "Email đăng nhập đã tồn tại"),
    COMPANY_EXISTED(HttpStatus.BAD_REQUEST, "Công ty đã đã được đăng ký trước đó"),
    INVALID_EMAIL_DOMAIN(HttpStatus.BAD_REQUEST, "Email không thuộc trường ĐH Sư phạm Kỹ thuật TP.HCM"),
    INVALID_MAJOR_CODE(HttpStatus.BAD_REQUEST, "Mã ngành trong email không hợp lệ"),
    EMAIL_AND_STUDENT_ID_MISMATCH(HttpStatus.BAD_REQUEST, "Mã số sinh viên không khớp với email"),
    ACCOUNT_ALREADY_ACTIVATED(HttpStatus.BAD_REQUEST, "Tài khoản đã được kích hoạt rồi"),
    FILE_TYPE_INVALID(HttpStatus.BAD_REQUEST, "Loại tệp không hợp lệ"),
    COURSE_CODE_EXISTS(HttpStatus.BAD_REQUEST, "Mã lớp học đã tồn tại"),
    INVALID_COURSE_STATUS(HttpStatus.BAD_REQUEST, "Trạng thái lớp học không hợp lệ"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không chính xác"),
    INVALID_OTP(HttpStatus.BAD_REQUEST, "Mã OTP không hợp lệ"),
    INVALID_LOGIN(HttpStatus.BAD_REQUEST, "Thông tin đăng nhập không chính xác"),

    // 401 : Unauthorized — user chưa được xác thực
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Chưa được xác thực"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token không hợp lệ"),
    INVALID_LOGIN_GOOGLE(HttpStatus.UNAUTHORIZED, "Tài khoản Google chưa được đăng ký"),

    // 403: Forbidden — user không có quyền
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "Không có quyền truy cập"),
    ADMIN_CANNOT_BE_DELETED(HttpStatus.FORBIDDEN, "Không có quyền xóa tài khoản quản trị"),
    USER_NOT_ACTIVATED(HttpStatus.FORBIDDEN, "Tài khoản chưa được kích hoạt"),
    USER_LOCKED(HttpStatus.FORBIDDEN, "Tài khoản bị khóa"),

    // 404: Not found — không tồn tại resource
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ"),
    JOB_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy bài đăng việc làm"),
    TEACHER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy giảng viên"),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy công ty"),
    JOB_APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ ứng tuyển"),
    INTERNSHIP_REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy báo cáo thực tập"),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy lớp học"),
    ACADEMIC_YEAR_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy năm học"),
    ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin ghi danh"),
    CV_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy CV"),

    // 500: Internal Server Error — có lỗi trong hệ thống
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Ngoại lệ chưa phân loại: "),
    JWT_SIGNING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể ký JWT Token"),
    UPLOAD_FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Tải lên tệp không thành công"),
    IMPORT_FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi nhập dữ liệu từ tệp");

    private final HttpStatus statusCode;
    private final String message;

    EnumException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
