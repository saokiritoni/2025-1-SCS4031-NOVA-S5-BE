package nova.backend.domain.cafe.schema;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카페 등록 multipart 요청 스키마")
public class CafeRegistrationMultipartSchema {

    @Schema(description = "카페 이름", example = "소은카페")
    public String cafeName;

    @Schema(description = "지점명", example = "건대점")
    public String branchName;

    @Schema(description = "사장 이름", example = "이소은")
    public String ownerName;

    @Schema(description = "사장 연락처", example = "01012345678")
    public String ownerPhone;

    @Schema(description = "카페 연락처", example = "01012345678")
    public String cafePhone;

    @Schema(description = "사업자 등록 번호", example = "123-45-67890")
    public String businessNumber;

    @Schema(description = "위도", example = "37.541")
    public Double latitude;

    @Schema(description = "경도", example = "127.078")
    public Double longitude;

    @Schema(description = "카페 도로명 주소", example = "서울특별시 중구 필동로 1길 30")
    public String roadAddress;


    @Schema(description = "최대 스탬프 수", example = "10")
    public Integer maxStampCount;

    @Schema(description = "캐릭터 타입 (예: ORANGE, BLACK ...)", example = "RABBIT")
    public String characterType;

    @Schema(description = "스탬프북 디자인 JSON, 없으면 기본 스탬프북으로 저장", example = "{\"bgColor\":\"#fff\", \"stampShape\":\"circle\"}")
    public String stampBookDesignJson;

    @Schema(description = "카페 대표 사진 URL", example = "https://example.com/images/cafe.jpg")
    public String cafeUrl;

    @Schema(description = "사업자 등록증 PDF 파일")
    public String businessRegistrationPdf;
}