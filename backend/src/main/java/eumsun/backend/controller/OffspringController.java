package eumsun.backend.controller;

import eumsun.backend.dto.request.ConnectionRandomString;
import eumsun.backend.service.OffspringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offspring")
@RequiredArgsConstructor
public class OffspringController {

    private final OffspringService offspringService;

    @PatchMapping("/connection")
    public String connectParent(@RequestBody ConnectionRandomString string) {

        return offspringService.connectParent(string.getRandomString());
    }
}
