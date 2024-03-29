package com.zelezniak.project.author;


import com.zelezniak.project.exception.CourseException;
import com.zelezniak.project.exception.CustomErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
final class AuthorServiceImpl implements AuthorService {

    private final CourseAuthorRepository authorRepository;

    public CourseAuthor findByEmail(String email) {
        return authorRepository.findByEmail(email);
    }

    public List<CourseAuthor> getAllAuthors() {
        List<CourseAuthor> authors = authorRepository.findAll();
        //return only authors without admin people
        authors.removeIf(courseAuthor -> courseAuthor.getRoles()
                .stream()
                .anyMatch(role -> role.toString().contains("ADMIN")));
        return authors;
    }

    public void saveAuthor(CourseAuthor author) {
    if(author!=null) {authorRepository.save(author);}
    }

    public boolean existsByEmail(String email) {
        return authorRepository.existsByEmail(email);
    }

    public CourseAuthor getById(Long authorId) {
        return authorRepository.findByAuthorId(authorId)
                .orElseThrow(() -> new CourseException(CustomErrors.USER_NOT_FOUND));
    }
}