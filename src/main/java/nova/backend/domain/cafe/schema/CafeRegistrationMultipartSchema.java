package nova.backend.domain.cafe.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import nova.backend.domain.cafe.entity.CharacterType;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(name = "CafeRegistrationMultipartSchema", description = "카페 등록 multipart 요청")
public class CafeRegistrationMultipartSchema {

    @Schema(description = "카페 이름", example = "더블톤")
    private String cafeName;

    @Schema(description = "지점명", example = "건대점")
    private String branchName;

    @Schema(description = "사장 이름", example = "김사장")
    private String ownerName;

    @Schema(description = "사장 전화번호", example = "010-1234-5678")
    private String ownerPhone;

    @Schema(description = "사업자 등록번호", example = "123-45-67890")
    private String businessNumber;

    @Schema(description = "위도", example = "37.49997")
    private Double latitude;

    @Schema(description = "경도", example = "127.0363")
    private Double longitude;

    @Schema(description = "최대 스탬프 수", example = "10")
    private Integer maxStampCount;

    @Schema(description = "캐릭터 타입", example = "BLACK")
    private CharacterType characterType;

    @Schema(description = "리워드 설명", example = "아메리카노 무료")
    private String rewardDescription;

    @Schema(description = "사업자등록증 PDF 파일", type = "string", format = "binary")
    private MultipartFile businessRegistrationPdf;
}
