import { useState } from 'react';
import axios from 'axios';
import './signup.css';
import Email_verify from '../../components/Signup/email_verify';
import Nickname_check from '../../components/Signup/nickname_check';
// import Address_check from '../../components/Signup/address_check'
import Post from '../../components/Signup/address_check';
import Password_check from '../../components/Signup/Password_Check';
import { useNavigate } from 'react-router-dom';

export default function Signup() {
  const [EmailValid, setEmailValid] = useState(false);
  const [NicknameValid, setNicknameValid] = useState(false);
  const [PwdValid, setPwdValid] = useState(false);
  const [AddressValid, setAddressValid] = useState(false);

  const ChangeEmailValid = () => {
    setEmailValid(true);
  };

  const ChangeNicknameValid = () => {
    setNicknameValid(true);
  };
  const ChangePwdValid = () => {
    setPwdValid(true);
  };
  const ChangeAddressValid = () => {
    setAddressValid(true);
  };

  const SignupValid =
    EmailValid && NicknameValid && PwdValid && AddressValid ? false : true;

  const navigate = useNavigate();
  const SignupSuccess = () => {
    navigate('./login');
  };

  const onClick = () => {
    axios
      .post('http://honjarang.kro.kr:3/api/v1/users/signup')
      .then(function (res) {
        console.log(res.data);
        SignupSuccess();
      })
      .catch(function (err) {
        console.log(err);
      });
  };
  return (
    <div
      className="w-3/5 h-screen border border-solid border-black rounded
    flex flex-col items-center justify-around"
    >
      <h1>회원가입</h1>
      <Email_verify ChangeEmailValid={ChangeEmailValid} />
      <Nickname_check ChangeNicknameValid={ChangeNicknameValid} />
      <Password_check ChangePwdValid={ChangePwdValid} />
      {/* <Address_check ChangeAddressValid = {ChangeAddressValid}/> */}
      <Post />
      <button
        disabled={SignupValid}
        className="border-solid border border-black rounded bg-main4 mt-2"
        onClick={onClick}
      >
        회원가입
      </button>
    </div>
  );
}
