package com.internhub.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EnumException {
    // 400 : Bad request — dữ liệu gửi lên không hợp lệ
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Dữ liệu yêu cầu không hợp lệ"),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "Invalid file name"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "Người dùng đã tồn tại"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "Email đã tồn tại"),

    // 401 : Unauthorized — user chưa được xác thực
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Bạn chưa được xác thực"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token không hợp lệ"),
    INVALID_OTP(HttpStatus.UNAUTHORIZED, "OTP không hợp lệ hoặc OTP đã hết hạn"),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không hợp lệ"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Mật khẩu hiện tại không đúng"),

    // 403: Forbidden — user không có quyền
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập"),
    ADMIN_CANNOT_BE_DELETED(HttpStatus.FORBIDDEN, "Không thể xóa người dùng có vai trò ADMIN"),

    // 404: Not found — không tồn tại resource
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy trang"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ"),

    // 500: Internal Server Error — có lỗi trong hệ thống
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Ngoại lệ chưa phân loại: "),
    JWT_SIGNING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể ký JWT Token"),
    ERROR_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "Không tải được tệp lên"),
    ;

    private final HttpStatus statusCode;
    private final String message;

    EnumException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
