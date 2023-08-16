import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router';



function ChatList({list, setChatId}) {
  const [Diff, setDiff] = useState('')
  const [msg, setmsg] = useState('')
  const navigate = useNavigate()
  const onClick = () => {
    setChatId(list.id)
  }
  const splitTitle = list.name?.split('&')[1]
  const isContain = list.name.includes("공동")
  
  const Diff_cal = () => {
  let today = new Date();   
  let year = today.getFullYear(); // 년도
  let month = today.getMonth() + 1;  // 월
  let date = today.getDate();  // 날짜
  let day = today.getTime();  // 요일

  if (list.last_message_created_at !== null) {
   let last_time = new Date(list.last_message_created_at)
    let year2 = last_time.getFullYear()
    let month2 = last_time.getMonth() + 1
    let date2 = last_time.getDate()
    let day2 = last_time.getTime()

    if (year - year2 !== 0) {
      setDiff(year - year2)
      setmsg('년 전')
    }
    else if (month - month2 !== 0) {
      setDiff(month - month2)
      setmsg('달 전')

    }
    else if (date - date2 !== 0) {
      setDiff(date - date2)
      setmsg('일 전')
    }
    else if (day - day2 !== 0) {
  
      if (day - day2 >= 60*60*1000) {
        const diff = day - day2
        setDiff(Math.floor(diff / (60*60*1000)))
        setmsg('시간 전')
      }
      else {
        const diff = day - day2
        setDiff(Math.floor(diff / (60*1000)))
        setmsg('분 전')
      }

    }

  }
}

useEffect(() => {
  Diff_cal()
},[])

  
return (
  <div  className="m-2" onClick={onClick}>
    <div className='grid-raw-10'>
    {!isContain ? (
      <span className="font-bold">{splitTitle?.split("1")[0]}</span> 
      ):(
      <span className="font-bold">{list.name}</span>
    )}
    {list.participant_count === 2 ? null : <span className='text-xs mb-2 ml-2'>{list.participant_count}</span>}
    <br />
    {list.last_message_created_at===null ? <span className='text-sm '>아직 작성된 대화가 없습니다.</span> :
    <span className='text-sm'>{list.last_message}</span>
    }
    {list.last_message_created_at===null ? null :
    <span className='text-xs ml-2'>{Diff}{msg}</span>
    }
    {list.unread_message_count === 0 ? null : <div className='w-5 h-5 text-center bg-red-400 mr-auto float-right text-white rounded'>{list.unread_message_count}</div>}
    
    </div>
  </div>
)
}

export default ChatList