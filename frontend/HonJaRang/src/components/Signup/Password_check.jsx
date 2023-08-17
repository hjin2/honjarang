import { useState, useEffect } from 'react';

export default function Password_check({ Pwd, setPwd, setPwdValid }) {
  // 비밀번호, 비밀번호 확인, 오류메시지
  const [password, setPassword] = useState('');
  const [password_cfm, setPassword_cfm] = useState('');
  const [pwdMsg, setpwdMsg] = useState('');
  const [pwdcfmMsg, setpwdcfmMsg] = useState('');

  // 비밀번호 유효성 검사(8~15자, 한글/영어/숫자 포함 가능)
  let pwdCheck = /^[A-Za-z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{8,15}$/;
  const pwd_check = (pwd) => {
    if (pwdCheck.test(pwd)) {
      setpwdMsg('');
    } else if (pwd === '') {
      setpwdMsg('');
    } else {
      setpwdMsg('사용할 수 없는 비밀번호입니다.');
    }
  };

  const PwdValidCheck = () => {
    if (password !== '' && password_cfm !== '') {
      setPwd(password);
      setPwdValid(true);
    }
  };

  const pwd_cfm_check = (pwd_cfm) => {
    if (pwd_cfm === '') {
      setpwdcfmMsg('');
      setPwdValid(false);
    } else if (pwd_cfm !== password) {
      setpwdcfmMsg('비밀번호가 일치하지 않습니다.');
      setPwdValid(false);
    } else if (pwd_cfm.length >= 8 && pwd_cfm === password) {
      PwdValidCheck();
      setpwdcfmMsg('');
    }
  };

  useEffect(() => {
    pwd_cfm_check(password_cfm);
    pwd_check(password);
  });

  const onChange_password = (event) => {
    setPassword(event.currentTarget.value);
    pwd_check(event.currentTarget.value);
  };

  const onChange_password_cfm = (event) => {
    setPassword_cfm(event.currentTarget.value);
    pwd_cfm_check(event.currentTarget.value);
  };

  return (
    <div className="mb-3 mt-2">
      <div>
        <label className="font-semibold text-lg text-main2 mb-1">
          비밀번호
        </label>
        <div>
          <input
            type="password"
            onChange={onChange_password}
            maxLength="15"
            className=" border-gray2 rounded-lg  w-60 h-10 p-2 focus:outline-main2"
          />
          <span className="font-semibold text-xs text-main5 ml-2">
            {pwdMsg}
          </span>
        </div>
      </div>
      <div className="mt-4">
        <label className="font-semibold text-lg text-main2 mb-1">
          비밀번호 확인
        </label>
        <div>
          <input
            type="password"
            onChange={onChange_password_cfm}
            maxLength="15"
            className=" border-gray2 rounded-lg w-60 h-10 p-2 focus:outline-main2"
          />
          <span className="font-semibold text-xs text-main5 ml-2">
            {pwdcfmMsg}
          </span>
        </div>
      </div>
    </div>
  );
}
