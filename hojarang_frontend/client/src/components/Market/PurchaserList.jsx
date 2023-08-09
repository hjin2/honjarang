export default function PurchaserList({ modalState, setModalState, purchasers }) {
    return (
      <div className="relative m-auto border bg-white rounded-lg w-4/12 h-4/12">
        <div>참여자 목록</div>
        {purchasers.map((purchaser) => (
          <div key={purchaser.id} className="flex">
            <div>{purchaser.nickname}</div>
            <div>{purchaser.quantity}</div>
          </div>
        ))}
      </div>
    );
  }
  