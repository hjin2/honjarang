import { useState } from "react";

export default function ArticlesList() {
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

  const limit = 10;
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  // useEffect(() => {
  //   fetch("http://localhost8080://boards/1/posts")
  //     .then((res) => res.json())
  //     .then((data) => setPosts(data));
  // }, []);

  return (
    <div className="p-6 h-full">
      {/* <header>
        <h1>게시글 목록</h1>
      </header> */}
      <div className="font-bold text-lg mb-5">최근 작성글 목록</div>
      <hr />
      <div className="w-11/12 mx-auto h-full">
        <div className="flex justify-between font-bold">
          <div className="w-4/6 text-center">제목</div>
          <div className="w-1/6">작성자</div>
          <div className="w-1/6">작성일</div>
        </div>
        <hr />
        <div>
          {posts
            .slice(offset, offset + limit)
            .map(({ id, title, user, date }) => (
              <div key={id}>
                <div  className="flex justify-between text-sm">
                  <div className="w-4/6 text-center">{title}</div>
                  <div className="w-1/6">{user}</div>
                  <div className="w-1/6">{date}</div>
                </div>
                <hr />
              </div>
            ))}
        </div>
      </div>
    </div>
  );
}
