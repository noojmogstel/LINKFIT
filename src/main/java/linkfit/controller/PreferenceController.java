package linkfit.controller;

import java.util.List;
import linkfit.dto.PreferenceRequest;
import linkfit.dto.PreferenceResponse;
import linkfit.service.PreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping
    public ResponseEntity<Void> registerPreference(
        @RequestHeader("Authorization") String authorization,
        @RequestBody PreferenceRequest request) {

        preferenceService.registerPreference(authorization, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PreferenceResponse>> getAllPreference(
        @RequestHeader("Authorization") String authorization) {
        List<PreferenceResponse> preferences = preferenceService.getAllPreference(authorization);
        return ResponseEntity.status(HttpStatus.OK).body(preferences);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePreference(
        @RequestHeader("Authorization") String authorization) {
        preferenceService.deletePreference(authorization);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
