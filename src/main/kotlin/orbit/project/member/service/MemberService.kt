package orbit.project.member.service

import orbit.project.member.http.MemberRequest
import orbit.project.member.http.MemberResponse
import orbit.project.member.models.MemberEntity
import orbit.project.member.repository.MemberRepository
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import orbit.project.utils.fail.FailResponse
import orbit.project.utils.success.SuccessResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,  // PasswordEncoder 타입으로 주입받기
) {

    // 회원 저장 서비스 메서드
    fun saveMember(memberRequest: MemberRequest): Mono<SuccessResponse<Nothing>> {
        // 이메일 유효성 검사
        if (!isValidEmail(memberRequest.memberEmail)) {
            throw CustomException(ErrorException.INVALID_EMAIL) // INVALID_EMAIL 예외 발생
        }

        // 비밀번호 유효성 검사
        if (!isValidPassword(memberRequest.memberPassword)) {
            throw CustomException(ErrorException.INVALID_PASSWORD) // INVALID_PASSWORD 예외 발생
        }

        // 비밀번호 암호화 및 저장
        return saveToDatabase(memberRequest)
            .map {
                // 성공 응답 생성
                SuccessResponse(
                    successCode = 200,
                    successResult = true,
                    data = null // 데이터는 필요 없으므로 null로 설정
                )
            }
            .onErrorResume { e ->
                val failResponse = when (e) {
                    is CustomException -> FailResponse(
                        failCode = e.statusCode, // CustomException에서 statusCode 추출
                        failResult = false,
                        data = e.message ?: "Unknown error occurred."
                    )
                    else -> FailResponse(
                        failCode = ErrorException.SERVER_ERROR.statusCode, // 알 수 없는 서버 오류
                        failResult = false,
                        data = ErrorException.SERVER_ERROR.message ?: "Unknown error occurred."
                    )
                }
                Mono.error(e) // 오류 발생 시 실패 응답 반환
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

    //회원 정보 가지고오기
    fun getMemberById(id: Long): Mono<MemberEntity> {
        return memberRepository.findByMemberId(id)
            .flatMap { member -> Mono.just(member) }
    }

    fun getMemberByLoginId(loginId: String): MemberEntity {
        return memberRepository.findByEmail(loginId)
            ?.switchIfEmpty(Mono.error(CustomException(ErrorException.MEMBER_NOT_FOUND))) //빈 값일 걍우 Mono 객체는 반환하지만~
            ?.block() ?: throw CustomException(ErrorException.MEMBER_NOT_FOUND) //ㄱ
    }


}