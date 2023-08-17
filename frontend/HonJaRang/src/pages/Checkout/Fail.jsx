import logoImage from '@/assets/2.png';

export default function Fail() {
  const searchParams = new URLSearchParams(window.location.search);

  // 고객에게 실패 사유 알려주고 다른 페이지로 이동

  return (
    <div className="flex flex-col items-center mt-24">
      <img src={logoImage} className="w-3/12" />
      <div className="text-2xl">결제실패</div>
      {/* <div>{`사유: ${searchParams.get('message')}`}</div> */}
    </div>
  );
}
