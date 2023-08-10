import { useState } from 'react'
import ArticlesList from './List/ArticlesList';
import MarKetList from './List/MarketList';
import Tab from './Tab';
import Modal from '../Common/Modal';
import ChargeTab from './Charge/ChargeTab';
import DeliveryList from './List/DeliveryList';

function SideBar() {
  const [activeTabIndex, setActiveTabIndex] = useState(0);
  const [modalState, setModalState] = useState(false);
  const onModalOpen = () => {
    setModalState(!modalState);
  };

  const tabs = [
    {
      title: '작성글 목록',
      content: <ArticlesList />,
    },
    {
      title: '공동구매 목록',
      content: <MarKetList />,
    },
    {
      title: '공동배달 목록',
      content : <DeliveryList />
    },
    {
      title: (
        <div type="button" onClick={onModalOpen}>
          포인트 충전 / 환급
        </div>
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
          <ChargeTab modalState={modalState} setModalState={setModalState}/>
        </Modal>
      )}
    </div>
  );
}

export default SideBar;
