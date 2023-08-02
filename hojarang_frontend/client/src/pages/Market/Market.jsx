import Delivery from '../../components/Market/Delivery';
import SideTab from '../../components/Common/SideTab'
import { Purchase } from '../../components/Market/Purchase';
import { Transaction } from '../../components/Market/Transaction';

export default function Market() {
  const tabs = [
    {
      title: '공동구매',
      content:  <Purchase />,
    },
    {
      title: '공동배달',
      content:  <Delivery />,
    },
    {
      title: '중고거래',
      content:  <Transaction />,
    },
  ]
  return (
    <div>
      <SideTab tabs = {tabs}/>
    </div>
  );
}
