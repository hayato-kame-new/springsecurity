package com.kame.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kame.springboot.form.SignupForm;
import com.kame.springboot.service.UserDetailsServiceImpl;

@Controller
@RequestMapping("/")
public class TestController {
	
	@Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@GetMapping
    public String index () {
        return "index";
    }

    @GetMapping("/login")
    public String login () {
        return "login";
    }
    
    /**
     * 「ユーザー登録」ページを表示するだけ
     * @param signupForm
     * @return "signup"
     */
    @GetMapping("/signup")
    public String newSignup(SignupForm signupForm) {
        return "signup";
    }

    /**
     * ユーザー情報の登録実行
     * UserDetailsServiceImpl において作成した register メソッドを使用して
     * ユーザー情報をデータベースに格納します
     * ユーザー登録に失敗した場合は、「ユーザー登録に失敗しました」というメッセージを表示する 既に登録されているユーザー名で登録しようとすると、とりあえずエラーメッセージが表示
     * @param signupForm
     * @param model
     * @return 成功:"redirect:/"<br /> 失敗:"signup"
     */
    @PostMapping("/signup")
    public String signup(SignupForm signupForm, Model model) {
        try {
            userDetailsServiceImpl.register(signupForm.getUsername(), signupForm.getPassword(), "ROLE_USER");
        } catch (DataAccessException e) {
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            return "signup";
        }
        return "redirect:/";
    }
}