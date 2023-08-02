import { useState, useEffect } from 'react';
import Pagination from '../Board/Pagination';
import { Link } from 'react-router-dom';

export default function Delivery() {
  const [DeliveryList, setList] = useState([
    { id: 16, title: 'test', user: 'ssafy', date: '2023.07.01' },
  ]);
  const [page, setPage] = useState(1);
  const limit = 15;
  const offset = (page - 1) * limit;

  // useEffect(() => {
  //   fetch('http://localhost:8080/api/v1/joint-delivery')
  //     .then((res) => res.json())
  //     .then((data) => setList(data));
  // });
  return (
    <div className="flex flex-col items-center m-0">
      <header>
        <h1>공동 배달 목록</h1>
      </header>

      <main>
        {DeliveryList.slice(offset, offset + limit).map(
          ({ id, title, user, date }) => (
            <article key={id}>
              <img src="" alt="" />
              <h4>{title}</h4>
              <h4>{user}</h4>
              <Link to={{ pathname: `deliverydetail/${id}` }}>
                <button>참여하기</button>
              </Link>
            </article>
          ),
        )}
      </main>

      <footer>
        <Pagination
          total={DeliveryList.length}
          limit={limit}
          page={page}
          setPage={setPage}
        />
      </footer>
    </div>
  );
}


