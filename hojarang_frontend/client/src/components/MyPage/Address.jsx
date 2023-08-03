import { useDaumPostcodePopup } from 'react-daum-postcode';
import { useState } from 'react';
import axios from 'axios';

export default function Address({setAddress, setLatitude, setLongitude, ChangeAddressValid, handleAddress}) {
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
        setAddress(fullAddress)
        setLatitude(res.data.documents[0].road_address.y)
        setLongitude(res.data.documents[0].road_address.x)
        ChangeAddressValid()
      })
    
  };

  const handleClick = () => {
    open({ onComplete: handleComplete });
  };
  return (
    <div>
      <div>
        <input type="text" value = {address} onClick={handleClick} />
        <button 
          className=''
          onClick={handleAddress}
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="w-6 h-6">
						<path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
					</svg>
        </button>
      </div>
    </div>
  )
}

