package at.technikum.springrestbackend.services;

import at.technikum.springrestbackend.dto.LoginRequestDTO;
import at.technikum.springrestbackend.dto.RegisterDTO;
import at.technikum.springrestbackend.mapper.UserMapper;
import at.technikum.springrestbackend.model.UserModel;
import at.technikum.springrestbackend.repository.UserRepository;
import at.technikum.springrestbackend.security.JwtUtil;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthenticationServices {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserServices userServices;
    private final UserMapper userMapper;

    @Autowired
    public AuthenticationServices(AuthenticationManager authenticationManager,
                                  CustomUserDetailsService userDetailsService, JwtUtil jwtUtil,
                                  UserRepository userRepository, UserServices userServices,
                                  UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userServices = userServices;
        this.userMapper = userMapper;
    }

    public ResponseEntity<?> registerUser(RegisterDTO registerDTO) {
        try {
            //email format check
            if (!isValidEmail(registerDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format: " + registerDTO.getEmail());
            }
            //check if user or email already exists
            if (userServices.usernameExists(registerDTO.getUsername())) {
                throw new EntityExistsException("Username already exists: " + registerDTO.getUsername());
            }
            if (userServices.emailExists(registerDTO.getEmail())) {
                throw new EntityExistsException("Email already exists: " + registerDTO.getEmail());
            }
            //convert to entity and save
            UserModel newUser = userMapper.toEntity(registerDTO);
            userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toSimpleDTO(newUser));

        } catch (EntityExistsException e) {
            //Handle the case where the username or email already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            //Handle any other exceptions that may occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<?> login(LoginRequestDTO loginRequestDTO) {
        try {
            String login = loginRequestDTO.getLogin();
            String password = loginRequestDTO.getPassword();
            UserModel user;
            UserDetails userDetails;

            // Login via username OR email
            if (isValidEmail(login)) {
                user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new Exception("User not found"));

                userDetails = userDetailsService.loadUserByEmail(login);
            } else {
                user = userRepository.findByUsername(login)
                        .orElseThrow(() -> new Exception("User not found"));

                userDetails = userDetailsService.loadUserByUsername(login);
            }

            authenticateUser(user.getUsername(), password);

            final String jwt = jwtUtil.generateToken(userDetails, user.isAdmin());
            final HttpHeaders headers = getSuccessfulLoginHeaderResponse(jwt);
            return ResponseEntity.ok()
                    .headers(headers).build();


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        return emailPattern.matcher(email).matches();
    }

    private void authenticateUser(String username, String password) throws Exception {
        try {
            // Attempt to authenticate with the provided username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (Exception e) {
            // Throw an exception if authentication fails
            throw new Exception("Invalid credentials", e);
        }
    }

    private HttpHeaders getSuccessfulLoginHeaderResponse(String jwt) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return headers;
    }
}
