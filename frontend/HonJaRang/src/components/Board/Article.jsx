import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';

export default function Article({ article }) {
  const navigate = useNavigate();
  const clickUser = (event) => {
    event.stopPropagation();
    navigate(`/mypage/${article.user_id}`);
  };

  const MAX_ARTICLE_NAME_LENGTH = 14;
  let adjustedArticleName = article.title;
  if (article.title.length > MAX_ARTICLE_NAME_LENGTH) {
    adjustedArticleName =
      article.title.substring(0, MAX_ARTICLE_NAME_LENGTH) + '...';
  }

  const MAX_USER_NICKNAME_LENGTH = 5;
  let adjustedUserName = article.user_nickname;
  if (article.user_nickname.length > MAX_USER_NICKNAME_LENGTH) {
    adjustedUserName =
      article.user_nickname.substring(0, MAX_USER_NICKNAME_LENGTH) + '...';
  }

  return (
    <div className="flex space-x-2">
      <div className="flex w-3/6">
        <div className="mr-4">
          <div className="flex justify-center">
            <FontAwesomeIcon
              icon={faThumbsUp}
              size="lg"
              style={{ color: '#008b57' }}
            />
          </div>
          <div className="text-xs text-center font-bold">
            {article.like_cnt}
          </div>
        </div>
        <div className="flex space-x-1">
          {article.is_notice ? (
            <div className="font-semibold text-main5">공지</div>
          ) : (
            <div className="font-semibold text-gray3">{article.category}</div>
          )}
          <div className="">{adjustedArticleName}</div>
          <div className="text-main1">[{article.comment_cnt}]</div>
        </div>
      </div>
      <div className="w-1/6" onClick={clickUser}>
        {adjustedUserName}
      </div>
      <div className="w-1/6">{article.created_at?.slice(0, 10)}</div>
      <div className="w-1/6">{article.views}</div>
    </div>
  );
}
