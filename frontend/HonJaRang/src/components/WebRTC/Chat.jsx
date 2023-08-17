import { useRef, useEffect } from "react";

export default function Chat({chatMessages, sendChatMessage}) {
  
  const containerRef = useRef(null)
  
  useEffect(() => {
    const container = containerRef.current;
    if (container) {
      container.scrollIntoView({
        behavior: "smooth",
        block: "end",
      })
    }
  }, [chatMessages])
  
  return (
    <div className="border-2 rounded-lg p-6">
      <div 
        className="chat-container p-6 w-12/12 h-full"
        style={{
          height:"90%",
          overflow : "auto",
          width : "350px"
        }}
        >
        <div className="chat-messages"
          ref={containerRef}
        >
          {chatMessages.map((message, index) => (
            <div key={index} className="chat-message">
              <strong>{message.sender}:</strong> {message.text}
            </div>
          ))}
        </div>
      </div>
      <div className="chat-input mt-5">
      <input
        className="border-none focus:outline-none"
        type="text"
        placeholder="채팅..."
        onKeyDown={(e) => {
          if (e.key === 'Enter') {
            sendChatMessage(e.target.value);
            e.target.value = '';
          }
        }}
        />
    </div>
  </div>
  )
}