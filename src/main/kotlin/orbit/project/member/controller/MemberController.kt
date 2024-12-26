package orbit.project.member.controller

import orbit.project.member.http.MemberRequest
import orbit.project.member.http.MemberResponse
import orbit.project.member.service.MemberService
import orbit.project.utils.success.SuccessResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService : MemberService
) {

    // 회원가입
    @PostMapping("/register")
    fun authRegister(@RequestBody memberRequest: MemberRequest): Mono<SuccessResponse<MemberResponse>> =
        memberService.saveMember(memberRequest)
            .map { memberResponse -> SuccessResponse(code = 200, message = true, data = memberResponse) }



}