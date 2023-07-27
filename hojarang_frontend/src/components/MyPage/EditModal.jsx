import React from 'react';

export default function EditModal({ modalState, setModalState }) {
  const onClickCloseButton = () => {
    setModalState(!modalState);
  };

  return (
    <div className='fixed top-0 right-0 bottom-0 left-0 flex items-center justify-center'>
      <div className='bg-gray5 opacity-70 fixed top-0 right-0 bottom-0 left-0' onClick={onClickCloseButton}></div>
      <div className='relative border bg-white m-auto rounded-lg w-2/6'>
        <div className='p-14 space-y-2'>  
          <div className='flex justify-between mb-4'>
            <div className='font-bold'>회원정보 수정</div>
            <button type="button" onClick={onClickCloseButton}>
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-6 h-6">
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div className='mx-auto border w-32 h-32'></div>
          <div className='flex justify-center'>
            <button type="button" className='main1-button w-48'>프로필 사진 변경하기</button>
          </div>
          <div>
            <div>이메일</div>
            <div>samsung@ssafy.com</div>
          </div>
          <div className='flex justify-between content-center'>
            <div>
              <div>닉네임</div>
              <div>SSAFY01</div>
            </div>
            <button type="button" className='main2-button w-24 text-xs'>변경하기</button>
          </div>
          <button type="button" className='main2-button w-24 text-xs'>비밀번호 변경</button>
          <div className='flex justify-between'>
            <div>
              <div>주소</div>
              <div className='text-xs'>경북 구미시 3공단 3로 302</div>
            </div>
            <button type="button" className='main2-button w-24 text-xs'>주소 변경</button>
          </div>
          <div className='flex justify-center'>
            <button type="button" className='main1-button w-48'>변경 사항 저장하기</button>
          </div>
        </div>
      </div>
    </div>
  );
}
