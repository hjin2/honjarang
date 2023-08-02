import { useState } from 'react';
import Pagination from './Pagination';

function Posts() {
  const [posts, setPosts] = useState([
    { id: 1, title: 'testa', user: 'ssafy', date: '2023.07.01' },
    { id: 2, title: 'testb', user: 'ssafy', date: '2023.07.01' },
    { id: 3, title: 'testc', user: 'ssafy', date: '2023.07.01' },
    { id: 4, title: 'testd', user: 'ssafy', date: '2023.07.01' },
    { id: 5, title: 'teste', user: 'ssafy', date: '2023.07.01' },
    { id: 6, title: 'tessfsdfsdfdfdtf', user: 'ssadddfy', date: '2023.07.01' },
    { id: 7, title: 'testg', user: 'ssafy', date: '2023.07.01' },
    { id: 8, title: 'testh', user: 'ssadfsfdfy', date: '2023.07.01' },
    { id: 9, title: 'testi', user: 'ssafy', date: '2023.07.01' },
    { id: 10, title: 'tedfdfdfsdfsdstj', user: 'ssafy', date: '2023.07.01' },
    { id: 11, title: 'testk', user: 'ssafy', date: '2023.07.01' },
    { id: 12, title: 'testl', user: 'ssafy', date: '2023.07.01' },
    { id: 13, title: 'testm', user: 'ssafy', date: '2023.07.01' },
    { id: 14, title: 'testssssn', user: 'ssafy', date: '2023.07.01' },
    { id: 15, title: 'testoo', user: 'ssafy', date: '2023.07.01' },
    { id: 16, title: 'testp', user: 'ssafy', date: '2023.07.01' },

  ]);

  const limit = 15;
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  // useEffect(() => {
  //   fetch("http://localhost8080://boards/1/posts")
  //     .then((res) => res.json())
  //     .then((data) => setPosts(data));
  // }, []);

  return (
    <div className="flex flex-col items-center m-0 p-5">
      {/* <header>
        <h1>게시글 목록</h1>
      </header> */}

      <div className="w-4/5">
        <main>
          {posts
            .slice(offset, offset + limit)
            .map(({ id, title, user, date }) => (
              <article key={id} className="flex justify-between py-2">
                <p className="w-1/6">{id}</p>
                <p className="w-2/6">{user}</p>
                <p className="w-3/6">{title}</p>
                <p className="w-2/6">{date}</p>
              </article>
            ))}
        </main>
        <footer>
          <Pagination
            total={posts.length}
            limit={limit}
            page={page}
            setPage={setPage}
          />
        </footer>
      </div>

    </div>
  );
}

export default Posts;
