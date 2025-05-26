package nova.backend.global.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    /**
     * 200 OK
     */
    OK(HttpStatus.OK, "요청이 성공했습니다."),

    /**
     * 201 Created
     */
    CREATED(HttpStatus.CREATED, "요청이 성공했습니다."),

    /**
     * Challenge
     */
    CHALLENGE_CREATED(HttpStatus.CREATED, "챌린지가 생성되었습니다."),
    CHALLENGE_DETAIL_RETRIEVED(HttpStatus.OK, "챌린지 상세 정보 조회 성공"),
    CHALLENGE_LIST_RETRIEVED(HttpStatus.OK, "챌린지 목록 조회 성공"),
    CHALLENGE_ACCUMULATED(HttpStatus.OK, "챌린지 적립이 완료되었습니다."),
    ALREADY_ACCUMULATED(HttpStatus.OK, "오늘은 이미 적립이 완료되었습니다."),
    PARTICIPATION_CREATED(HttpStatus.OK, "참여 내역이 자동으로 생성되었습니다."),




    ;

    private final HttpStatus httpStatus;
    private final String message;
}
