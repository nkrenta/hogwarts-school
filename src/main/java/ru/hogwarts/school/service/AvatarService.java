package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarService {

    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    Avatar findAvatar(long studentId);

    Page<Avatar> findAllPaginated(int pageNumber, int pageSize, String sortField, Sort.Direction direction);
}
