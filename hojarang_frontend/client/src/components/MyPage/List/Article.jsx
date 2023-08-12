export default function Article({article}) {
  return (
    <div className="flex space-x-2">
      <div>
        <div className="flex space-x-1">
          <div className="">{article.title}</div>
          <div className="text-main1">[{article.views}]</div>
        </div>
        <div className="flex text-gray3 text-xs space-x-1">
          <div className="font-semibold">{article.category}</div>
          <div>{article.user_nickname}</div>
          <div>{article.created_at?.slice(0,10)}</div>
        </div>
      </div>
    </div>
  )
}
