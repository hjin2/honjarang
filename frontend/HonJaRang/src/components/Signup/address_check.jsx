import { useDaumPostcodePopup } from 'react-daum-postcode';
import { useState } from 'react';
import axios from 'axios';

function Address_check(props) {
  const [address, setaddress] = useState('')
  const open = useDaumPostcodePopup('https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js');

  const handleComplete = (data) => {
    let fullAddress = data.address;
    let extraAddress = '';

    if (data.addressType === 'R') {
      if (data.bname !== '') {
        extraAddress += data.bname;
      }
      if (data.buildingName !== '') {
        extraAddress += extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName;
      }
      fullAddress += extraAddress !== '' ? ` (${extraAddress})` : '';
    }

    console.log(fullAddress);
    setaddress(fullAddress)
    axios.get('https://dapi.kakao.com/v2/local/search/address.json',
    {params: {
      analyze_type: 'exact',
      query: fullAddress
    },
  headers: {Authorization: 'KakaoAK bd5e8110fcb76407b6fb34f994dec34f'}},
    )
    .then(function(res) {
      props.setAddress(fullAddress)
      props.setLatitude(Number(res.data.documents[0].road_address.y))
      props.setLongitude(Number(res.data.documents[0].road_address.x))
      props.ChangeAddressValid()
    })
    
  };

  const handleClick = () => {
    open({ onComplete: handleComplete });
  };
  return (
    <div>
      <div className='w-full'>
            <label className="font-semibold text-lg text-main2">주소</label>
            <br />
            <input type="text" disabled value = {address} className="inline-block border-gray2 rounded-lg block w-80 h-10 p-2 focus:outline-main2"
            />
            <button className='w-20 h-10 main1-full-button my-10 text-base ml-2'
            onClick={handleClick}>
              주소 검색
            </button>
          </div>
    </div>
  )
}


export default Address_check