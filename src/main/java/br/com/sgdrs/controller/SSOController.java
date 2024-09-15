//package br.com.sgdrs.controller;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/SSO")
//public class SSOController {
//
//
//    @PostMapping("/loginS")
//    public String loginSuccess(@AuthenticationPrincipal OAuth2User principal, Model model) {
//        model.addAttribute("email", principal.getAttribute("email"));
//        return "loginSuccess"; // Nome da view a ser exibida após login bem-sucedido
//    }
//}
