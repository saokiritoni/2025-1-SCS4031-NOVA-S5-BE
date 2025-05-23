package nova.backend.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    /**
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "ENUM 입력값이 올바르지 않습니다."),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 접근 권한이 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "액세스 토큰의 값이 올바르지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 형식이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 값이 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요."),
    NOT_MATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "일치하지 않는 리프레시 토큰입니다."),
    /**
     * 403 Forbidden
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),


    /**
     * 404 Not Found
     */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "엔티티를 찾을 수 없습니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    /**
     * 409 Conflict
     */
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),

    /**
     * 413 Payload Too Large
     */
    FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "이미지 최대 크기를 초과하였습니다."),


    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하였습니다."),

    /**
     * User Error
     */
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패하였습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "다른 소셜 계정으로 이미 가입된 사용자입니다."),

    /**
     * Oauth Error
     */
    OAUTH_TOKEN_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 토큰 요청에 실패하였습니다."),
    OAUTH_USER_RESOURCE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 사용자 정보 조회에 실패하였습니다."),
    JSON_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱에 실패하였습니다."),

    /**
     * StampBook Error
     */
    STAMPBOOK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 해당 카페에 대한 스탬프북이 존재합니다."),
    STAMPBOOK_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "스탬프북이 아직 완료되지 않았습니다."),
    REWARD_ALREADY_CLAIMED(HttpStatus.CONFLICT, "이미 리워드를 전환했습니다."),
    REWARD_NOT_CLAIMED(HttpStatus.BAD_REQUEST, "리워드가 아직 전환되지 않았습니다."),
    REWARD_ALREADY_USED(HttpStatus.CONFLICT, "이미 리워드를 사용했습니다."),
    NOT_ENOUGH_REWARDS(HttpStatus.BAD_REQUEST, "사용 가능한 리워드가 부족합니다."),
    ALREADY_USED_STAMPBOOK(HttpStatus.BAD_REQUEST, "이미 사용된 스탬프북은 마이페이지에 추가할 수 없습니다."),
    NOT_CUSTOMED(HttpStatus.BAD_REQUEST,"스탬프북이 커스텀되지 않았습니다."),

    /**
     * Cafe Error
     */
    EXPOSED_STAMPBOOK_NOT_FOUND(HttpStatus.BAD_REQUEST," 카페에서 노출 설정된 스탬프북이 없습니다."),

    /**
    Stamp Error
     */
    CAFE_NOT_SELECTED(HttpStatus.BAD_REQUEST, "카페가 선택되지 않았습니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;





}
