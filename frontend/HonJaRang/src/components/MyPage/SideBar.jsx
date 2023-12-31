import { useState } from 'react';
import ArticleList from '@/components/MyPage/List/ArticleList';
import Tab from '@/components/MyPage/Tab';
import Modal from '@/components/Common/Modal';
import ChargeTab from '@/components/MyPage/Charge/ChargeTab';
import Delivery from '@/components/MyPage/Delivery';
import Transaction from '@/components/MyPage/Transaction';
import Purchase from '@/components/MyPage/Purchase';

function SideBar({ id, isMe }) {
  const [activeTabIndex, setActiveTabIndex] = useState(0);
  const [modalState, setModalState] = useState(false);
  const onModalOpen = () => {
    setModalState(!modalState);
  };

  const tabs = [
    {
      title: '작성글 목록',
      content: <ArticleList id={id} />,
    },
    {
      title: '공동구매 목록',
      content: <Purchase id={id} isMe={isMe} />,
    },
    {
      title: '공동배달 목록',
      content: <Delivery id={id} isMe={isMe} />,
    },
    {
      title: '중고거래 목록',
      content: <Transaction id={id} isMe={isMe} />,
    },
    {
      title: (
        <>
          {isMe ? (
            <div type="button" onClick={onModalOpen}>
              포인트 충전 / 환급
            </div>
          ) : null}
        </>
      ),
    },
  ];

  return (
    <div className="container mx-auto">
      <Tab
        tabs={tabs}
        activeTabIndex={activeTabIndex}
        setActiveTabIndex={setActiveTabIndex}
      />
      {modalState && (
        <Modal modalState={modalState} setModalState={setModalState}>
          <ChargeTab modalState={modalState} setModalState={setModalState} />
        </Modal>
      )}
    </div>
  );
}

export default SideBar;
