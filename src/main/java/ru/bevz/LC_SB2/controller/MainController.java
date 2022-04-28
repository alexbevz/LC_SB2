package ru.bevz.LC_SB2.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import ru.bevz.LC_SB2.service.MessageService;

import javax.validation.Valid;

@Controller
public class MainController {

    private final MessageRepo messageRepo;

    private final ControllerUtils controllerUtils;

    private final MessageService messageService;

    public MainController(MessageRepo messageRepo, ControllerUtils controllerUtils, MessageService messageService) {
        this.messageRepo = messageRepo;
        this.controllerUtils = controllerUtils;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Message> pageMessages;

        if (!filter.isBlank()) {
            pageMessages = messageRepo.findByTag(filter, pageable);
        } else {
            pageMessages = messageRepo.findAll(pageable);
            filter = null;
        }

        model.addAttribute("pageMessages", pageMessages);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message, BindingResult bindingResult,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("file") MultipartFile file
    ) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(controllerUtils.getErrors(bindingResult));
            message.setId(0L);
            model.addAttribute("message", message);
        } else {
            message.setAuthor(user);
            messageService.saveMessage(message, file);
            model.addAttribute("message", null);
        }

        Page<Message> pageMessages = messageRepo.findAll(pageable);

        model.addAttribute("url", "/main");
        model.addAttribute("pageMessages", pageMessages);

        return "main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user, Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Message message
    ) {
        Page<Message> pageMessages = messageRepo.findByAuthor(user, pageable);

        model.addAttribute("pageMessages", pageMessages);
        model.addAttribute("url", "/user-messages/" + user.getId());

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
    ) {

        if (message.getAuthor().equals(currentUser)) {
            if (!text.isEmpty()) {
                message.setText(text);
            }
            if (!tag.isEmpty()) {
                message.setTag(tag);
            }
            messageService.saveMessage(message, file);
        }

        return "redirect:/user-messages/" + userId;
    }
}
