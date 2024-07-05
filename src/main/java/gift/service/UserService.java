package gift.service;

import gift.dto.UserEncryptedDTO;
import gift.dto.UserRequestDTO;
import gift.dto.UserResponseDTO;
import gift.repository.UserDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserDAO userDAO;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userDAO.findAll();
    }

    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {
        String email = userRequestDTO.email();
        String encryptedPW = bCryptPasswordEncoder.encode(userRequestDTO.password());

        return userDAO.create(new UserEncryptedDTO(email, encryptedPW));
    }

    public void deleteUser(long id) {
        userDAO.delete(id);
    }
}
