package nova.backend.domain.cafe.dto.response;

import nova.backend.domain.cafe.entity.Cafe;

/**
 * 중간 처리용 DTO (Query DSL / JPQL DTO projection)
 * @param cafe
 * @param downloadCount
 */
public record CafeWithDownloadCountDTO(
        Cafe cafe,
        Long downloadCount
) {}
