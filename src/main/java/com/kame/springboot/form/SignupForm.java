package com.kame.springboot.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


// フォームクラスには エンティティのアノテーションはつけない
public class SignupForm {
	
	
	// フォームには、入力チェックを行うためのアノテーションを付けていきます
	//Null チェックには、一般的には ＠NotNull を使いますが、ここでは、＠NotBlank を使用しています。
//	＠NotNull 又は ＠NotEmpty を使用した場合、半角スペースのみでもユーザー名として登録ができてしまいますが、
//	この半角スペースのユーザー名ではログインすることができないためです。
	// なお、Spring MVC では「文字列の入力フィールドに未入力の状態でフォームを送信した場合、デフォルトではフォームオブジェクトにnullではなく、空文字がバインドされる」ため、＠NotNull は入力値のチェックでは働きません。
	
	 @NotBlank // これにしてください
	    @Size(min = 1, max = 50, message = "ユーザー名は1文字以上50文字以下で入力してください")  // 入力文字数の最小値と最大値を指定
    private String username;
	 
	 @NotBlank  //  Null、空文字、空白をエラーとする これにしてください
	    @Size(min = 6, max = 20)
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
