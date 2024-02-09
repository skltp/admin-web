package se.skltp.admin.modules.jms.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class PropertiesController {
    private static Logger LOG = LoggerFactory.getLogger(PropertiesController.class);

    @Autowired
    BuildProperties buildProperties;

    @GetMapping("/api/appVersion")
    public ResponseEntity<String> getVariable() {
        LOG.info("Request received for app version");
        String variable = buildProperties.getVersion();
        return ResponseEntity.ok(variable);
    }
}
