import {useState} from 'react'
import { useSelector } from 'react-redux';


export default function Charge({ modalState, setModalState }) {
  const onClickCloseButton = () =>{
    setModalState(!modalState)
  }
  const point = useSelector((state) => state.userinfo.point)
  const [activeRadio, setActiveRadio] = useState('5000');
  const handleClickRadioButton = (e) => {
    console.log(e.target.value);
    setActiveRadio(e.target.value);
  };

  const newTabClick = () => {
    window.open(`http://localhost:3000/checkout/${activeRadio}`, '결제팝업', 'width=700px,height=800px,scrollbars=yes');
    setModalState(!modalState)
  }

  const tabs = [
    {
      title: '5,000P',
      price: '5000',
    },
    {
      title: '10,000P',
      price: '10000',
    },
    {
      title: '30,000P',
      price: '30000',
    },
    {
      title: '50,000P',
      price: '50000',
    },
  ];
  return (
    <div className='space-y-4'>
      {tabs.map((tab, index) => {
        return (
          <div className="flex items-center" key={index} >
            <input
              type="radio"
              className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500"
              value={tab.price}
              checked={activeRadio === `${tab.price}`}
              id={`radio-${index}`}
              onChange={handleClickRadioButton}
            />
            <label
              htmlFor={`radio-${index}`}
              className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300"
            >
              {tab.title}
            </label>
          </div>
        );
      })}
      <hr />
      <div className='flex justify-between'>
        <div className='space-y-2'>
          <div className='text-xs'>현재 포인트</div>
          <div className='w-20 h-8 border-2 rounded-lg flex items-center text-xs'>{point}P</div>
        </div>
        <div>
          <div className='h-7'></div>
          <div>+</div>
        </div>
        <div className='space-y-2'>
          <div className='text-xs'>결제할 포인트</div>
          <div className='w-20 h-8 border-2 rounded-lg flex items-center text-xs'> {activeRadio}P </div>
        </div>
        <div>
          <div className='h-7'></div>
          <div>=</div>
        </div>
        <div className='space-y-2'>
          <div className='text-xs'>결제후 포인트</div>
          <div className='w-20 h-8 border-2 rounded-lg flex items-center text-xs'> {Number(point) + Number(activeRadio)}P</div>
        </div>
      </div>
      <div className="mt-20 flex justify-between">
        <button className="main1-button w-24" onClick={newTabClick}>결제하기</button>
        {/* <button className='main1-button w-24' onClick={popUp}>
          결제하기
        </button> */}
        <button 
          className="main5-button w-24"
          onClick={onClickCloseButton}>취소하기</button>
      </div>
    </div>
  )
}
