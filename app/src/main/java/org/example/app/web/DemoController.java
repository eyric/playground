package org.example.app.web;

import lombok.RequiredArgsConstructor;
import org.example.api.base.HttpResponse;
import org.example.api.demo.DemoRequest;
import org.example.api.demo.DemoResult;
import org.example.biz.service.DemoService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController extends BaseController {

    private final DemoService demoService;

    @PostMapping("/demo")
    public HttpResponse<DemoResult> demo(@RequestBody @Validated DemoRequest request, BindingResult bindingResult) {
        return act(() -> demoService.demo(request), bindingResult);
    }

    @PostMapping("/test")
    public HttpResponse<Boolean> test() {
        return act(demoService::test);
    }
}
