package com.padaks.todaktodak.untact.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Parser {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public Optional<String> parseId(String sid) {
        return Optional.ofNullable(sid);
    }
}
