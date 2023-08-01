import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

export const ArticleDetail = () => {
  // const params = useParams();
  // const articles = useSelector(store => store.articles);
  // const viewArticle = articles.filter(article => article.index === params.id);
  // const {title, content} = viewArticle[0];
  // const [arti, setArties] = useState({
  //   title,
  //   content
  // });
  
  const { id } = useParams();
  console.log({id})
  const articles = useSelector((store) => store.articles);
  const article = articles.find((article, index) => index.toString() === id);
  // 게시글 정보가 없을 경우 예외 처리
  if (!article) {
    return <p>게시글을 찾을 수 없습니다.</p>;
  }


  return (
    <div>
      <div>
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
    </div>
  );
};
