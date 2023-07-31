import './signup.css'
import Email_verify from '../../components/Signup/email_verify'
import Nickname_check from '../../components/Signup/nickname_check'
// import Address_check from '../../components/Signup/address_check'
import Post from '../../components/Signup/address_check'
import Password_check from '../../components/Signup/Password_Check'

export default function Signup() {

  return (
    <div className="w-3/5 h-screen border border-solid border-black rounded
    flex flex-col items-center justify-around">
          <h1>회원가입</h1>
          <Email_verify />
          <Nickname_check />
          <Password_check />
          {/* <Address_check /> */}
          <Post />
          <button className='border-solid border border-black rounded bg-main4 mt-2'>회원가입</button>
    </div>
  )
}
