package eumsun.backend.service;

import eumsun.backend.domain.Log;
import eumsun.backend.dto.toService.CreateLogDto;
import eumsun.backend.repository.repositoryImpl.LogRepositoryValid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogService {
    private final LogRepositoryValid repository;

    @Transactional
    public void createLog(CreateLogDto dto) {
        Log log = Log.builder()
                .userId(dto.userId())
                .requestUrl(dto.url())
                .requestBody(dto.body())
                .build();
        repository.saveLog(log);
    }
}
