import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function Stores({ modalState, setModalState, onStoreClick }) {
  
  const token = localStorage.getItem("access_token")
  const URL = import.meta.env.VITE_APP_API

  const [keyword, setKeyword] = useState('');
  const [searchStores, setSearchStores] = useState([]);


  // 가게 검색
  const search = () => {
    const headers = {'Authorization': `Bearer ${token}`};

    axios.get(`${URL}/api/v1/joint-deliveries/stores`, 
      {params: {keyword}, headers: headers,})
      .then((res) => {
        setSearchStores(res.data)
      })
      .catch((err) => {
        console.log(err)
      })
    }

  const handleKeywordChange = (e) => {
    setKeyword(e.target.value);
    console.log(e.target.value)
  };


  // 선택한 가게 전달
  const storeClick = (store) => {
    onStoreClick(store);
    setModalState(false)
  }

  // 엔터로도 검색 가능하도록
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      search();
    }
  };

  // 이미지 없을 때 대체 이미지 넣기
  const defaultImage = '/src/assets/noimage.png';

  return (
    <div className="relative border bg-white m-auto rounded-lg w-3/6 p-3">
      <div className="flex felx-row">
        <input
          type="text"
          value={keyword}
          onChange={handleKeywordChange}
          className="h-8 w-80 border border-gray2 focus:outline-main2 p-2 mr-3"
          placeholder="가게명을 정확하게 입력해 주세요"
          onKeyDown={handleKeyDown}
          />
        <div>
          <button className="main1-button w-20" onClick={search}>검색</button>
        </div>
      </div>
          
      <div className="h-96 overflow-y-scroll">
        {searchStores.map((store) => (
          <div key={store.id} className="flex justify-between m-3">
            <div className="flex flex-row">
            <img className="h-16 w-16 mr-4 rounded-md" 
            src={store.image !== "null" ? store.image : defaultImage} alt={store.keyword} />
              <div> 
                <p className="font-semibold">{store.name}</p>
                <p>{store.address}</p>
              </div>
            </div>
            <button onClick={() => storeClick(store)} className="hover:text-main2">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                <path strokeLinecap="round" strokeLinejoin="round" d="M4.5 12.75l6 6 9-13.5" />
              </svg>
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
