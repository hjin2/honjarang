import { useState, useEffect } from "react"

function Chat() {
  const [Msg, setMsg] = useState('')

  const onChange = (e) => {
    setMsg(e.target.value)
  }

  
  return (
    <div>채팅창
      <div>
        <form>
          <input type="text" onChange={onChange}/>
        </form>
      </div>
    </div>
  )
}

export default Chat