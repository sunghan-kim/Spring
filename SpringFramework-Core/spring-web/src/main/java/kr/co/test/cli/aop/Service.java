package kr.co.test.cli.aop;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Service {
    public void log() {
        log.error(">>> aop log");
    }
}
