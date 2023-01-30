package com.ssrmms.proyecto_dam.controller;

import com.ssrmms.proyecto_dam.dto.ChangePasswordForm;
import com.ssrmms.proyecto_dam.entity.User;
import com.ssrmms.proyecto_dam.repository.RoleRepository;
import com.ssrmms.proyecto_dam.service.UserService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

        @GetMapping("/escritorio")
    public String escritorio() {
        return "escritorio";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/userForm")
    public String userForm(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll()); //para consultar role
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }

    @PostMapping("/userForm")
    public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");

        } else {
            try {
                userService.createUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("roles", roleRepository.findAll());
            }
        }
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll());

        return "user-form/user-view";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUserForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
        User user = userService.getUserById(id);

        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("userForm", user);
        model.addAttribute("formTab", "active");//Activa el tab del formulario.

        model.addAttribute("editMode", true);
        model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
                
        return "user-form/user-view";
    }

    @PostMapping("/editUser")
    public String postEditUserForm(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab", "active");
            model.addAttribute("editMode", "true");
            model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
        } else {
            try {
                userService.updateUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab", "active");
            } catch (Exception e) {
                
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab", "active");
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("roles", roleRepository.findAll());
                model.addAttribute("editMode", "true");
                model.addAttribute("passwordForm",new ChangePasswordForm(user.getId()));
            }
        }

        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "user-form/user-view";
    }

    @GetMapping("/userForm/cancel")
    public String cancelEditUser(ModelMap model) {
        return "redirect:/userForm";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(Model model, @PathVariable(name = "id") Long id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            model.addAttribute("deleteError", "The user could not be deleted.");
        }
        return userForm(model);
    }
    @PostMapping("/editUser/changePassword")
	public ResponseEntity<String> postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		try {
			//If error, just return a 400 bad request, along with the error message
	        if (errors.hasErrors()) {
	            String result = errors.getAllErrors()
	                        .stream().map(x -> x.getDefaultMessage())
	                        .collect(Collectors.joining(""));

	            throw new Exception(result);
	        }
			userService.changePassword(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}
    
    
    
    
    
    
    
    
    
    
}
