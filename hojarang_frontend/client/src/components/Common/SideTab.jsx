import Highlight from '@/assets/Highlight.png'

const activetabStyles = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  backgroundImage: `url(${Highlight})`, // 배경 이미지 경로 설정
  backgroundSize: 'cover',
  padding: '10px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
  fontWeight : "900"
};
const tabStyles = {
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  padding: '10px',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
  color : "#888888",
  fontWeight : "bold"
}


export default function SideTab({ tabs, activeTabIndex, setActiveTabIndex }){

  return (
    <div className='flex space-x-14 mx-auto'>
      <ul className="space-y-10 my-10 mx-auto">
        {tabs.map((tab, index) => (
          <li
            key={index}
            onClick={() => setActiveTabIndex(index)}
            className='w-32 mx-auto'
            style={
              activeTabIndex === index
                ? { ...activetabStyles, fontWeight: 'bold' }
                : tabStyles
            }
          >
            {tab.title}
          </li>
        ))}
      </ul>
    </div>
  )
}
