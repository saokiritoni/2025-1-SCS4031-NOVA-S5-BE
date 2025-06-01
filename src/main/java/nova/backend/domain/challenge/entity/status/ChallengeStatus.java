package nova.backend.domain.challenge.entity.status;

/**
 * 챌린지 자연스러운 중단 상태 자체
 */
public enum ChallengeStatus {
    IN_PROGRESS, // 아직 완료되지 않음
    COMPLETED,   // 성공 기준 도달
    REWARDED // 리워드 전환 완료
}
