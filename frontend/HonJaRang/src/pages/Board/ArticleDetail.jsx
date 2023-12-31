import { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Comment from '@/components/Board/Comment';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { API } from '@/apis/config';

export const ArticleDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const token = localStorage.getItem('access_token');
  const headers = { Authorization: `Bearer ${token}` };
  const [detail, setDetail] = useState({});
  const [comments, setComments] = useState([]);
  const [isClick, setIsClick] = useState(false);
  const [comment, setComment] = useState('');
  const [user, setUser] = useState({});
  const [isWriter, setIsWriter] = useState(false);
  const [isLiked, setIsLiked] = useState(false);
  const [likedCnt, setLikedCnt] = useState(0);
  const [isNotice, setIsNotice] = useState(false);
  const userId = localStorage.getItem('user_id');
  const handleClick = () => {
    setIsClick(!isClick);
  };

  const clickUser = () => {
    navigate(`/mypage/${detail.user_id}`);
  };
  const addComment = () => {
    const data = {
      content: comment,
    };
    axios
      .post(`${API.POST}/${id}/comments`, data, { headers })
      .then((res) => {
        fetchComments();
      })
      .catch((err) => console.log(err));
  };

  const writeComment = (e) => {
    e.preventDefault();
    if (comment.trim() !== '') {
      console.log(comment);
      addComment();
      setComment('');
    }
  };

  const fetchUser = useCallback((userId) => {
    axios
      .get(`${API.USER}/info`, { params: { id: userId }, headers })
      .then((res) => {
        console.log(res.data);
        setUser(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  useEffect(() => {
    fetchDetail();
    fetchComments();
  }, []);

  const fetchDetail = useCallback(() => {
    axios
      .get(`${API.POST}/${id}`, { headers })
      .then((res) => {
        setDetail(res.data);
        console.log(res.data);
        if (res.data.user_id == userId) {
          setIsWriter(true);
        } else {
          setIsWriter(false);
        }
        fetchUser(userId);
        setIsNotice(res.data.is_notice);
        setIsLiked(res.data.is_liked);
        setLikedCnt(res.data.like_cnt);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [id, headers, userId]);

  const fetchComments = useCallback(() => {
    axios
      .get(`${API.POST}/${id}/comments`, { headers })
      .then((res) => {
        console.log(res.data);
        setComments(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [id, headers]);

  const handelArticleDelete = () => {
    axios
      .delete(`${API.POST}/${id}`, { headers })
      .then((res) => {
        navigate('/board', { replace: true });
      })
      .catch((err) => console.log(err));
  };
  const editArticle = () => {
    navigate(`/board/articleupdate/${id}`);
  };

  const updateComments = (deletedCommentId) => {
    setComments((prevComments) =>
      prevComments.filter((comment) => comment.id !== deletedCommentId),
    );
  };

  const handleNotice = () => {
    setIsNotice(!isNotice);
  };
  const notice = () => {
    axios
      .put(`${API.POST}/${id}/notice`, [], { params: { id: id }, headers })
      .then((res) => {
        console.log(res);
        handleNotice();
      })
      .catch((err) => console.log(err));
  };

  const handleLike = () => {
    axios
      .post(`${API.POST}/${id}/like`, [], { headers })
      .then((res) => {
        console.log(res);
        setIsLiked(!isLiked);
        if (isLiked) {
          setLikedCnt(likedCnt - 1);
        } else {
          setLikedCnt(likedCnt + 1);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 p-5">
      {/* 제목 */}
      <div className="flex justify-between">
        <div className="text-xl font-semibold mb-5">
          [{detail.category}] {detail.title}
        </div>
        {user.role === 'ROLE_ADMIN' ? (
          <>
            {isNotice ? (
              <button className="main5-full-button w-24" onClick={notice}>
                공지취소
              </button>
            ) : (
              <button className="main1-full-button w-24" onClick={notice}>
                공지등록
              </button>
            )}
          </>
        ) : null}
      </div>
      <div className="flex justify-between px-2 my-3">
        <div className="flex">
          <img
            className="w-10 h-10 rounded-full mr-2"
            src={user.profile_image}
            alt="profile_image"
          />
          <div>
            <div
              className="text-xs font-semibold cursor-pointer"
              onClick={clickUser}
            >
              {detail.nickname}
            </div>
            <div className="text-xs">{detail.created_at?.slice(0, 16)}</div>
          </div>
        </div>
        <div className="flex space-x-2">
          <div className="text-xs mr-3 mt-1.5">댓글 {comments.length}</div>

          {isWriter ? (
            <div>
              <button onClick={handleClick}>
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
                    d="M12 6.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 12.75a.75.75 0 110-1.5.75.75 0 010 1.5zM12 18.75a.75.75 0 110-1.5.75.75 0 010 1.5z"
                  />
                </svg>
              </button>
              {isClick ? (
                <div className="absolute border-2 rounded-lg bg-white text-center space-y-2 p-2">
                  <div
                    type="button"
                    className="cursor-pointer"
                    onClick={editArticle}
                  >
                    수정
                  </div>
                  <div
                    type="button"
                    className="coursor-pointer"
                    onClick={handelArticleDelete}
                  >
                    삭제
                  </div>
                </div>
              ) : null}
            </div>
          ) : null}
        </div>
      </div>
      <hr />
      {/* 본문 내용 */}
      <div className="whitespace-pre-line p-2 my-2">
        {detail.post_image ? (
          <img src={detail?.post_image} alt="image" loading="lazy" />
        ) : null}
        <div className="mt-5">{detail.content}</div>
        <div className="flex justify-center mt-10" onClick={handleLike}>
          <button className="mr-3">
            <FontAwesomeIcon
              icon={faThumbsUp}
              size="2xl"
              style={{ color: '#008b57' }}
            />
          </button>
          <div className="mt-2 text-lg font-semibold">{likedCnt}</div>
        </div>
      </div>
      <hr />
      {/* 댓글 */}
      <form className="flex justify-between mt-5 mb-3" onSubmit={writeComment}>
        <input
          type="text"
          className="w-9/12 border-none focus:outline-none"
          placeholder="댓글..."
          value={comment}
          onChange={(e) => setComment(e.target.value)}
        />
        <button type="submit" className="text-gray3">
          댓글 작성
        </button>
      </form>
      {comments.length > 0 ? (
        <div className="space-y-2 px-2">
          {comments.map((comment) => (
            <div key={comment.id}>
              <Comment
                comment={comment}
                id={detail.id}
                updateComments={updateComments}
                fetchUser={fetchUser}
              />
              <hr />
            </div>
          ))}
        </div>
      ) : (
        <div>댓글이 없습니다.</div>
      )}
    </div>
  );
};
export default ArticleDetail;
