import Rooms from '../Rooms';
import DeliveryRoom from './DeliveryRoom';
import { useEffect, useState } from 'react';
import axios from 'axios';
import Pagination from 'react-js-pagination';
import '../pagination.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { API } from '@/apis/config';

export default function DeliveryList() {
  const token = localStorage.getItem('access_token');
  const headers = { Authorization: `Bearer ${token}` };
  const [deliveryData, setDeliveryData] = useState([]);
  const [pageSize, setPageSize] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [keyword, setKeyword] = useState('');

  useEffect(() => {
    axios
      .get(`${API.DELIVERIES}/page`, {
        params: { size: 12, keyword: keyword },
        headers,
      })
      .then((res) => {
        console.log(res.data);
        setPageSize(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [keyword]);

  const fetchDeliveryData = () => {
    axios
      .get(`${API.DELIVERIES}`, {
        params: { page: currentPage, size: 12, keyword: keyword },
        headers,
      })
      .then((res) => {
        console.log(res.data);
        setDeliveryData(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    fetchDeliveryData();
  }, [currentPage]);

  const setPage = (error) => {
    setCurrentPage(error);
  };

  const search = (e) => {
    e.preventDefault();
    fetchDeliveryData();
  };
  const handleKeyword = (e) => {
    setKeyword(e.target.value);
  };

  return (
    <div>
      {deliveryData.length > 0 ? (
        <div className="h-full">
          <div className="flex justify-end mb-5">
            <form action="" className="space-x-2" onSubmit={search}>
              <input
                className="border border-gray2 focus:outline-main2 h-10 p-2"
                type="text"
                placeholder="검색어"
                onChange={handleKeyword}
              />
              <button>
                <FontAwesomeIcon
                  icon={faMagnifyingGlass}
                  style={{ color: '#008b28' }}
                />
              </button>
            </form>
          </div>
          <Rooms roomsData={deliveryData} component={DeliveryRoom} />
          <div className="flex justify-center">
            <Pagination
              activePage={currentPage}
              itemsCountPerPage={12}
              totalItemsCount={12 * pageSize}
              pageRangeDisplayed={10}
              prevPageText={'<'}
              nextPageText={'>'}
              onChange={setPage}
            />
          </div>
        </div>
      ) : (
        <div className="text-center mt-10 font-bold text-xl">
          배달 목록이 없습니다.
        </div>
      )}
    </div>
  );
}
