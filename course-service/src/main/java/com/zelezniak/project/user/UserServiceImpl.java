package com.zelezniak.project.user;


import com.zelezniak.project.author.CourseAuthor;
import com.zelezniak.project.author.CourseAuthorRepository;
import com.zelezniak.project.exception.CourseException;
import com.zelezniak.project.exception.CustomErrors;
import com.zelezniak.project.role.Role;
import com.zelezniak.project.role.RoleRepository;
import com.zelezniak.project.student.Student;
import com.zelezniak.project.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final CourseAuthorRepository authorRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String ROLE_STUDENT = "ROLE_STUDENT";
    private static final String ROLE_TEACHER = "ROLE_TEACHER";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    @Transactional
    public void createNewUser(UserData user) {
        if (user != null) {
            validateIfUserExistsAndEmailFormat(user.getEmail());
            String role = user.getRole();
            if (role.equals(ROLE_STUDENT)) {
                studentRepository.save(newStudent(user));
            } else if (role.equals(ROLE_TEACHER)) {
                authorRepository.save(newAuthor(user));
            }
        }
    }

    private Student newStudent(UserData user) {
        Student studentToSave = setStudentData(user);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ROLE_STUDENT));
        studentToSave.setRoles(roles);
        return studentToSave;
    }

    private CourseAuthor newAuthor(UserData user) {
        CourseAuthor authorToSave = setAuthorData(user);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ROLE_STUDENT));
        roles.add(roleRepository.findByName(ROLE_TEACHER));
        authorToSave.setRoles(roles);
        return authorToSave;
    }

    private CourseAuthor setAuthorData(UserData user) {
        return CourseAuthor.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(encodePassword(user.getPassword()))
                .build();
    }

    private Student setStudentData(UserData user) {
        return Student.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(encodePassword(user.getPassword()))
                .build();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CourseAuthor author = authorRepository.findByEmail(username);
        if (author != null) {return UserDetailsBuilder.buildUserDetails(author);}
        else {
            Student student = studentRepository.findByEmail(username);
            if (student != null) {return UserDetailsBuilder.buildUserDetails(student);}
            else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }
    }

    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    private void validateIfUserExistsAndEmailFormat(String email) {
        if (email.matches(EMAIL_PATTERN)) {
            if (studentRepository.existsByEmail(email) || authorRepository.existsByEmail(email)) {
                throw new CourseException(CustomErrors.USER_ALREADY_EXISTS);}
        } else {throw new CourseException(CustomErrors.EMAIL_IN_WRONG_FORMAT);}
    }
}