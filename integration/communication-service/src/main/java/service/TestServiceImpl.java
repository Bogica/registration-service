package service;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService{

    @Override
    public String getServiceName() {
        return "helloooo";
    }
}
