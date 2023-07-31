import { useState } from "react";
import Pagination from "./Pagination";

function Posts() {
  const [posts, setPosts] = useState([
    {id:1, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:2, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:3, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:4, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:5, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:6, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:7, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:8, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:9, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:10, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:11, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:12, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:13, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:14, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:15, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
    {id:16, title:'test', user: 'ssafy', date:'2023.07.01'},
  ]);


  const limit = 15
  const [page, setPage] = useState(1);
  const offset = (page - 1) * limit;

  // useEffect(() => {
  //   fetch("http://localhost8080://boards/1/posts")
  //     .then((res) => res.json())
  //     .then((data) => setPosts(data));
  // }, []);



  return (
    <div className="flex flex-col items-center m-0">
      <header>
        <h1>게시글 목록</h1>
      </header>

      <main>
        {posts.slice(offset, offset + limit).map(({ id, title, user, date }) => (
          <article key={id}>
            <h3>
              {id} {title} {user} {date}
            </h3> 
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
    </div>);
}

export default Posts;
