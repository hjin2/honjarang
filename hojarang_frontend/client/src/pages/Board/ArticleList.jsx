import Article from './Article';
import { Link } from 'react-router-dom';

export default function AricleList() {
  return (
    <div>
      <Article />
      <div className="flex">
        <Link to="/board/articlecreate">
          <button className="main1-button w-24">작성하기</button>
        </Link>
      </div>
    </div>
  );
}
