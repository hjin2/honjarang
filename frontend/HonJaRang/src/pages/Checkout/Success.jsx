import logoImage from '@/assets/2.png';

export default function Success() {
  const searchParams = new URLSearchParams(window.location.search);

  // 서버로 승인 요청

  return (
    <div className="flex flex-col items-center mt-24">
      <img src={logoImage} className="w-3/12" />
      <div className="text-2xl">결제가</div>
      <div className="text-2xl">완료되었습니다</div>
      <div className="mt-10">{`결제 금액: ${Number(
        searchParams.get('amount'),
      ).toLocaleString()}원`}</div>
    </div>
  );
}
