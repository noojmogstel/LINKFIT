package linkfit.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import linkfit.dto.LoginRequest;
import linkfit.dto.RegisterRequest;
import linkfit.entity.Person;
import linkfit.util.JwtUtil;

@Service
public class AuthService<T extends Person> {

    private final PersonService<T> personService;
    private final ImageUploadService imageUploadService;
    private final JwtUtil jwtUtil;

    public AuthService(@Qualifier("personService") PersonService<T> personService,
        ImageUploadService imageUploadService, JwtUtil jwtUtil) {
        this.personService = personService;
        this.imageUploadService = imageUploadService;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest<T> request, MultipartFile profileImage) {
        T entity = request.toEntity();
        personService.existsByEmail(request.getEmail());
        request.verifyPassword();
        imageUploadService.saveProfileImage(entity, profileImage);
        personService.save(entity);
    }

    public String login(LoginRequest request) {
        T entity = personService.findByEmail(request.getEmail());
        entity.validatePassword(request.getPassword());
        return jwtUtil.generateToken(entity.getId(), entity.getEmail());
    }
}
