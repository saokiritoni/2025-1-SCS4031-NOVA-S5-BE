package nova.backend.domain.cafe.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.cafe.entity.CharacterType;

@Schema(description = "카페 등록 요청 Schema")
public class CafeRegistrationSchema {

    @Schema(description = "카페 이름", example = "더블톤")
    public String cafeName;

    @Schema(description = "지점명", example = "건대점")
    public String branchName;

    @Schema(description = "사장 이름", example = "김사장")
    public String ownerName;

    @Schema(description = "사장 전화번호", example = "010-1234-5678")
    public String ownerPhone;

    @Schema(description = "사업자 등록번호", example = "123-45-67890")
    public String businessNumber;

    @Schema(description = "위도", example = "37.49997")
    public Double latitude;

    @Schema(description = "경도", example = "127.0363")
    public Double longitude;

    @Schema(description = "카페 도로명 주소", example = "서울특별시 강남구 테헤란로 123")
    public String roadAddress;

    @Schema(description = "최대 스탬프 수", example = "10")
    public Integer maxStampCount;

    @Schema(description = "캐릭터 타입", example = "BLACK")
    public CharacterType characterType;

}
