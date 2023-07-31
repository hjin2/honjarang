import React, { useEffect, useState } from 'react';
import DaumPostcode from 'react-daum-postcode';
import './post.css';

const Post = (props) => {
  const [enroll_company, setEnroll_company] = useState({
    address: '',
  });

  const [popup, setPopup] = useState(false);

  const handleInput = (e) => {
    setEnroll_company({
      ...enroll_company,
      [e.target.name]: e.target.value,
    });
  };

  const handleComplete = (data) => {
    setPopup(!popup);
  };

  retrun(
    <div className="address_search">
      address
      <input
        className="user_enroll_text"
        placeholder="주소"
        type="text"
        required={true}
        name="address"
        onChange={handleInput}
        value={enroll_company.address}
      />
      <button onClick={handleComplete}>우편번호 찾기</button>
      {popup && (
        <Post company={enroll_company} setcompany={setEnroll_company}></Post>
      )}
    </div>,
  );

  const complete = (data) => {
    let fullAddress = data.address;
    let extraAddress = '';

    if (data.addressType === 'R') {
      if (data.bname !== '') {
        extraAddress += data.bname;
      }
      if (data.buildingName !== '') {
        extraAddress +=
          extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName;
      }
      fullAddress += extraAddress !== '' ? ` (${extraAddress})` : '';
    }
    console.log(data);
    console.log(fullAddress);
    console.log(data.zonecode);

    props.setcompany({
      ...props.company,
      address: fullAddress,
    });
  };

  return (
    <div>
      <DaumPostcode className="postmodal" autoClose onComplete={complete} />
    </div>
  );
};

export default Post;
