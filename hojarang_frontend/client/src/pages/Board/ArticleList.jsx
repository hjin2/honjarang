import Article from './Article';
import { Link } from 'react-router-dom';

export default function AricleList() {
  return (
    <div>
      <Article />
      <Link to="/board/articlecreate">작성하기</Link>
    </div>
  );
}
