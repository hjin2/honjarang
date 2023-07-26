import './signup.css'

export default function Signup() {
  const duplicate_email = false
  const duplicate_nickname = false
  return (
    <div>
        <h1>Signup</h1>
          <div class="email">
            이메일
            <br />
            <input type="text" />
            <button>인증번호 전송</button>
          </div>
          <div class="verify">
            인증번호 입력
            <br />
            <input type="number" />
            <button>인증번호 확인</button>
          </div>
          <div class="nickname">
            닉네임
            <br />
            <input type="text" />
            <button>닉네임 중복 확인</button>
          </div>
          <div class="password">
            비밀번호
            <br />
            <input type="password" id="password"/>
          </div>
          <div class="password_confirm">
            비밀번호 확인
            <br />
            <input type="password" id="password_confirm"/>
          </div>
          <div class="address">
            주소
            <br />
            <input type="text" /> <button>주소 검색</button>
          </div>
          <div class="address2">
            상세주소
            <br />
            <input type="text" id="address2"/>
          </div>
    </div>
  )
}
