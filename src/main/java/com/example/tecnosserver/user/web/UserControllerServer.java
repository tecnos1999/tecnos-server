package com.example.tecnosserver.user.web;
import com.example.tecnosserver.system.jwt.JWTTokenProvider;
import com.example.tecnosserver.user.dto.LoginRequest;
import com.example.tecnosserver.user.dto.LoginResponse;
import com.example.tecnosserver.user.dto.RegisterResponse;
import com.example.tecnosserver.user.dto.UserDTO;
import com.example.tecnosserver.user.model.User;
import com.example.tecnosserver.user.service.UserCommandService;
import com.example.tecnosserver.user.service.UserQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import static com.example.tecnosserver.utils.Utils.JWT_TOKEN_HEADER;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/")
@AllArgsConstructor
public class UserControllerServer {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {
        authenticate(user.email(), user.password());
        User loginUser = userQueryService.findByEmail(user.email()).get();
        User userPrincipal = getUser(loginUser);

        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        LoginResponse loginResponse = new LoginResponse(
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                userPrincipal.getFirstName(),
                userPrincipal.getLastName(),
                userPrincipal.getPhoneNumber(),
                userPrincipal.getEmail(),
                userPrincipal.getProfileUrl() ,
                userPrincipal.getUserRole()
        );
        return new ResponseEntity<>(loginResponse, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserDTO userDTO) {
        this.userCommandService.addUser(userDTO);
        User userPrincipal = userQueryService.findByEmail(userDTO.email()).get();
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        RegisterResponse registerResponse = new RegisterResponse(
                jwtHeader.getFirst(JWT_TOKEN_HEADER),
                userPrincipal.getFirstName(),
                userPrincipal.getLastName(),
                userPrincipal.getPhoneNumber(),
                userPrincipal.getEmail(),
                userPrincipal.getProfileUrl(),
                userPrincipal.getUserRole()
        );
        authenticate(userDTO.email(), userDTO.password());
        return new ResponseEntity<>(registerResponse, jwtHeader, HttpStatus.OK);
    }


    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }


    private HttpHeaders getJwtHeader(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJWTToken(user));
        return headers;
    }


    private User getUser(User loginUser) {
        User userPrincipal = new User();
        userPrincipal.setEmail(loginUser.getEmail());
        userPrincipal.setPassword(loginUser.getPassword());
        userPrincipal.setUserRole(loginUser.getUserRole());
        userPrincipal.setFirstName(loginUser.getFirstName());
        userPrincipal.setLastName(loginUser.getLastName());
        userPrincipal.setPhoneNumber(loginUser.getPhoneNumber());
        userPrincipal.setRegisteredAt(loginUser.getRegisteredAt());
        userPrincipal.setProfileUrl(loginUser.getProfileUrl());
        userPrincipal.setCreatedAt(loginUser.getCreatedAt());
        return userPrincipal;
    }
}
