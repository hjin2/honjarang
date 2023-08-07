import axios from "axios"
import { useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { setNickname } from "../../redux/slice/UserInfoSlice"

export default function Nickname({setNicknameValid, handleNickname}) {
  // 닉네임, 닉네임 오류 메시지
  const nickname = useSelector((state) => state.userinfo.nickname)
  const [nicknameMsg, setnicknameMsg] = useState('')
  const [nicknameInput, setNicknameInput] = useState(nickname)
  const dispatch = useDispatch()
  const onChange = (event) => {
    setNicknameInput(event.target.value)
  }
		// 닉네임 유효성 검사(2자 ~ 15자, 한글,영어,숫자 포함 가능)
		let nicknameCheck = /^[A-Za-z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2,15}$/
		const nickname_check = () => {
			console.log(nickname)
			console.log(nicknameInput)
			if(nickname!==nicknameInput){
				if (nicknameCheck.test(nickname) && nickname !== '탈퇴한 사용자') {
					axios.get('http://honjarang.kro.kr:30000/api/v1/users/check-nickname',
						{
							params: {nickname : nicknameInput},				
					})
						.then(function (response) {
							console.log(response)
							setnicknameMsg('')
							setNicknameValid()
							handleNickname()
						})
						.catch(function (error) {
							console.log(error)
							setnicknameMsg('중복된 닉네임입니다.')
				})
				}
				else {
					setnicknameMsg('사용할 수 없는 닉네임입니다.')
					console.log('닉네임 오류')
				}
			}else{
				handleNickname()
			}
		}

		
	return (
		<div>
			<div className="flex">
				<input type="text" name="nickname" onChange={onChange} placeholder={nicknameInput} maxLength="15"/>
				<button
					onClick = { nickname_check }
				>
					<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
						<path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
					</svg>
				</button>
			</div>
			<span className="text-main5 text-xs">{nicknameMsg}</span>
		</div>
  )
}