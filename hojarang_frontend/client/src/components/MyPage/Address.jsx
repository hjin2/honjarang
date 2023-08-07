import { useDaumPostcodePopup } from 'react-daum-postcode';
import axios from 'axios';

export default function Address({addressInput, setLatitude, setLongitude, setAddressInput, setAddressValid, handleAddress}) {
  const open = useDaumPostcodePopup('https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js');
  const handleComplete = (data) => {
    let fullAddress = data.address;
    console.log(fullAddress);
    axios.get('https://dapi.kakao.com/v2/local/search/address.json',
      {params: {
        analyze_type: 'exact',
        query: fullAddress
      },
        headers: {Authorization: 'KakaoAK bd5e8110fcb76407b6fb34f994dec34f'}},
      )
      .then(function(res) {
        setAddressInput(fullAddress)
        setLatitude(res.data.documents[0].road_address.y)
        setLongitude(res.data.documents[0].road_address.x)
        setAddressInput(fullAddress)
        setAddressValid()
      })
    
  };

  const handleClick = () => {
    open({ onComplete: handleComplete });
  };
  return (
    <div>
      <div>
        <input type="text" value = {addressInput} onClick={handleClick} readOnly/>
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

