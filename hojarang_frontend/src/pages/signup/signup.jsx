import './signup.css'
import Email_verify from '../../components/Signup/email_verify'


export default function Signup() {

  return (
    <div>
        <h1>Signup</h1>
          <Email_verify />
          <div>
            비밀번호
            <br />
            <input type="password"/>
          </div>
          <div>
            비밀번호 확인
            <br />
            <input type="password"/>
          </div>
          <div>
            주소
            <br />
            <input type="text" />
            <button className='border-solid border border-black rounded bg-gray2 ml-2'>주소 검색</button>
          </div>
          <div>
            상세주소
            <br />
            <input type="text" id="address2"/>
          </div>
          <button className='border-solid border border-black rounded bg-main4 mt-2'>회원가입</button>
    </div>
  )
}
