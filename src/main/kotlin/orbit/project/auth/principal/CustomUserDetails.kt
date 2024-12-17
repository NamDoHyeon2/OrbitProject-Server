import orbit.project.member.models.MemberEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val memberEntity: MemberEntity,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return memberEntity.password //근데 이렇게 하면 암호화된게 풀려서 오나?
    }

    override fun getUsername(): String {
        return memberEntity.name
    }

    override fun isAccountNonExpired(): Boolean {
        // 계정 만료 여부 체크 (여기서는 항상 true로 설정)
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        // 계정 잠금 여부 체크 (여기서는 항상 true로 설정)
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        // 인증 정보 만료 여부 체크 (여기서는 항상 true로 설정)
        return true
    }

    override fun isEnabled(): Boolean {
        // 계정 활성화 여부 체크 (여기서는 항상 true로 설정)
        return true
    }

}
