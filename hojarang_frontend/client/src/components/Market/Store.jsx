import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function Store({ modalState, setModalState, onStoreClick }) {
  const [keyword, setKeyword] = useState('');
  // const [seletedStore, setSeletedStore] = useState(null); // 선택된 가게
  // const [data, setData] = useState([]); // 가져온 데이터들
  const [searchStores, setSearchStores] = useState([]);

  useEffect(() => {
    fetchSearchStores();
  }, [keyword]);

  
  const fetchSearchStores = async () => {
    try{
      const res = await axios.get('http://honjarang.kro.kr:30000/api/v1/joint-deliveries/stores', 
      {params: {keyword}, });
      setSearchStores(res.data);
    }catch(err) {
      console.log(err)
    }
  };

  const onClickCloseButton = () => {
    setModalState(!modalState);
  };

  const handleKeywordChange = (e) => {
    setKeyword(e.target.value);
    console.log(e.target.value)
  };

  const handleSearch = () => {
    fetchSearchStores();

  }
  const handleStoreClick = (store) => {
    onStoreClick(store.id);
    setModalState(!modalState)
  };


  return (
    <div className="relative border bg-white m-auto rounded-lg w-2/6">
      <input
        type="text"
        value={keyword}
        onChange={handleKeywordChange}
      />
      <button onClick={handleSearch}>검색</button>
      {searchStores.map((store) => (
        <div key={store.id} onClick={() => handleStoreClick(store)}>
          {/* <img src={store.image} alt={store.keyword} /> */}
          <p>{store.keyword}</p>
        </div>
      ))};
    </div>
  );
}
