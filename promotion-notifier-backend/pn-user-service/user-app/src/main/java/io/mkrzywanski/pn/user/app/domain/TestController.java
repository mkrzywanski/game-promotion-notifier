package io.mkrzywanski.pn.user.app.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestController {
    @GetMapping("/get")
    ResponseEntity<String> get() {
        return ResponseEntity.ok("ok");
    }
}
