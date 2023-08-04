import axios from "axios"
import { useState } from "react"

export default function Nickname_check({Nickname, setNickname, ChangeNicknameValid}) {
  // 닉네임, 닉네임 오류 메시지
  const [nickname, setInput] = useState('')
  const [nicknameMsg, setnicknameMsg] = useState('')

  const onChange = (event) => {
    setInput(event.target.value)
  }

  // 닉네임 유효성 검사(2자 ~ 15자, 한글,영어,숫자 포함 가능)
  let nicknameCheck = /^[A-Za-z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2,15}$/
  const nickname_check = () => {
    if (nicknameCheck.test(nickname) && nickname !== '탈퇴한 사용자') {
  axios.get('http://honjarang.kro.kr:30000/api/v1/users/check-nickname',
  {params: {nickname: nickname}})
  .then(function (response) {
    console.log(response)
    setnicknameMsg('')
    setNickname(nickname)
    ChangeNicknameValid()
  })
  .catch(function (error) {
    console.log(error)
    setnicknameMsg('중복된 닉네임입니다.')
  })
 }
else {
  setnicknameMsg('사용할 수 없는 닉네임입니다.')
  console.log('닉네임 오류')
}}

  return (
    <div>
      <div>
        닉네임
        <br />
        <input type="text" name="nickname" onChange={onChange} value={nickname} maxLength="15"/>
        <button className='border-solid border border-black rounded bg-gray2 ml-2'
        onClick = { nickname_check }>중복 확인</button>
        <br />
        <span className="">{nicknameMsg}</span>
      </div>
    </div>
  )
}