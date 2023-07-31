import React from 'react';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';

export default function Article() {
  const articles = useSelector((store) => store.articles);
  const renderArticle = () =>
    articles.map((article) => (
      <div key={article.id}>
        <Link to={`/board/article/${article.id}`}>
          {article.title}
          {article.content}
          <button>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={1.5}
              stroke="currentColor"
              className="w-6 h-6"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M11.25 4.5l7.5 7.5-7.5 7.5m-6-15l7.5 7.5-7.5 7.5"
              />
            </svg>
          </button>
        </Link>
      </div>
    ));

  return (
    <div>
      <div className="grid gap-5 md:grid-cols-2">
        {articles.length ? (
          renderArticle()
        ) : (
          <p className="text-center col-span-2 text-gray-700 font-semibold">
            게시글이 없습니다.
          </p>
        )}
      </div>
    </div>
  );
}
