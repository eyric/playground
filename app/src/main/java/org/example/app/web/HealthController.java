package org.example.app.web;

import org.example.api.base.HttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController extends BaseController {

    @GetMapping("/health/check")
    public HttpResponse<Boolean> check() {
        return addResultForResponse(true);
    }
}
