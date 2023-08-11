import React from 'react'

export default function Article({article}) {
  return (
    <div className="flex">
      <div className="w-1/6">{article.category}</div>
      <div className="w-3/6 text-center">{article.title}</div>
      <div className="w-1/6">{article.created_at?.slice(0,10)}</div>
      <div className="w-1/6">{article.views}</div>
    </div>
  )
}
