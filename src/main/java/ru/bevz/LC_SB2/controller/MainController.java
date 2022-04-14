package ru.bevz.LC_SB2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.bevz.LC_SB2.domain.Message;
import ru.bevz.LC_SB2.domain.User;
import ru.bevz.LC_SB2.repos.MessageRepo;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.util.Objects;
import java.util.UUID;

@Controller
public class MainController {

    private final MessageRepo messageRepo;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, name = "filter", defaultValue = "") String filter,
            Model model
    ) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file

    ) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.getErrors(bindingResult));
            model.addAttribute("message", message);
        } else {
            if (!file.isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    if (!uploadDir.mkdir()) {
                        throw new AccessException("You are innocent! Some problem on the server!");
                    }
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFileName));

                message.setFilename(resultFileName);
            }

            messageRepo.save(message);
            model.addAttribute("message", null);
        }

        model.addAttribute("messages", messageRepo.findAll());

        return "main";
    }

}
