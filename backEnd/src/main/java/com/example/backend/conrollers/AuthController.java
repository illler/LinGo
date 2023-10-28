package com.example.backend.conrollers;

import com.example.backend.DTO.AuthDTO;
import com.example.backend.DTO.UserDTO;
import com.example.backend.auth.AuthenticationResponse;
import com.example.backend.auth.AuthenticationRequest;
import com.example.backend.auth.RegisterRequest;
import com.example.backend.error.ErrorResponse;
import com.example.backend.model.User;
import com.example.backend.services.impl.EmailServiceImpl;
import com.example.backend.services.props.AuthenticationService;
import com.example.backend.services.props.MyUserDetailsService;
import com.example.backend.util.UserValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "AuthController", description = "Это то что нужно трогать)")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserValidator userValidator;

    private final MyUserDetailsService myUserDetailsService;
    private final EmailServiceImpl emailService;


    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request,
                                                           BindingResult bindingResult){
        userValidator.validate(request.getEmail(), bindingResult);
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Пользователь уже создан"));
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/recover-password")
    public String sendNewPassword(@RequestParam String email){
        User user = myUserDetailsService.findByEmail(email);
        emailService.sendPasswordRecoveryMail(user);
        return "All fine";
    }

    @PostMapping("/checkTmpPassword")
    public Boolean checkTmpPassword(@RequestBody AuthDTO authDTO){
        return authenticationService.checkTemplePassword(authDTO);
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@RequestBody AuthDTO authDTO){
        authenticationService.updatePassword(authDTO);
        return "Пароль обновлен";
    }
}
