import React from 'react'

export default function PurchaseDetailProduct({content, image})  {
  return (
    <div className="w-full flex flex-col items-center">
      <img src={image} alt="상품이미지" className="w-4/6"/>
      {content}
    </div>
  );
};
