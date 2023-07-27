import React from 'react'

export default function ChargeModal({ modalState, setModalState }) {
  const onClickCloseButton = () => {
      setModalState(!modalState);
    };
  return (
    <div className='fixed top-0 right-0 bottom-0 left-0 flex items-center justify-center'>
      <div className='bg-gray5 opacity-70 fixed top-0 right-0 bottom-0 left-0' onClick={onClickCloseButton}></div>
      <div className='relative border bg-white m-auto rounded-lg w-2/6'>
        <div className='m-4'>
          <div className='flex justify-between'>
            <div>포인트 결제</div>
            <div>포인트 환급</div>
          </div>
          <div>5000p</div>
          <div>10000p</div>
          <div>30000p</div>
          <div>50000p</div>
          <hr />
          <div>현재 포인트 결제할 포인트 결제 후 포인트</div>
          <div>결제 수단 선택</div>
        </div>
      </div>
    </div>
  )
}
