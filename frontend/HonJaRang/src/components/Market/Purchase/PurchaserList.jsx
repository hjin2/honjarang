export default function PurchaserList({ modalState, setModalState, purchasers }) {
    return (
      <div className="relative m-auto border bg-white rounded-lg w-4/12 h-4/12 p-3">
        <div className="flex flex-row justify-between pl-24 pr-24 mb-3">
          <div className="text-lg">닉네임</div>
          <div className="text-lg">수량</div>
        </div>
        {purchasers.map((purchaser) => (
          <div key={purchaser.id} className="flex flex-row justify-between pl-24 pr-24 mb-1">
            <div>{purchaser.nickname}</div>
            <div>{purchaser.quantity}</div>
          </div>
        ))}
      </div>
    );
  }
  