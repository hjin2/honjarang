import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams, Link } from 'react-router-dom';
import { deleteArticle }  from '../../redux/slice/articleSlice';

export const ArticleDetail = () => {
  // const params = useParams();
  // const articles = useSelector(store => store.articles);
  // const viewArticle = articles.filter(article => article.index === params.id);
  // const {title, content} = viewArticle[0];
  // const [arti, setArties] = useState({
  //   title,
  //   content
  // });
  const dispatch = useDispatch();
  
  const { id } = useParams();
  // console.log(id)
  const articles = useSelector((store) => store.articles);
  const article = articles.find((article, index) => index.toString() === id);
  // 게시글 정보가 없을 경우 예외 처리
  if (!article) {
    return <p>게시글을 찾을 수 없습니다.</p>;
  }

  const handelArticleDelete = () => {
    dispatch(deleteArticle({id : article.index}));
    console.log(id)
    // <Alert severity="info">
    //   <AlertTitle>삭제되었습니다.</AlertTitle>
    // </Alert>
  }

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 p-5">
      {/* 제목 */}
      <div className="flex justify-between text-xl font-bold">
        <div>
          [{article.category}] {article.title}
        </div>
        <div>
          <button >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 12.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 18.75a.75.75 0 110-1.5.75.75 0 010 1.5z" />
          </svg>
          </button>
        </div>
      </div>

      <div>
        <Link to={`/board/articleupdate/${id}`}>
          <button>수정</button>
        </Link>
        <Link to="/board">
          <button onClick={handelArticleDelete}>삭제</button>
        </Link>
      </div>
      {/* 작성자, 작성일자 */}
      
      <hr />
      {/* 본문 내용 */}
      <div className="whitespace-pre-line p-2">
        {article.content}
      </div>

      {/* 댓글 */}
      <hr />
      <div>
        댓글 개수
      </div>
      <hr />
      <div className="flex justify-between p-5">
        <form action="">
          댓글 작성 폼
        </form>
        <button className="main2-button w-24">댓글 작성</button>
      </div>
      <hr />
      <div>
        댓글들
      </div>
    </div>
  );
};
export default ArticleDetail;