const Tab = ({ tabs, activeTabIndex, setActiveTabIndex }) => {
  return (
    <div className="flex space-x-14 mx-auto">
      <ul className="border-2 border-main1 rounded-lg basis-1/6 grid">
        {tabs.map((tab, index) => (
          <li
            key={index}
            onClick={() => setActiveTabIndex(index)}
            className="m-auto content-evenly cursor-pointer"
          >
            <div className={`${activeTabIndex === index ? 'font-bold' : ''}`}>
              {tab.title}
            </div>
          </li>
        ))}
      </ul>
      <div className="border border-main1 rounded-lg basis-5/6">
        {tabs[activeTabIndex].content}
      </div>
    </div>
  );
};

export default Tab;
