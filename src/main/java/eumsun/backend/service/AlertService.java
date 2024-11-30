package eumsun.backend.service;

import eumsun.backend.domain.Alert;
import eumsun.backend.dto.toService.CreateAlertDto;
import eumsun.backend.repository.repositoryImpl.AlertRepositoryValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepositoryValid alertRepository;

    @Transactional
    public void createAlert(CreateAlertDto dto) {
        Alert alert = Alert.builder()
                .offspringId(dto.offspringId())
                .parentId(dto.parentId())
                .content(dto.message())
                .build();

        alertRepository.createAlert(alert);
    }
}
