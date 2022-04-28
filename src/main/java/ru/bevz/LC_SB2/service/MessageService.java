package ru.bevz.LC_SB2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bevz.LC_SB2.domain.Message;
import ru.bevz.LC_SB2.domain.User;
import ru.bevz.LC_SB2.domain.dto.MessageDto;
import ru.bevz.LC_SB2.repos.MessageRepo;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class MessageService {

    @Value("${upload.path}")
    private String uploadPath;
    private final MessageRepo messageRepo;


    public MessageService(MessageRepo messageRepo, EntityManager em) {
        this.messageRepo = messageRepo;
    }

    public void saveMessage(Message message, MultipartFile file) {
        if (!file.isEmpty()) {
            saveFile(message, file);
        }
        messageRepo.save(message);
    }

    private void saveFile(Message message, MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "." + file.getOriginalFilename();

        try {
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Page<MessageDto> getPageMessages(Pageable pageable, String filter, User user) {
        Page<MessageDto> pageMessages;
        if (!filter.isBlank()) {
            pageMessages = messageRepo.findByTag(filter, pageable, user);
        } else {
            pageMessages = messageRepo.findAll(pageable, user);
            filter = null;
        }
        return pageMessages;
    }
}
