package org.example.biz.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.api.demo.DemoRequest;
import org.example.api.demo.DemoResult;
import org.example.biz.service.DemoService;
import org.example.dal.dao.intf.DemoDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoServiceImpl implements DemoService {

    private final DemoDao demoDao;

    @Override
    public DemoResult demo(DemoRequest request) {
        return DemoResult.builder().id(request.getId()).build();
    }

    @Override
    public Boolean test() {
        demoDao.test();
        return true;
    }
}
