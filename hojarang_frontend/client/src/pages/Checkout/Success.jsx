export default function Success() {
    const searchParams = new URLSearchParams(window.location.search);
  
    // 서버로 승인 요청
  
    return (
      <div>
        <h1>결제 성공</h1>
        <div>{`주문 아이디: ${searchParams.get("orderId")}`}</div>
        <div>{`결제 금액: ${Number(searchParams.get("amount")).toLocaleString()}원`}</div>
      </div>
    );
  }
  