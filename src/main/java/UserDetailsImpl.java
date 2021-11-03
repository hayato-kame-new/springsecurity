import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

	/**
	 * シリアル番号UID
	 */
	private static final long serialVersionUID = -2054376501550251092L;
	
	// 3つのフィールド変数を追加
	private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;

    // ３つのフィールドを使ってコンストラクタを生成
	public UserDetailsImpl(String username, String password, Collection<GrantedAuthority> authorities) {
		super();
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	//メソッドの戻り値は、次のようにしておきます
	// 最後の4つは、特に判定せず、全て true を返すようにしておきます。

	// 権限リストを返す 戻り値の設定  コンストラクタで設定した値
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO 自動生成されたメソッド・スタブ
		//return null;
		return authorities;
	}

	// パスワードを返す 戻り値の設定  コンストラクタで設定した値
	@Override
	public String getPassword() {
		// TODO 自動生成されたメソッド・スタブ
		// return null;
		return password;
	}

	// ユーザ名を返す 戻り値の設定  コンストラクタで設定した値
	@Override
	public String getUsername() {
		// TODO 自動生成されたメソッド・スタブ
		// return null;
		return username;
	}

	// 常に true とする
	// アカウントの有効期限の判定
	@Override
	public boolean isAccountNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		// return false;
		return true;
	}

	// 常に true とする  アカウントのロック状態の判定
	@Override
	public boolean isAccountNonLocked() {
		// TODO 自動生成されたメソッド・スタブ
		// return false;
		return true;
	}

	// 常に true とする 資格情報の有効期限の判定
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO 自動生成されたメソッド・スタブ
		// return false;
		return true;
	}

	// 常に true とする 有効なユーザーであるかの判定
	@Override
	public boolean isEnabled() {
		// TODO 自動生成されたメソッド・スタブ
		// return false;
		return true;
	}

}
