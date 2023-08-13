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

  return (
    <div className="relative border bg-white m-auto rounded-lg w-3/6">
      <input
        type="text"
        value={keyword}
        onChange={handleKeywordChange}
        className="h-7 border-gray3 m-2 w-80"
        placeholder="가게명을 정확하게 입력해 주세요"
        onKeyDown={handleKeyDown}
      />
      <button className="main1-button w-20" onClick={search}>검색</button>
      <div className="h-96 overflow-y-scroll">
        {searchStores.map((store) => (
          <div key={store.id} className="flex justify-between m-3">
            <div className="flex flex-row">
            <img className="h-16 w-16 mr-4 rounded-md" src={store.image} alt={store.keyword} />
              <div>
                <p className="font-semibold">{store.name}</p>
                <p>{store.address}</p>
              </div>
            </div>
            <button onClick={() => storeClick(store)}>
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
