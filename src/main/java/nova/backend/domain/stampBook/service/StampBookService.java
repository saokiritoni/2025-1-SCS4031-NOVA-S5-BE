package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampBookService {

    private final StampBookRepository stampBookRepository;

    public List<StampBookResponseDTO> getStampBooksForUser(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(userId);
        return stampBooks.stream()
                .map(StampBookResponseDTO::fromEntity)
                .toList();
    }
}

