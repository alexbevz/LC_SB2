package ru.bevz.LC_SB2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.bevz.LC_SB2.domain.Message;
import ru.bevz.LC_SB2.domain.User;
import ru.bevz.LC_SB2.repos.MessageRepo;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepo messageRepo;

    private final ControllerUtils controllerUtils;

    public MainController(MessageRepo messageRepo, ControllerUtils controllerUtils) {
        this.messageRepo = messageRepo;
        this.controllerUtils = controllerUtils;
    }

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
            model.mergeAttributes(controllerUtils.getErrors(bindingResult));
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);
            model.addAttribute("message", null);
        }

        List<Message> messageRepoAll = (List<Message>) messageRepo.findAll();
        model.addAttribute("messages", messageRepoAll);

        return "main";
    }

    private void saveFile(Message message, MultipartFile file) throws IOException {
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
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        Set<Message> messages = user.getMessages();

        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", user.equals(currentUser));
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("userChannel", user);
        model.addAttribute("countSubscriptions", user.getSubscriptions().size());
        model.addAttribute("countSubscribers", user.getSubscribers().size());

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "user") Long userId,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (message.getAuthor().equals(currentUser)) {
            if (!text.isEmpty()) {
                message.setText(text);
            }
            if (!tag.isEmpty()) {
                message.setTag(tag);
            }
            saveFile(message, file);
        }

        return "redirect:/user-messages/" + userId;
    }
}
