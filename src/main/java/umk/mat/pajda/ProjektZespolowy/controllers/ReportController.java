package umk.mat.pajda.ProjektZespolowy.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umk.mat.pajda.ProjektZespolowy.DTO.ReportDTO;
import umk.mat.pajda.ProjektZespolowy.services.EmailService;

@RestController("/reports")
@Tag(
        name = "Report Endpoint",
        description = "Controller for sending reports")
public class ReportController {

    private final EmailService emailService;

    public ReportController(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping
    @Operation(
            summary = "POST - send \"Report\"",
            description = "Following endpoint send report")
    public ResponseEntity<String> sendReport(@RequestBody @Valid ReportDTO reportDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Validation failed: " + bindingResult.getAllErrors());
        }
        if(emailService.sendReport(reportDTO)){
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("error with sending report");
        }
    }

}
