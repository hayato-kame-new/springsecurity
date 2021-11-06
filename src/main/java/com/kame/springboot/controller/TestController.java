package com.kame.springboot.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
// security.core.annotationの方です
// import org.springframework.security.core.annotation.AuthenticationPrincipal;  // こっちです消されないようにコメントアウトでコピーしただけです
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kame.springboot.entity.UserDetailsImpl;
import com.kame.springboot.form.SignupForm;
import com.kame.springboot.service.UserDetailsServiceImpl;


@Controller
@RequestMapping("/")
public class TestController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    
    /**
     * SecurityContextHolder からユーザー情報を取得する
     * @return "index"
     */
//    @GetMapping
//    public String index() {
//    	
//    	// SecurityContextHolder からユーザー情報を取得する
//    	// ユーザー情報を SecurityContextHolder から取得することもできます
//    	 // SecurityContextHolderからAuthenticationオブジェクトを取得
//    	SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
//
//        // Authenticationオブジェクトからユーザー情報を取得
//        System.out.println(authentication.getName());  // ユーザー名を表示
//        System.out.println(authentication.getAuthorities());  // 権限情報を表示
//        
//     // Authenticationオブジェクトからユーザー情報を取得
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
//        System.out.println(principal.getUsername());  // ユーザー名を表示
//        System.out.println(principal.getPassword());  // パスワードを表示
//        System.out.println(principal.getAuthorities());  // 権限情報を表示
//           	
//        return "index";
//    }
    
    //  ＠AuthenticationPrincipal からユーザー情報を取得する
    // ユーザー情報は、アノテーション ＠AuthenticationPrincipal を使用すると、より簡単に取得することができます
    // TestController のindex メソッドを変更すると import org.springframework.security.core.annotation.AuthenticationPrincipal;
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(userDetails.getUsername());  // ユーザー名を表示
        System.out.println(userDetails.getPassword());  // パスワードを表示
        System.out.println(userDetails.getAuthorities().toString());  // 権限情報を表示
        return "index";
    }
    

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String newSignup(SignupForm signupForm) {
        return "signup";
    }

    /**
     * 
     * @param signupForm
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/signup")
    public String signup(@Validated SignupForm signupForm, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "signup";
        }
        
        // true: 同一ユーザーが存在する
        if (userDetailsServiceImpl.isExistUser(signupForm.getUsername())) {
            model.addAttribute("signupError", "ユーザー名 " + signupForm.getUsername() + "は既に登録されています");
            return "signup";
        }

        // ユーザ登録に成功したら、ログイン済みと同じになるらしい
        try {
            userDetailsServiceImpl.register(signupForm.getUsername(), signupForm.getPassword(), "ROLE_USER");
        } catch (DataAccessException e) {
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            return "signup";
        }
        // メソッドの引数に HttpServletRequest request を追加　
        // 自動でログイン処理を行うコードを追加します
        // 既にログインしている場合は、一旦ログアウトさせるようにしてます
        // SecurityContext は、セキュリティ情報（コンテキスト）を定義するインターフェース
        // 実行中のアプリケーションに関するセキュリティ情報（コンテキスト）は、SecurityContextHolder の getContext メソッドを使用することで取得することができます
        // この SecurityContextHolder には、現在アプリケーションとやり取りをしているユーザーに関する情報も含まれています。
        // セッション中のユーザー情報の取得 
        // このユーザーに関する情報は、Authentication インターフェイスで定義されており、SecurityContextHolder の getAuthentication メソッドを使用することにより取得することができます
        // この Authentication オブジェクトから、ユーザー名や権限情報（ロール）なども取得することができます
        SecurityContext context = SecurityContextHolder.getContext(); 
        Authentication authentication = context.getAuthentication();

        //  匿名ユーザー（anonymousUser）であることの判定 つまり、ログインしてないユーザかどうか
        // 「ログイン済みのユーザー」であることを判定することですが、そのために「匿名ユーザー」（ログインしていないユーザー） かどうかを判定する手段を用います
        // 匿名ユーザー（anonymousUser）かどうかは、Authentication オブジェクトが AnonymousAuthenticationToken インスタンスであるかどうかを確認することで判定することができます
        // ログアウト処理を実行する
        // ログアウト処理は、SecurityContextHolder に格納されているセキュリティ情報（コンテキスト）を消去することで実行します
        // 消去には、clearContext メソッドを使用します。
        if (authentication instanceof AnonymousAuthenticationToken == false) {
        	// きちんとユーザ登録されてると、ここには来ない つまり、セッション中のユーザー情報があるので、すでにログイン済みに似てる状態にはなってる
        	// でも、明示的にログイン処理はしてないので、下で、ログインをする必要がある
            SecurityContextHolder.clearContext(); 
        }

        // ログイン処理を行うコード ユーザ登録されて、セッションにはあるけど、きちんとログインをする ユーザにさせずに、ここで自動的にログインをしている 
        // HttpServletRequest オブジェクトの login メソッドを使用して、新たに登録されたユーザー名とパスワードでログイン処理を行っています
        
        try {
            request.login(signupForm.getUsername(), signupForm.getPassword());
        } catch (ServletException e) {
            e.printStackTrace();
        }

        
        return "redirect:/";
    }
}