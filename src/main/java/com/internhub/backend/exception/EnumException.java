package com.internhub.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumException {

    // 400 : Bad request — dữ liệu gửi lên không hợp lệ
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Dữ liệu yêu cầu không hợp lệ"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "Người dùng đã tồn tại"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "Email đăng nhập đã tồn tại"),
    COMPANY_EXISTED(HttpStatus.BAD_REQUEST, "Công ty đã tồn tại"),
    INVALID_EMAIL_DOMAIN(HttpStatus.BAD_REQUEST, "Địa chỉ email không thuộc trường ĐH Sư phạm Kỹ thuật TP.HCM"),
    INVALID_MAJOR_CODE(HttpStatus.BAD_REQUEST, "Mã ngành không hợp lệ"),
    EMAIL_AND_STUDENT_ID_MISMATCH(HttpStatus.BAD_REQUEST, "Mã số sinh viên không khớp với email"),

    // 401 : Unauthorized — user chưa được xác thực
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Chưa được xác thực"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token không hợp lệ"),
    INVALID_OTP(HttpStatus.UNAUTHORIZED, "OTP không hợp lệ hoặc OTP đã hết hạn"),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "Thông tin đăng nhập không chính xác"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Mật khẩu hiện tại không đúng"),

    // 403: Forbidden — user không có quyền
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "Không có quyền truy cập"),
    ADMIN_CANNOT_BE_DELETED(HttpStatus.FORBIDDEN, "Không có quyền xóa tài khoản quản trị"),

    // 404: Not found — không tồn tại resource
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ"),
    JOB_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy bài đăng việc làm"),

    // 500: Internal Server Error — có lỗi trong hệ thống
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Ngoại lệ chưa phân loại: "),
    JWT_SIGNING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể ký JWT Token"),
    ERROR_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "Tải lên tệp không thành công"),
    ;

    private final HttpStatus statusCode;
    private final String message;

    EnumException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
