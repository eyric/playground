package org.example.biz.service;

import org.example.api.demo.DemoRequest;
import org.example.api.demo.DemoResult;

public interface DemoService {
    DemoResult demo(DemoRequest request);

    Boolean test();
}
