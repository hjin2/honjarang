import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router';



function ChatList({list}) {
  const navigate = useNavigate()
  const onClick = () => {
    navigate(`./${list.id}`)
  }


  
  return (
    <div  className="border-b border-solid border-black" onClick={onClick}>
      <div className='grid-raw-10'>
      <span>{list.name}</span> <span>{list.participant_count}</span>
      <br />
      {list.last_message==='메시지 없음' ? <span>아직 작성된 대화가 없습니다.</span> :
      <span>{list.last_message}</span> 
      }
      </div>
      <div className='grid-raw-2'>{list.unread_message_count}</div>
    </div>
  )
}

export default ChatList