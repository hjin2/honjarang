import { useState } from 'react'
import ArticlesList from './ArticlesList';
import MarKetList from './MarketList';
import { Link } from 'react-router-dom';
import Tab from './Tab';
import Modal from '../Common/Modal';
import ChargeTab from './ChargeTab';

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
      title: <Link to="/chatting">채팅 확인하기</Link>,
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