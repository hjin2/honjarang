export default function Article({ article }) {
  return (
    <div className="flex space-x-2 p-1">
      <div>
        <div className="flex space-x-2 ">
          <div className="">{article.title}</div>
          <div className="text-main1">[{article.comment_cnt}]</div>
        </div>
        <div className="flex text-gray3 text-xs space-x-1 mb-1">
          <div className="font-semibold">{article.category}</div>
          <div>{article.created_at?.slice(0, 10)}</div>
        </div>
      </div>
    </div>
  );
}
