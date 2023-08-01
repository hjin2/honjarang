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
    dispatch(deleteArticle({id : article.id}));
    console.log(id)
    // <Alert severity="info">
    //   <AlertTitle>삭제되었습니다.</AlertTitle>
    // </Alert>
  }

  return (
    <div>
      <div>
        [{article.category}] 
        {article.title}
        <button>
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
          <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 12.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 18.75a.75.75 0 110-1.5.75.75 0 010 1.5z" />
        </svg>
        </button>
      </div>
      <div>
        {article.content}
      </div>
      <div>
        <Link to={`/board/articleupdate/${id}`}>
          <button>수정</button>
        </Link>
        <Link to="/board">
          <button onClick={handelArticleDelete}>삭제</button>
        </Link>
      </div>
    </div>
  );
};
export default ArticleDetail;