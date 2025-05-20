package nova.backend.domain.cafe.dto.response;

import nova.backend.domain.cafe.entity.Cafe;

public record CafeWithDownloadCountDTO(
        Cafe cafe,
        Long downloadCount
) {}
