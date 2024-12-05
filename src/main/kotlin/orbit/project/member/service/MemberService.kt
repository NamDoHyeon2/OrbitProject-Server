package orbit.project.member.service

import orbit.project.member.http.MemberRequest
import orbit.project.member.http.MemberResponse
import orbit.project.member.models.MemberEntity
import orbit.project.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder // PasswordEncoder 주입
) {

    // 회원 저장 서비스 메서드
    fun saveMember(memberRequest: MemberRequest): Mono<MemberResponse> {

        // 이메일 유효성 검사
        if (!isValidEmail(memberRequest.memberEmail)) {
            return Mono.error(IllegalArgumentException("유효하지 않은 이메일 형식입니다."))
        }

        // 비밀번호 유효성 검사
        if (!isValidPassword(memberRequest.memberPassword)) {
            return Mono.error(IllegalArgumentException("비밀번호는 8~20자이며, 숫자, 문자, 특수문자를 포함해야 합니다."))
        }

        // 아이디 검증, 비밀번호 암호화, 저장을 순차적으로 실행
        return validateLoginId(memberRequest.memberLoginId)
            .flatMap {
                saveToDatabase(memberRequest)
            }
            .flatMap { resultMemberEntity ->
                MemberResponse.fromEntity(Mono.just(resultMemberEntity))
            }
    }

    // 이메일 유효성 검사 함수
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(Regex(emailRegex))
    }

    // 비밀번호 유효성 검사 함수
    fun isValidPassword(password: String): Boolean {
        println(password)
        val regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*(),.?\":{}|<>]).{8,20}$"
        return password.matches(Regex(regex))
    }


    // 아이디 중복 검증 함수
    private fun validateLoginId(loginId: String): Mono<Boolean> {
        return memberRepository.existsByLoginId(loginId)
            .flatMap { exists ->
                if (exists) {
                    Mono.error(IllegalArgumentException("이미 사용 중인 아이디입니다: $loginId"))
                } else {
                    Mono.just(true)
                }
            }
    }

    private fun encryptPassword(password: String): String {
        return passwordEncoder.encode(password)
    }

    // 회원 저장 함수
    private fun saveToDatabase(memberRequest: MemberRequest): Mono<MemberEntity> {
        val encryptedPassword = encryptPassword(memberRequest.memberPassword)
        val saveMember = MemberEntity.fromRequest(
            memberRequest.copy(memberPassword = encryptedPassword)
        )
        return memberRepository.save(saveMember)
    }

}