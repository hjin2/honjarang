import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router';
import personImage from '@/assets/person.png';
import peopleImage from '@/assets/people.png';
import axios from 'axios';
import { API } from '@/apis/config';

function ChatList({ list }) {
  const [Diff, setDiff] = useState('');
  const [msg, setmsg] = useState('');
  const navigate = useNavigate();
  const onClick = () => {
    navigate(`./${list.id}`, { state: { title: finalTitle } });
  };
  const [user, setUser] = useState()
  let splitTitle = "";
  const isContain = list.name.includes('공동');
  let finalTitle = '';
  if (isContain) {
    finalTitle = list.name;
  } else {
    if(list.name.split('&')[1].includes(user?.nickname)){
      splitTitle = list.name.split('&')[0];
    }else{
      splitTitle = list.name.split('&')[1];
    }

    if(splitTitle.includes("1")){
      finalTitle = splitTitle.split("1")[0]
    }
    else{
      finalTitle = splitTitle
    }
    console.log(finalTitle)
  }

  const Diff_cal = () => {
    let today = new Date();
    let year = today.getFullYear(); // 년도
    let month = today.getMonth() + 1; // 월
    let date = today.getDate(); // 날짜
    let day = today.getTime(); // 요일

    if (list.last_message_created_at !== null) {
      let last_time = new Date(list.last_message_created_at);
      let year2 = last_time.getFullYear();
      let month2 = last_time.getMonth() + 1;
      let date2 = last_time.getDate();
      let day2 = last_time.getTime();

      if (year - year2 !== 0) {
        setDiff(year - year2);
        setmsg('년 전');
      } else if (month - month2 !== 0) {
        setDiff(month - month2);
        setmsg('달 전');
      } else if (date - date2 !== 0) {
        setDiff(date - date2);
        setmsg('일 전');
      } else if (day - day2 !== 0) {
        if (day - day2 >= 60 * 60 * 1000) {
          const diff = day - day2;
          setDiff(Math.floor(diff / (60 * 60 * 1000)));
          setmsg('시간 전');
        } else {
          const diff = day - day2;
          setDiff(Math.floor(diff / (60 * 1000)));
          setmsg('분 전');
        }
      }
    }
  };

  useEffect(() => {
    Diff_cal();
    axios.get(`${API.USER}/info`, {params:{id:localStorage.getItem("user_id")},headers:{"Authorization" : `Bearer ${localStorage.getItem("access_token")}`}})
      .then((res) => {

        setUser(res.data)
      })
  }, []);

  return (
    <div className=" cursor-pointer m-2" onClick={onClick}>
      <div className="flex justify-between">
        <div className="flex">
          {finalTitle.includes('공동') ? (
            <img src={peopleImage} className="h-10 w-10 m-auto mr-2" />
          ) : (
            <img src={personImage} className="h-10 w-10 m-auto mr-2" />
          )}
          <div>
            <div className="flex items-baseline">
              <div className="text-lg font-semibold">{finalTitle}</div>
              {list.participant_count === 2 ? null : (
                <div className="text-xs mb-2 ml-2 text-gray3">
                  {list.participant_count}
                </div>
              )}
            </div>
            <div>
              {list.last_message_created_at === null ? (
                <div className="text-xs text-gray3">
                  아직 작성된 대화가 없습니다.
                </div>
              ) : (
                <div className="text-xs text-gray3">{list.last_message}</div>
              )}
            </div>
          </div>
        </div>
        <div>
          {list.last_message_created_at === null ? null : (
            <div className="text-xs ml-2 text-gray3">
              {Diff}
              {msg}
            </div>
          )}
          {list.unread_message_count === 0 ? null : (
            <div className="w-5 h-5 bg-main5 text-white rounded-full flex justify-center items-center ml-auto text-xs">
              {list.unread_message_count}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ChatList;
