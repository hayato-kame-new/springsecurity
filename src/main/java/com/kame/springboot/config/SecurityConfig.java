package com.kame.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//@EnableWebSecurity
//厳密に言うと、このアノテーションにより
//「Spring Security が提供しているコンフィギュレーションクラスがインポートされ、
//Spring Security を利用するために必要となるコンポーネントの Been 定義が自動で行われる」とのことです（Spring 徹底入門414ページ）。

// ＠Configuration と ＠EnableWebSecurity の両方を指定
// ＠EnableWebSecurity のみを指定 のやり方があるらしい
// ＠Configuration のみを指定  どのやり方でも動くらしい

@Configuration
@EnableWebSecurity  // これを指定することで Spring Security の機能が有効化されます
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	// com.kame.springboot.config パッケージを作って、そこに作る
	// このクラスには「Spring Securityの設定情報が定義されており、対象のメソッドをオーバーライドすることで設定を変更することができる
	
	// これで、Basic認証が行われなくなります
//	  @Override
//	    protected void configure(HttpSecurity http) throws Exception{
//	        http.authorizeRequests().antMatchers("/").permitAll();
//	    }
	
	
//	クライアント側から入力された平文のパスワードと、データベースのハッシュ化されたパスワードを照合するために、パスワードエンコーダーを設定する必要があります。
//
//	次のように、＠Been アノテーションを付けて Been 定義を行い、DI コンテナに登録します。
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
    }
	/**
	 * このサンプルコードは、ユーザー名が「yama3」、パスワードは「123456」の場合です。
パスワードは「ハッシュ化された文字列」をセットする必要がありますので、BCryptPasswordEncoder の encode メソッドでハッシュ化しています。

当然ですが、次のように、ハッシュ化されたパスワードを直接記載しても、問題なく動作します。 
	 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	 // System.out.println(new BCryptPasswordEncoder().encode("123456"));
    	
    	// DB はまだ使わないので、inMemoryAuthentication というメソッドを使用して、メモリ内にユーザー情報を格納して認証を行うようにしています。
    	// ユーザー名、パスワード等は、次の形で指定します。
        auth.inMemoryAuthentication()  // メモリ内認証を追加する
            .withUser("yama3")  // ユーザー名を指定 これをログイン画面で打ち込めばログインできる
            .password(passwordEncoder().encode("123456"))  // パスワードを指定（ハッシュ化されたもの） この 123456を打ち込めばログインできる
            .roles("USER"); // ユーザーが保持する権限情報を指定
        // これでhttp://localhost:8080/ にアクセスしてユーザー名とパスワードを入力します。ログイン成功する
        
        //  auth.inMemoryAuthentication().withUser(ユーザー名).password(パスワード).roles(権限情報);
//        こっちでもOK
//        auth.inMemoryAuthentication()
//        .withUser("yama3")
//        .password("$2a$10$E55vg96856cWy4oyAUpQ6OH2mxO6eTt43A5lPwa3MszPbDpAOPiLG")
//        .roles("USER");
        
        // パスワードは平文ではなく、ハッシュ化した文字列を記載する必要があります。
        // あらかじめハッシュ化した文字列を指定したい場合は、どこか適宜の場所に次のコードを書けば、コンソール画面から取得できます。
        // System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// 「全てのリクエストの承認は、ログインしていることが条件」
        http.authorizeRequests()
            .anyRequest()
            .authenticated();
        
        // formLogin メソッドでフォーム認証を使用することを指定しています。
        // loginPage メソッドでログイン画面のURLを /login と指定し、defaultSuccessUrl メソッドで認証後にリダイレクトされるページを指定し、
        // permitAll メソッドで全てのユーザーにアクセスの許可を与えています。
        // なお、Spring Security では、フォーム認証のほか、Basic 認証、Digest 認証、Remember Me 認証用のサーブレットフィルタクラスも提供されています（Spring 徹底入門423ページ）。

        http.formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .permitAll();
    }

}
