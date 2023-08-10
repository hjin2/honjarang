export default function Article({article}) {
  return (
  <div className="flex">
    <div className="w-1/6">{article.id}</div>
    <div className="w-2/6 text-center">{article.user_id}</div>
    <div className="w-3/6 text-center">{article.title}</div>
    <div className="w-2/6">{article.created_at?.slice(0,10)}</div>
  </div>
  );
}
