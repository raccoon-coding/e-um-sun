package eumsun.backend.controller;

import eumsun.backend.dto.request.ConnectDto;
import eumsun.backend.dto.request.PurchaseTokenGoogleDto;
import eumsun.backend.service.ParentService;
import eumsun.backend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parent")
@Slf4j
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;
    private final PurchaseService purchaseService;

    @PostMapping("/connection")
    public String connectOffspring(@RequestBody ConnectDto connectDto) {

        return parentService.connectOffspring(connectDto.getEmail());
    }

    @GetMapping("/connect/getString")
    public String getConnectionString() {

        return parentService.getString();
    }

    @PatchMapping("/purchase/google/token")
    public String purchaseTokenGoogle(@RequestBody PurchaseTokenGoogleDto purchaseTokenGoogleDto) {

        return purchaseService.purchaseTokenGoogle(purchaseTokenGoogleDto);
    }
}
