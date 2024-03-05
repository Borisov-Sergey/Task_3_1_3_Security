package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.SecurityUserDetails;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getUserAccount(@AuthenticationPrincipal SecurityUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        return "userPage";
    }
    @GetMapping(value = "/admin")
    public String getAllUser(Model model) {
        model.addAttribute("user", userService.getAll());
        return "adminPage";
    }

    @GetMapping(value = "/update")
    public String getUpdateUser(@RequestParam(value = "id", required = false) Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "updateUser";
    }

    @PostMapping(value = "/update")
    public String postUpdateUser(@ModelAttribute User user,
                                 @RequestParam(value = "rls") String[] roles) {
        Set<Role> resultRole = new HashSet<>();
        for (String role : roles) {
            resultRole.add(roleService.findByName(role));
        }
        user.setRoles(resultRole);
        userService.update(user);

        return "redirect:/user/admin";
    }


    @GetMapping(value = "/save")
    public String getSaveNewUser(Model model) {
        model.addAttribute("user", new User());
        return "saveNewUser";
    }

    @PostMapping(value = "/save")
    public String postSaveNewUser(@ModelAttribute User user,
                                  @RequestParam(value = "rls") String[] roles) {
        Set<Role> result = new HashSet<>();
        for (String role : roles) {
            result.add(roleService.findByName(role));
        }

        user.setRoles(result);
        userService.save(user);
        return "redirect:/user/admin";
    }



    @GetMapping(value = "/delete")
    public String getDeleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.removeById(id);
        return "redirect:/user/admin";
    }

}
