package com.spring.security.controller;

import com.spring.security.model.AuthenticationRequest;
import com.spring.security.model.AuthenticationResponse;
import com.spring.security.service.CustomUserDetailsService;
import com.spring.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String sayHello(){
        return "hello";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

      // By default Spring uses UsernamePasswordAuthenticationToken so we need to generate one
      try{
          authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
          );
      } catch(BadCredentialsException ex){
          throw new Exception("Incorrect username or password");
      }

      UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

      final String jwt = jwtUtil.generateToken(userDetails);

      return ResponseEntity.ok().body(new AuthenticationResponse(jwt));

    }


}
