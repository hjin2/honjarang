export default function Fail() {
    const searchParams = new URLSearchParams(window.location.search);
  
    // 고객에게 실패 사유 알려주고 다른 페이지로 이동
  
    return (
      <div>
        <h1>결제 실패</h1>
        <div>{`사유: ${searchParams.get("message")}`}</div>
      </div>
    );
  }