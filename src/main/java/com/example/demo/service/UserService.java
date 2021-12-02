package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserDetails;
import com.example.demo.entity.Workspace;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.*;
import com.example.demo.repository.UserDetailsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkspaceRepository;
import com.example.demo.utility.HashUtils;
import com.example.demo.utility.MapperUtility;
import com.example.demo.validator.EmailValidator;
import com.example.demo.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private static Map<String, Integer> numberOfSeatsPerWorkspace = new HashMap<>();

    public UserResponseDto saveUser(UserRequestDto userDto) throws ConstraintViolationException {
        User user = MapperUtility.map(userDto, User.class);
        boolean alreadyExistsUserWithSameEmail = repository.existsByEmail(user.getEmail());
        boolean samePassword = PasswordValidator.arePasswordAndRePasswordSame(userDto.getPassword(), userDto.getRePassword());
        boolean isEmailValid = EmailValidator.validate(userDto.getEmail());

        if (alreadyExistsUserWithSameEmail || !samePassword || !isEmailValid) {
            return null;
        }
        user.setPassword(HashUtils.encodeString(user.getPassword()));

        return MapperUtility.map(repository.save(user), UserResponseDto.class);
    }

    public List<User> saveUsers(List<User> users) {
        return repository.saveAll(users);
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public UserResponseDto getUserById(int id) throws ResourceNotFoundException {

        return MapperUtility.mapStrict(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + id)), UserResponseDto.class);
    }

    public void deleteUserById(int id) {
        repository.deleteById(id);
    }

    //check does user with given email and password already exists in database
    public UserResponseDto authenticateUser(LoginRequestDto loginRequestDto) throws ResourceNotFoundException {
        User user = repository.findByEmail(loginRequestDto.getEmail());
        if (user != null) {
            if (BCrypt.checkpw(loginRequestDto.getPassword(), user.getPassword())) {
                return MapperUtility.map(user, UserResponseDto.class);
            } else {
                throw new ResourceNotFoundException("Wrong password!");
            }
        } else {
            throw new ResourceNotFoundException("Not found user with given email!");
        }
    }

    public Integer saveUserDetails(UserDetailsDto userDetailsDto, Integer userId) throws ConstraintViolationException {
        UserDetails userDetails = MapperUtility.map(userDetailsDto, UserDetails.class);

        if (EmailValidator.validate(userDetailsDto.getAdditionalEmailAddress())) {
            User user = repository.findById(userId).orElse(null);

            userDetails.setUser(null);
            user.setUserDetails(userDetails);

            System.out.println("User.UserDetails: " + user.getUserDetails());
            repository.save(user);

            return userId;
        }
        return 0;
    }

    public void saveWorkspace(WorkspaceDto workspaceDto, Integer userIdFromJwt) throws ConstraintViolationException, ResourceNotFoundException {
        User user = repository.findById(userIdFromJwt).orElseThrow();

        Workspace workspace = MapperUtility.mapStrict(workspaceDto, Workspace.class);
        workspace.setUser(user);

        user.getWorkspaces().add(workspace);
        repository.save(user);
    }

    public Optional<Workspace> findAllWorkspacesById(Long id) throws ResourceNotFoundException {  //one user can have many workspaces
        return workspaceRepository.findWorkspaceById(id);
    }

    public Set<Workspace> findAllWorkspacesByUserId(int id) throws ResourceNotFoundException {
        Optional<User> user = repository.findById(id);
        return user.get().getWorkspaces();
    }

    public void removeWorkspace(Long id) throws ResourceNotFoundException {
        workspaceRepository.deleteById(id);
    }

    public WorkspaceDto updateWorkspace(long userId, WorkspaceDto workspaceDto) {

        Workspace workspaceDb = workspaceRepository.findById(workspaceDto.getId()).orElseThrow();
        if (userId != workspaceDb.getUser().getId()) {
            throw new UnauthorizedException();
        }
        workspaceDb.setSeats(workspaceDto.getSeats());
        workspaceDb.setCoordinates(workspaceDto.getCoordinates());
        workspaceDb.setName(workspaceDto.getName());

        Workspace savedWorkspace = workspaceRepository.save(workspaceDb);
        return MapperUtility.map(savedWorkspace, WorkspaceDto.class);

    }

    public boolean validateNumberOfSeats(String workspaceName, Integer numberOfSeats) {
        if (numberOfSeatsPerWorkspace.containsKey(workspaceName)) {
            Integer numberOfOccupiedSeatsForWorkspace = numberOfSeatsPerWorkspace.get(workspaceName);
            ++numberOfOccupiedSeatsForWorkspace;
            if ((numberOfOccupiedSeatsForWorkspace) > numberOfSeats) {
                return false;
            }
            numberOfSeatsPerWorkspace.put(workspaceName, numberOfOccupiedSeatsForWorkspace);
            return true;
        } else {
            numberOfSeatsPerWorkspace.put(workspaceName, 1);
            return true;
        }
    }
}


