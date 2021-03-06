package com.kame.springboot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kame.springboot.entity.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    JdbcTemplate jdbcTemplate;
	
	// 追加 ユーザー新規登録の時に必要 ＠Autowired アノテーションを使用して、SecurityConfig クラスで Bean 定義した PasswordEncode を取得します。
	@Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
        	// カラム の名前は小文字にして テーブル名は users です user ではない
            String sql = "SELECT * FROM users WHERE name = ?";  // PostgreSQLはuserが使えないので usersにしてます
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, username);
            String password = (String)map.get("password");
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
            return new UserDetailsImpl(username, password, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found.", e);
        }
    }
    
 // カラム の名前は小文字にして テーブル名は users です user ではない
    // JdbcTemplate の update メソッドで、データベースにユーザー情報を登録します
    // パスワードは、PasswordEncoder（BCrypt）でハッシュ化しておきます
    @Transactional
    public void register(String username, String password, String authority) {
        String sql = "INSERT INTO users(name, password, authority) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, username, passwordEncoder.encode(password), authority);
    }
    
    
    /**
     * データベースに同一ユーザー名が既に登録されているかを確認する
     * JdbcTemplate の queryForObject メソッドを使用してデータベース内の検索結果を取得
     * 同一ユーザー名が存在すれば true、存在しなければ　false を返します
     * @param username
     * @return true:同一ユーザー名が存在する<br /> false:存在しない
     */
    public boolean isExistUser(String username) { // からむは nameになってる
        String sql = "SELECT COUNT(*) FROM users WHERE name = ?";  // users テーブル名
        int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { username });
        if (count == 0) {
            return false;
        }
        return true;
    }
}

