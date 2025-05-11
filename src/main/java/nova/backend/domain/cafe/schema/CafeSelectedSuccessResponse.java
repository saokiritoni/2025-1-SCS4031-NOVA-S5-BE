package nova.backend.domain.cafe.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(name = "CafeSelectedSuccessResponse", description = "현재 선택된 카페 상세 정보 반환 DTO")
public class CafeSelectedSuccessResponse {

    @Schema(description = "성공 여부", example = "true")
    public boolean success;

    @Schema(description = "응답 데이터")
    public Cafe data;

    public static class Cafe {
        @Schema(description = "카페 ID", example = "1")
        public Long cafeId;

        @Schema(description = "카페 이름", example = "유등이커피")
        public String cafeName;

        @Schema(description = "위도", example = "37.123456")
        public Double latitude;

        @Schema(description = "경도", example = "127.123456")
        public Double longitude;

        @Schema(description = "전화번호", example = "02-1234-5678")
        public String cafePhone;

        @Schema(description = "최대 스탬프 개수", example = "10")
        public Integer maxStampCount;

        @Schema(description = "현재 영업중 여부", example = "true")
        public boolean isOpenNow;

        @Schema(description = "카페 등록 상태", example = "APPROVED")
        public CafeRegistrationStatus registrationStatus;

        @Schema(description = "정기 영업 시간")
        public List<OpenHour> openHours;

        @Schema(description = "특별 영업일")
        public List<SpecialDay> specialDays;
    }

    public static class OpenHour {
        @Schema(description = "요일", example = "MONDAY")
        public DayOfWeek dayOfWeek;

        @Schema(description = "해당 요일에 영업하는지 여부", example = "true")
        public boolean isOpen;

        @Schema(description = "오픈 시간", example = "08:00")
        public LocalTime openTime;

        @Schema(description = "마감 시간", example = "20:00")
        public LocalTime closeTime;

        @Schema(description = "라스트오더 시간", example = "19:30")
        public LocalTime lastOrder;
    }

    public static class SpecialDay {
        @Schema(description = "특별 영업일 날짜", example = "2025-05-15")
        public LocalDate specialDate;

        @Schema(description = "영업 여부", example = "false")
        public boolean isOpen;

        @Schema(description = "오픈 시간", example = "10:00")
        public LocalTime openTime;

        @Schema(description = "마감 시간", example = "18:00")
        public LocalTime closeTime;

        @Schema(description = "라스트오더 시간", example = "17:30")
        public LocalTime lastOrder;

        @Schema(description = "비고 메모", example = "근로자의 날 단축 운영")
        public String note;
    }
}
